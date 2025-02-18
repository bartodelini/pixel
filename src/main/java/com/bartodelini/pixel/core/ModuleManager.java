package com.bartodelini.pixel.core;

import com.bartodelini.pixel.logging.Logger;
import com.bartodelini.pixel.logging.LoggerFactory;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * A <i>ModuleManager</i> is used to hold and coordinate {@linkplain EngineModule EngineModules}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class ModuleManager {

    private final List<EngineModule> engineModuleList = new LinkedList<>();
    private final List<EngineModule> initializedEngineModuleList = new LinkedList<>();
    private final Logger logger = LoggerFactory.getLogger(this);

    /**
     * Initializes all {@linkplain EngineModule EngineModules} present in this {@code ModuleManager}.
     *
     * @throws ModuleInitializeException if any of the {@code EngineModules} failed during the initialization stage.
     */
    public void initializeAll() throws ModuleInitializeException {
        logger.info("Initializing all modules...");
        for (EngineModule engineModule : engineModuleList) {
            try {
                double startTime = System.nanoTime();
                engineModule.initialize();
                double endTime = System.nanoTime();
                initializedEngineModuleList.add(engineModule);
                logger.fine(engineModule.getName() + " initialized in " + ((endTime - startTime)) / (1000 * 1000) + "ms.");
            } catch (ModuleInitializeException e) {
                logger.fatal("Failed initializing " + engineModule.getName() + ".");
                throw e;
            }
        }
        logger.fine("All modules successfully initialized.");
    }

    /**
     * Starts all {@linkplain EngineModule EngineModules} present in this {@code ModuleManager}.
     */
    public void startAll() {
        logger.info("Starting all modules...");
        for (EngineModule engineModule : initializedEngineModuleList) {
            engineModule.start();
            logger.fine(engineModule.getName() + " started.");
        }
        logger.fine("All modules started.");
    }

    /**
     * Calls {@code fixedUpdate} on all {@linkplain EngineModule EngineModules} present in this {@code ModuleManager}.
     */
    public void fixedUpdateAll(double fixedDeltaTime) {
        initializedEngineModuleList.forEach(m -> m.fixedUpdate(fixedDeltaTime));
    }

    /**
     * Calls {@code update} on all {@linkplain EngineModule EngineModules} present in this {@code ModuleManager}.
     */
    public void updateAll(double deltaTime) {
        initializedEngineModuleList.forEach(m -> m.update(deltaTime));
    }

    /**
     * Calls {@code lateUpdate} on all {@linkplain EngineModule EngineModules} present in this {@code ModuleManager}.
     */
    public void lateUpdateAll() {
        initializedEngineModuleList.forEach(EngineModule::lateUpdate);
    }

    /**
     * Calls {@code interpolate} on all {@linkplain EngineModule EngineModules} present in this {@code ModuleManager}.
     */
    public void interpolateAll(double alpha) {
        initializedEngineModuleList.forEach(m -> m.interpolate(alpha));
    }

    /**
     * Calls {@code stop} on all {@linkplain EngineModule EngineModules} present in this {@code ModuleManager} with the
     * passed in exitCode.
     *
     * @param exitCode the exitCode to be passed down to all {@code EngineModules}.
     */
    public void stopAll(int exitCode) {
        logger.info("Stopping all modules...");
        initializedEngineModuleList.forEach(m -> {
            m.stop(exitCode);
            logger.fine(m.getName() + " stopped.");
        });
        logger.fine("All modules stopped.");
    }

    /**
     * Returns whether all {@linkplain EngineModule EngineModules} present in this {@code ModuleManager} have been
     * initialized.
     *
     * @return {@code} true if all {@code EngineModules} have been initialized; {@code false} otherwise.
     */
    public boolean allInitialized() {
        return engineModuleList.size() == initializedEngineModuleList.size();
    }

    /**
     * Adds the passed in {@linkplain EngineModule} to this {@code ModuleManager}.
     *
     * @param engineModule the {@code EngineModule} to be added to this {@code ModuleManager}.
     * @throws NullPointerException if the specified {@code EngineModule} is {@code null}.
     */
    public void addModule(EngineModule engineModule) {
        Objects.requireNonNull(engineModule, "engineModule must not be null");
        engineModuleList.add(engineModule);
    }

    /**
     * Removes the passed in {@linkplain EngineModule} from this {@code ModuleManager}.
     *
     * @param engineModule the {@code EngineModule} to be removed from this {@code ModuleManager}.
     * @throws NullPointerException if the specified {@code EngineModule} is {@code null}.
     */
    public void removeModule(EngineModule engineModule) {
        Objects.requireNonNull(engineModule, "engineModule must not be null");
        engineModuleList.remove(engineModule);
    }

    /**
     * Returns an unmodifiable {@linkplain List} of all {@linkplain EngineModule EngineModules} present in this
     * {@code ModuleManager}.
     *
     * @return an unmodifiable {@code List} of all {@code EngineModules}.
     */
    public List<EngineModule> getEngineModuleList() {
        return Collections.unmodifiableList(engineModuleList);
    }

    /**
     * Returns an unmodifiable {@linkplain List} of all initialized {@linkplain EngineModule EngineModules} present in
     * this {@code ModuleManager}.
     *
     * @return an unmodifiable {@code List} of all initialized {@code EngineModules}.
     */
    public List<EngineModule> getInitializedEngineModuleList() {
        return Collections.unmodifiableList(initializedEngineModuleList);
    }
}