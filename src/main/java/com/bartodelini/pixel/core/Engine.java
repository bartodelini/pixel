package com.bartodelini.pixel.core;

import com.bartodelini.pixel.ecs.SceneManager;
import com.bartodelini.pixel.environment.Command;
import com.bartodelini.pixel.environment.Environment;
import com.bartodelini.pixel.environment.EnvironmentManager;
import com.bartodelini.pixel.environment.Variable;
import com.bartodelini.pixel.event.EventBus;
import com.bartodelini.pixel.logging.*;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;

/**
 * An <i>Engine</i> is a container object for {@linkplain EngineModule} objects.
 * <p>
 * It implements a <i>main loop</i> which calls appropriate methods in a fixed time interval ({@code fixedUpdate}), as
 * well as a flexible interval ({@code update}). The provided {@code interpolate} method allows for an interpolation
 * between game states of adjacent {@code fixedUpdate} calls.
 *
 * @author Bartolini
 * @version {@value VERSION}
 */
public class Engine {

    /**
     * The name of the {@code Engine}.
     */
    public static final String NAME = "pixel";

    /**
     * The version of the {@code Engine}.
     */
    public static final String VERSION = "1.1";

    private final SceneManager sceneManager;
    private final ModuleManager moduleManager;
    private final EnvironmentManager environmentManager;
    private final EventBus eventBus;
    private final Logger logger = LoggerFactory.getLogger(this);
    private final Variable<Integer> e_fups;
    private final Queue<Runnable> invokeOnStartQueue = new LinkedList<>();

    private Thread engineLoopThread;
    private volatile boolean running = false;
    private int exitCode = 0;
    double fixedDeltaTime;
    double fixedDeltaTimeNS;

    /**
     * Allocates a new {@code Engine} object be passing in a {@linkplain SceneManager} and the target amount of fixed
     * updates per second (fups).
     *
     * @param sceneManager the {@code SceneManager} which will be attached to all {@code EngineModules} of this
     *                     {@code Engine} instance.
     * @param fups         the target amount of fixed updates per second.
     * @throws NullPointerException if the specified {@code SceneManager} is {@code null}.
     */
    public Engine(SceneManager sceneManager, int fups) {
        this.sceneManager = Objects.requireNonNull(sceneManager, "sceneManager must not be null");
        this.moduleManager = new ModuleManager();
        this.environmentManager = new EnvironmentManager();
        this.eventBus = new EventBus();

        // Create an Environment for the Engine
        Environment environment = new Environment("engine");
        environmentManager.addEnvironment(environment);

        // Add Commands
        environment.addCommand(new Command("exit", "Exits the Engine.") {
            @Override
            public int execute(StringBuilder stringBuilder, List<String> args) {
                if (args != null && args.size() > 0) {
                    try {
                        int exitCode = Integer.parseInt(args.get(0));
                        Engine.this.exit(exitCode);
                        return 1;
                    } catch (NumberFormatException e) {
                        // ERR_NUMBER_FORMAT
                        return -1;
                    }
                } else {
                    Engine.this.exit();
                    return 1;
                }
            }
        });

        // Add Variables
        e_fups = new Variable<>("fups", fups, true, 1, false, Integer.MAX_VALUE,
                Variable.CHEAT | Variable.NOTIFY, "Target amount of fixed updates per second.");
        environment.addVariable(e_fups);

        // Add a change hook to update the timings on e_fups change
        e_fups.addChangeHook(integer -> {
            fixedDeltaTime = 1.0D / e_fups.getValue();
            fixedDeltaTimeNS = (1000 * 1000 * 1000) * fixedDeltaTime;
        });
    }

    /**
     * Starts the engine by creating a new engine loop {@linkplain Thread} and calling its start method. If the engine
     * is already running, does nothing.
     */
    public void start() {
        if (!isRunning() && engineLoopThread == null) {
            running = true;
            engineLoopThread = new Thread(this::run, "Engine Loop Thread");
            engineLoopThread.start();
        }
    }

    /**
     * Exits the engine by calling its {@code exit(int code)} method with exit code 0.
     */
    public void exit() {
        exit(0);
    }

    /**
     * Exits the engine by setting the running variable to false, which in turn exits the main engine loop. The
     * application exits after the exit method finishes on all {@linkplain EngineModule EngineModules} with the exit
     * code.
     *
     * @param code the exit code the application will exit with.
     */
    public void exit(int code) {
        if (isRunning()) {
            exitCode = code;
            running = false;
        }
    }

    /**
     * Returns whether the engine is running by checking the value of the {@code running} variable.
     *
     * @return {@code true} if the engine is running; {@code false} otherwise.
     */
    public boolean isRunning() {
        return running;
    }

    /**
     * This method implements the engine loop.
     */
    private synchronized void run() {
        // Initialize phase
        logger.info("Engine started.");
        logger.info("Initialization phase begins.");
        try {
            moduleManager.initializeAll();
        } catch (ModuleInitializeException e) {
            UIUtils.showExceptionDialog("Engine Initialization Error  :(",
                    "Could not initialize the " + e.getEngineModule().getName() + " module.\n", e);
            logger.info("Initialization phase ends.");
            exit(-1);
            terminate();
            return;
        }
        logger.info("Initialization phase ends.");

        // Start phase
        logger.info("Start phase begins.");
        // Start all modules
        moduleManager.startAll();
        // Invoke all tasks waiting for engine start
        logger.info("Running all tasks waiting for engine start...");
        invokeOnStartQueue.forEach(Runnable::run);
        logger.info("Finished running all tasks waiting for engine start.");
        logger.info("Start phase ends.");

        logger.info("Entering main loop.\n");

        // Set up variables needed for the engine loop
        fixedDeltaTime = 1.0D / e_fups.getValue();
        fixedDeltaTimeNS = (1000 * 1000 * 1000) * fixedDeltaTime;
        double accumulatorNS = 0;
        double maxFrameTimeNS = 250000000;
        double lastTimeNS = System.nanoTime();
        double currentTimeNS, frameTimeNS;

        // Enter engine loop
        while (running) {
            // Update timing variables
            currentTimeNS = System.nanoTime();
            frameTimeNS = currentTimeNS - lastTimeNS;
            lastTimeNS = currentTimeNS;

            // Cap the frameTime to the limit
            if (frameTimeNS >= maxFrameTimeNS) {
                frameTimeNS = maxFrameTimeNS;
            }

            accumulatorNS += frameTimeNS;

            // Perform fixedUpdates
            while (accumulatorNS >= fixedDeltaTimeNS) {
                try {
                    fixedUpdate(fixedDeltaTime);
                } catch (Exception e) {
                    UIUtils.showExceptionDialog("Engine Runtime Error  :(",
                            "An error occurred when calling fixedUpdate.", e);
                    exit(-1);
                }
                accumulatorNS -= fixedDeltaTimeNS;
            }

            // Perform interpolation
            try {
                interpolate(accumulatorNS / fixedDeltaTimeNS);
            } catch (Exception e) {
                UIUtils.showExceptionDialog("Engine Runtime Error  :(",
                        "An error occurred when calling interpolate.", e);
                exit(-1);
            }

            // Perform update
            try {
                update(frameTimeNS / 1000000000);
            } catch (Exception e) {
                UIUtils.showExceptionDialog("Engine Runtime Error  :(",
                        "An error occurred when calling update.", e);
                exit(-1);
            }

            // Perform lateUpdate
            try {
                lateUpdate();
            } catch (Exception e) {
                UIUtils.showExceptionDialog("Engine Runtime Error  :(",
                        "An error occurred when calling lateUpdate.", e);
                exit(-1);
            }
        }

        // Exit phase
        terminate();
    }

    /**
     * Helper method used to perform the update.
     *
     * @param deltaTime the delta time between {@code update} calls.
     */
    private void update(double deltaTime) {
        moduleManager.updateAll(deltaTime);
    }

    /**
     * Helper method used to perform the lateUpdate.
     */
    private void lateUpdate() {
        moduleManager.lateUpdateAll();
    }

    /**
     * Helper method used to perform the update.
     *
     * @param fixedDeltaTime the fixed delta time between {@code fixedUpdate} calls.
     */
    private void fixedUpdate(double fixedDeltaTime) {
        moduleManager.fixedUpdateAll(fixedDeltaTime);
    }

    /**
     * Helper method used to perform the interpolation.
     *
     * @param alpha the missed fraction of the {@code fixedUpdate} in the current update cycle.
     */
    private void interpolate(double alpha) {
        moduleManager.interpolateAll(alpha);
    }

    /**
     * Helper method used to perform the stop stage.
     */
    private void terminate() {
        logger.info("Exit phase begins.");
        moduleManager.stopAll(exitCode);
        logger.info("Exit phase ends.");
        logger.info("Exiting engine with code " + exitCode);
        LogRecordWriter logWriter = new LogRecordWriter(LoggerFactory.getLogList(), new LogRecordFormatter() {
            final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    .withZone(ZoneId.systemDefault());

            @Override
            public String format(LogRecord record) {
                return dateFormatter.format(record.getInstant()) + " "
                        + record.getLevel().toString() + " "
                        + record.getSource().getSimpleName() + ": "
                        + record.getMessage();
            }
        });
        logWriter.writeToFile("engineLog.log");
        System.exit(exitCode);
    }

    /**
     * Adds the given {@linkplain Runnable} to the {@linkplain Queue} of {@code Runnables} to be run as the engine
     * starts.
     *
     * @param runnable a {@code Runnable} to be run at engine start.
     * @throws NullPointerException  if the specified {@code Runnable} is {@code null}.
     * @throws IllegalStateException if this method was invoked while the {@code Engine} was running.
     */
    public void invokeOnStart(Runnable runnable) {
        Objects.requireNonNull(runnable, "runnable must not be null");
        if (isRunning()) {
            throw new IllegalStateException("cannot add to start queue when engine is running");
        }
        invokeOnStartQueue.add(runnable);
    }

    /**
     * Adds the given {@linkplain EngineModule} by calling the {@code addModule} method of the {@code ModuleManager}
     * object. Then adds the modules {@linkplain Environment} to the {@linkplain EnvironmentManager} by calling
     * its {@code addEnvironment} method and sets the {@linkplain SceneManager}, {@linkplain EventBus} and the
     * {@code EnvironmentManager} of the {@code EngineModule}. This method should only be called when the {@code Engine}
     * is not running, otherwise an exception is thrown.
     *
     * @param engineModule the {@code EngineModule} to add.
     * @throws IllegalStateException if the {@code Engine} is running or if the {@code EngineModule} is active.
     */
    public void addModule(EngineModule engineModule) {
        if (isRunning()) {
            throw new IllegalStateException("cannot add an EngineModule when the engine is running");
        }
        if (engineModule.isActive()) {
            throw new IllegalArgumentException("cannot add an active EngineModule");
        }
        // Add the EngineModule
        moduleManager.addModule(engineModule);

        // Add the Environment of the EngineModule to the EnvironmentManager
        environmentManager.addEnvironment(engineModule.getEnvironment());

        // Set the SceneManager, EventBus and EnvironmentManager of the EngineModule
        engineModule.setSceneManager(sceneManager);
        engineModule.setEventBus(eventBus);
        engineModule.setEnvironmentManager(environmentManager);
    }

    /**
     * Removes the given {@linkplain EngineModule} by calling the {@code removeModule} method of the
     * {@linkplain ModuleManager} object. Then removes the modules {@linkplain Environment} from the
     * {@linkplain EnvironmentManager} by calling its {@code removeEnvironment} method. This method should only be
     * called when the {@code Engine} is not running, otherwise an exception is thrown.
     *
     * @param engineModule the {@code EngineModule} to be removed.
     * @throws IllegalStateException if the {@code Engine} is running.
     */
    public void removeModule(EngineModule engineModule) {
        if (isRunning()) {
            throw new IllegalStateException("cannot remove an EngineModule when the engine is running");
        }
        // Remove the EngineModule from the ModuleManager
        moduleManager.removeModule(engineModule);

        // Remove the Environment of the EngineModule from the EnvironmentManager
        environmentManager.removeEnvironment(engineModule.getEnvironment());
    }

    /**
     * Returns the {@code SceneManager}.
     *
     * @return the {@code SceneManager}.
     */
    public SceneManager getSceneManager() {
        return sceneManager;
    }

    /**
     * Returns the {@code ModuleManager}.
     *
     * @return the {@code ModuleManager}.
     */
    public ModuleManager getModuleManager() {
        return moduleManager;
    }

    /**
     * Returns the {@code EnvironmentManager}
     *
     * @return the {@code EnvironmentManager}
     */
    public EnvironmentManager getEnvironmentManager() {
        return environmentManager;
    }
}