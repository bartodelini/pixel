package com.bartodelini.pixel.modules.terminal;

import com.bartodelini.pixel.core.EngineModule;
import com.bartodelini.pixel.environment.Variable;
import com.bartodelini.pixel.logging.LogLevel;
import com.bartodelini.pixel.logging.LogRecordFormatter;
import com.bartodelini.pixel.logging.Logger;
import com.bartodelini.pixel.logging.LoggerFactory;
import com.bartodelini.pixel.modules.input.Input;
import com.bartodelini.pixel.modules.input.Key;
import com.bartodelini.pixel.modules.terminal.commands.*;
import com.bartodelini.pixel.modules.terminal.shell.commands.*;
import com.bartodelini.pixel.modules.terminal.shell.Shell;
import com.bartodelini.pixel.modules.terminal.shell.ShellEnvironment;
import com.bartodelini.pixel.modules.terminal.shell.SimpleShell;
import com.bartodelini.pixel.modules.terminal.shell.dispatcher.SimpleDispatcher;
import com.bartodelini.pixel.modules.terminal.shell.parser.SimpleParser;
import com.bartodelini.pixel.modules.terminal.ui.TerminalUI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;

/**
 * A <i>Terminal</i> is an {@linkplain EngineModule} responsible for providing the user with a command prompt.
 *
 * @author Bartolini
 * @version 1.2
 */
public class Terminal extends EngineModule {

    private final Logger logger = LoggerFactory.getLogger(this);
    private final TerminalUI ui;
    private final Variable<Boolean> varCheats;
    private final Map<String, String> aliasMap = new HashMap<>();
    private final Queue<String> inputQueue = new LinkedList<>();
    private final ShellEnvironment shellEnvironment;

    private Shell shell;

    /**
     * Allocates a new {@code Terminal} object by passing in a {@linkplain TerminalUI}.
     *
     * @param ui the {@code TerminalUI} for this {@code Terminal}.
     * @throws NullPointerException if the specified {@code TerminalUI} is {@code null}.
     */
    public Terminal(TerminalUI ui) {
        super("Terminal", "terminal");

        // Reference the variables
        this.ui = Objects.requireNonNull(ui, "ui must not be null");

        // Add an input consumer to the ui
        ui.addInputConsumer(this::submit);

        // Add variables
        environment.addVariable(this.varCheats = new Variable<>("cheats", false, Variable.NOTIFY,
                "Allows altering of variables marked as 'cheat'."));
        this.varCheats.addChangeHook(preVal -> updateUIPrompt());

        // Add terminal commands
        environment.addCommand(new CmdAlias(aliasMap));
        environment.addCommand(new CmdClear(ui));
        environment.addCommand(new CmdEcho());
        environment.addCommand(new CmdToggleTerminal(this));

        // Create a new ShellEnvironment
        shellEnvironment = new ShellEnvironment(
                System.getProperty("user.name").toLowerCase(), Paths.get(""), Paths.get(""));

        // Add a changeHook to the ShellEnvironment
        shellEnvironment.addChangeHook(se -> updateUIPrompt());

        // Update the ui prompt
        updateUIPrompt();

        // Add shell commands
        environment.addCommand(new CmdPWD(shellEnvironment));
        environment.addCommand(new CmdWhoami(shellEnvironment));
        environment.addCommand(new CmdOpen(shellEnvironment));
        environment.addCommand(new CmdLS(shellEnvironment));
        environment.addCommand(new CmdCD(shellEnvironment));

        // Create a LogRecordFormatter for the Terminal
        LogRecordFormatter formatter = record -> {
            StringBuilder sb = new StringBuilder();
            sb.append("[noparse][[/noparse]");
            switch (record.getLevel()) {
                case ALL -> sb.append("ALL ");
                case DEBUG -> sb.append("[color=debug]DEBG[/color]");
                case FATAL -> sb.append("[color=error]FATAL[/color]");
                case ERROR -> sb.append("[color=error]FAIL[/color]");
                case WARNING -> sb.append("[color=warning]WARN[/color]");
                case INFO -> sb.append("[color=info]INFO[/color]");
                case FINE -> sb.append("[color=fine] OK [/color]");
                default -> sb.append(record.getLevel().toString());
            }
            sb.append("[noparse]] [/noparse]");
            sb.append("[color=light_gray]").append(record.getSource().getSimpleName()).append("[/color]");
            sb.append(": ");
            if (record.getLevel().equals(LogLevel.ERROR)) {
                sb.append("[color=red]").append(record.getMessage()).append("[/color]");
            } else {
                sb.append(record.getMessage());
            }
            return sb.toString();
        };

        // Add a LogRecordHandler for the Terminal
        LoggerFactory.addLogRecordHandler(record -> ui.println(formatter.format(record)));
    }

    @Override
    public void initialize() {
        // Initialize Shell
        this.shell = new SimpleShell(
                shellEnvironment,
                new SimpleParser('"', ';', ' '),
                new SimpleDispatcher(
                        getEnvironmentManager(),
                        new SimpleParser('"', ';', ' '),
                        aliasMap,
                        varCheats));

        // Add commands
        environment.addCommand(new CmdExec(shell));
        environment.addCommand(new CmdHelp(getEnvironmentManager()));

        // Check if the autoexec file exitsts
        File autoexecFile = new File("autoexec.cfg");
        if (!autoexecFile.exists() || autoexecFile.isDirectory()) {
            // Create new autoexec.cfg file
            logger.warning("autoexec.cfg file not found, a new one will be created on exit.");
            try (PrintWriter writer = new PrintWriter("autoexec.cfg")) {
                writer.println("// This .cfg file will be execed during the initialization phase of the engine.");
                writer.println("// Add any commands to this file you wish to execute at this time.");
            } catch (FileNotFoundException | SecurityException e) {
                logger.error("could not write to the autoexec.cfg file", e);
            }
            return;
        }
        logger.info("Execing autoexec.cfg...");
        shell.execute("exec autoexec");
        logger.fine("Finished execing autoexec.cfg.");
    }

    @Override
    public void start() {
        // Show the ui
//        ui.setVisible(true);
    }

    @Override
    public synchronized void fixedUpdate(double fixedDeltaTime) {
        // Return if no new inputs were made
        if (inputQueue.size() == 0) {
            return;
        }

        // Iterate through the inputQueue
        int queueSize = inputQueue.size();
        for (int i = 0; i < queueSize; i++) {
            shell.execute(inputQueue.poll()).stream()
                    .filter(er -> !er.output().isEmpty())
                    .forEach(er -> {
                        if (er.exitCode() == -1) {
                            ui.error(er.output());
                        } else if (er.exitCode() == 2) {
                            ui.warning(er.output());
                        } else {
                            ui.println(er.output());
                        }
                    });
        }

        // Enable the ui to receive input again
        ui.awaitInput();
    }

    @Override
    public void update(double deltaTime) {
        if (Input.isKeyPressed(Key.F12)) {
            setVisible(!isVisible());
        }
    }

    @Override
    public void stop(int exitCode) {
        ui.setPrompt("");
        ui.setInputable(false);
    }

    /**
     * Adds the passed in input to the {@linkplain Queue} of inputs.
     *
     * @param input the input to be added.
     * @throws NullPointerException if the specified input is {@code null}.
     */
    public void submit(String input) {
        Objects.requireNonNull(input, "input must not be null");
        inputQueue.add(input);
    }

    /**
     * Sets the visibility of the {@code Terminal}.
     *
     * @param show if {@code true} shows the {@code Terminal}; otherwise hides the {@code Terminal}.
     */
    public void setVisible(boolean show) {
        ui.setVisible(show);
    }

    /**
     * Returns whether the {@code Terminal} is visible.
     *
     * @return {@code true} if the {@code Terminal} is visible; {@code false} otherwise.
     */
    public boolean isVisible() {
        return ui.isVisible();
    }

    /**
     * Helper method used to update the prompt of the {@linkplain TerminalUI}.
     */
    private void updateUIPrompt() {
        String path;
        if (shellEnvironment.getWorkingPath().equals(shellEnvironment.getHomePath())) {
            path = "~";
        } else {
            if (shellEnvironment.getWorkingPath().toAbsolutePath()
                    .startsWith(shellEnvironment.getHomePath().toAbsolutePath())) {
                path = "~" + shellEnvironment.getWorkingPath().toAbsolutePath().toString()
                        .substring(shellEnvironment.getHomePath().toAbsolutePath().toString().length());
            } else {
                path = shellEnvironment.getWorkingPath().toAbsolutePath().toString();
            }
        }
        ui.setPrompt("[color=green]" + shellEnvironment.getUsername() + "[/color]" +
                "[color=green]@pixel[/color]:" +
                "[color=blue]" + path + "[/color] " +
                (!varCheats.getValue() ? "[color=cyan]$[/color] " : "[color=red]#[/color] "));
    }
}