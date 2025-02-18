package com.bartodelini.pixel.modules.terminal.commands;

import com.bartodelini.pixel.environment.Command;
import com.bartodelini.pixel.environment.EnvironmentManager;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * A <i>CmdHelp</i> is used to display information about available commands and variables.
 *
 * @author Bartolini
 * @version 1.0
 */
public class CmdHelp extends Command {

    private final EnvironmentManager environmentManager;
    private final DecimalFormat decimalFormat;

    /**
     * Allocates a new {@code CmdHelp} by passing in an {@linkplain EnvironmentManager}.
     *
     * @param environmentManager the {@code EnvironmentManager} to be used by this {@code CmdHelp}.
     * @throws NullPointerException if the specified {@code EnvironmentManager} is {@code null}.
     */
    public CmdHelp(EnvironmentManager environmentManager) {
        super("help", "Displays this help screen.");
        this.environmentManager = Objects.requireNonNull(environmentManager, "environmentManager must not be null");
        decimalFormat = new DecimalFormat("0", DecimalFormatSymbols.getInstance(Locale.ENGLISH));
        decimalFormat.setMaximumFractionDigits(340);
    }

    @Override
    public int execute(StringBuilder stringBuilder, List<String> args) {
        AtomicInteger commandCount = new AtomicInteger();
        AtomicInteger variableCount = new AtomicInteger();
        AtomicInteger longestEntry = new AtomicInteger();
        environmentManager.getAllEnvironments().forEach(env -> env.getAllCommands()
                .forEach(com -> {
                    commandCount.getAndIncrement();
                    if (longestEntry.get() < com.getName().length()) {
                        longestEntry.set(com.getName().length());
                    }
                }));
        environmentManager.getAllEnvironments().forEach(env -> env.getAllVariables()
                .forEach(var -> {
                    variableCount.getAndIncrement();
                    String valueString;
                    if (var.getType().isAssignableFrom(Number.class)) {
                        valueString = decimalFormat.format(var.getValue());
                    } else {
                        valueString = var.getValue().toString();
                    }
                    if (longestEntry.get() < var.getName().length() + valueString.length() + 5) {
                        longestEntry.set(var.getName().length() + valueString.length() + 5);
                    }
                }));

        stringBuilder.append("Displaying a total of [color=yellow]").append(commandCount)
                .append("[/color] [color=green]commands[/color] and [color=yellow]").append(variableCount)
                .append("[/color] [color=cyan]variables[/color] in [color=yellow]")
                .append(environmentManager.getAllEnvironments().size())
                .append("[/color] [color=orange]environments[/color]:\n");

        environmentManager.getAllEnvironments().stream().sorted().forEach(env -> {
            stringBuilder.append("env:[color=orange]").append(env.getPrefix()).append("[/color]\n");
            AtomicInteger currentCommand = new AtomicInteger(1);
            AtomicInteger currentVariable = new AtomicInteger(1);
            env.getAllCommands().stream().sorted().forEach(com -> {
                String paddedCommand = String.format("%-" + longestEntry.get() + "s", com.getName());
                stringBuilder
                        .append(" ")
                        .append(currentCommand.get() == env.getAllCommands().size() && env.getAllVariables().isEmpty() ? "└" : "├")
                        .append("─ com:[color=green]").append(paddedCommand).append("[/color]  - ")
                        .append(com.getDescription()).append("\n");
                currentCommand.getAndIncrement();
            });
            env.getAllVariables().stream().sorted().forEach(var -> {
                String valueString;
                if (var.getType().isAssignableFrom(Number.class)) {
                    valueString = decimalFormat.format(var.getValue());
                } else {
                    valueString = var.getValue().toString();
                }
                int paddingLength =
                        (longestEntry.get() - var.getName().length() - valueString.length() - 5);
                String padding = "";
                if (paddingLength > 0) {
                    padding = String.format("%-" + paddingLength + "s", "");
                }
                stringBuilder
                        .append(" ")
                        .append(currentVariable.get() == env.getAllVariables().size() ? "└" : "├").append("─ var:[color=cyan]")
                        .append(var.getName()).append("[/color] = '").append(valueString).append("'")
                        .append(padding).append("  - ").append(var.getDescription()).append("\n");
                currentVariable.getAndIncrement();
            });
        });
        return 1;
    }
}