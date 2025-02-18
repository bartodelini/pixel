package com.bartodelini.pixel.modules.terminal.shell.commands;

import com.bartodelini.pixel.environment.Command;
import com.bartodelini.pixel.modules.terminal.shell.ShellEnvironment;

import java.awt.*;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * A <i>CmdOpen</i> is used to open a specified file in the program associated with its extension.
 *
 * @author Bartolini
 * @version 1.1
 */
public class CmdOpen extends Command {

    private final ShellEnvironment shellEnvironment;

    /**
     * Allocates a new {@code CmdOpen} object py passing in a {@linkplain ShellEnvironment}.
     *
     * @param shellEnvironment the {@code ShellEnvironment} to be used by this command.
     * @throws NullPointerException if the specified {@code ShellEnvironment} is {@code null}.
     */
    public CmdOpen(ShellEnvironment shellEnvironment) {
        super("open", "Opens the specified path in the default file explorer.");
        this.shellEnvironment = Objects.requireNonNull(shellEnvironment, "shellEnvironment must not be null");
    }

    @Override
    public int execute(StringBuilder stringBuilder, List<String> args) {
        // Return if no file specified
        if (args == null || args.isEmpty()) {
            return 0;
        }

        // Open the current working directory in file explorer
        if (args.get(0).equals(".")) {
            try {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(shellEnvironment.getWorkingPath().toAbsolutePath().toFile());
            } catch (UnsupportedOperationException e) {
                stringBuilder.append("this operation is not supported by the used OS.");
                return -1;
            } catch (IOException e) {
                stringBuilder.append("the specified file has no associated application or the associated application fails to be launched.");
                return -1;
            } catch (SecurityException e) {
                stringBuilder.append("this operation was denied by the security manager.");
                return -1;
            }
            return 0;
        }

        // Open the specified file
        try {
            Path path = shellEnvironment.getWorkingPath().resolve(args.get(0));
            if (path.toFile().exists()) {
                Desktop desktop = Desktop.getDesktop();
                desktop.open(path.toFile());
            } else {
                stringBuilder.append("'").append(args.get(0)).append("'").append(" does not exist.");
                return -1;
            }
        } catch (InvalidPathException e) {
            stringBuilder.append("'").append(args.get(0)).append("'").append(" does not exist.");
            return -1;
        } catch (SecurityException e) {
            stringBuilder.append("this operation was denied by the security manager.");
            return -1;
        } catch (UnsupportedOperationException e) {
            stringBuilder.append("this operation is not supported by the used OS.");
            return -1;
        } catch (IOException e) {
            stringBuilder.append("the specified file has no associated application or the associated application fails to be launched.");
            return -1;
        }

        return 0;
    }
}