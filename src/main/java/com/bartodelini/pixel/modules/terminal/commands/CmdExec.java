package com.bartodelini.pixel.modules.terminal.commands;

import com.bartodelini.pixel.environment.Command;
import com.bartodelini.pixel.modules.terminal.shell.Shell;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

/**
 * A <i>CmdExec</i> is used to execute commands with a specified {@linkplain Shell}.
 *
 * @author Bartolini
 * @version 1.0
 */
public class CmdExec extends Command {

    private final Shell shell;

    private final Set<String> execedConfigs = new HashSet<>();

    /**
     * Allocates a new {@code CmdExec} by passing in a {@linkplain Shell}.
     *
     * @param shell the {@code Shell} used to execute commands.
     * @throws NullPointerException if the specified {@code Shell} is {@code null}.
     */
    public CmdExec(Shell shell) {
        super("exec", "Executes a specified config file.");
        this.shell = Objects.requireNonNull(shell, "shell must not be null");
    }

    @Override
    public int execute(StringBuilder stringBuilder, List<String> args) {
        // Return if no filename was specified
        if (args.isEmpty()) {
            stringBuilder.append("No filename specified.");
            return -1;
        }

        // Add the file extension if its missing
        String filename = args.get(0);
        if (!filename.endsWith(".cfg")) {
            filename += ".cfg";
        }

        // Read the file path
        Path filePath = Path.of(filename);

        // Check for additional arguments
        if (args.contains("-l") || args.contains("-list")) {
            AtomicInteger lineCount = new AtomicInteger();

            try (Stream<String> stream = Files.lines(filePath)) {
                // List all lines of the specified config file
                stringBuilder.append(String.format(
                        "[color=blue]Displaying the content of '[color=yellow]%s[/color]'[/color]", filename));
                stream.forEach(l -> {
                    lineCount.getAndIncrement();
                    stringBuilder.append("\n[color=yellow]")
                            .append(String.format("%1$2d", lineCount.get()).replace(' ', '0'))
                            .append("[/color]: [color=gray]").append("'").append(l).append("'[/color]");
                });
                stringBuilder.append("\n[color=blue]Finished displaying '[color=yellow]")
                        .append(filename)
                        .append("[/color]' (Total of [color=yellow]").append(lineCount)
                        .append((lineCount.get() > 1) ? "[/color] lines)" : "[/color] line)")
                        .append("[/color]");
            } catch (IOException e) {
                // No config file with the specified name found
                stringBuilder.append(String.format(
                        "No file with the name '%s' found in the default directory.", filename));
            }
            return 1;
        }

        // Check for circular exec reference to avoid an endless exec loop
        if (!execedConfigs.add(filename)) {
            // Return if circular exec loop found
            stringBuilder.append("Circular exec loop found, not execing to avoid endless exec loop.");
            return -1;
        }

        AtomicInteger lineCount = new AtomicInteger();
        AtomicInteger instructionCount = new AtomicInteger();

        try (Stream<String> stream = Files.lines(filePath)) {
            // Exec file
            stringBuilder.append(String.format("[color=blue]Execing '[color=yellow]%s[/color]' ...[/color]", filename));
            stream.forEach(l -> {
                // Ignore commented lines
                if (l.startsWith("//")) {
                    return;
                }

                lineCount.getAndIncrement();
                if (args.contains("-s") || args.contains("-show")) {
                    stringBuilder.append("\n[color=yellow]")
                            .append(String.format("%1$2d", lineCount.get()).replace(' ', '0'))
                            .append("[/color]: [color=gray]").append("'").append(l).append("'[/color]");
                }
                shell.execute(l).stream()
                        .filter(er -> !er.output().isEmpty())
                        .map(er -> "\n" + (er.exitCode() == -1 ?
                                padAllLines("[color=red][background=black]" + er.output(), 4) + "[/background][/color]" :
                                padAllLines(er.output(), 4)))
                        .forEach(e -> {
                            stringBuilder.append(e);
                            instructionCount.getAndIncrement();
                        });
            });
        } catch (IOException e) {
            // No config file with the specified name found
            stringBuilder.append(String.format("No file with the name '%s' found in the default directory.", filename));
            execedConfigs.remove(filename);
            return -1;
        }

        // Print execing information
        stringBuilder.append("\n")
                .append(String.format("[color=blue]Finished execing '[color=yellow]%s[/color]'", filename))
                .append(" (Total of [color=yellow]").append(instructionCount)
                .append((instructionCount.get() > 1) ? "[/color] instructions)" : "[/color] instruction)")
                .append("[/color]");
        execedConfigs.remove(filename);
        return 1;
    }

    /**
     * Helper method used to pad all lines in a {@code String}.
     *
     * @param str     the text to pad.
     * @param padding the amount of padding in number of spaces.
     * @return the string with all lines padded by the specified amount.
     * @throws NullPointerException if the specified string is {@code null}.
     */
    private String padAllLines(String str, int padding) {
        Objects.requireNonNull(str, "str must not be null");
        String[] split = str.split("\n");
        for (int i = 0; i < split.length; i++) {
            split[i] = String.format("%1$" + ((padding == 0) ? "" : padding) + "s", "") + split[i];
        }
        return String.join("\n", split);
    }
}