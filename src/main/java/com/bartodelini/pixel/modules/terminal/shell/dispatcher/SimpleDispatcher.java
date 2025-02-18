package com.bartodelini.pixel.modules.terminal.shell.dispatcher;

import com.bartodelini.pixel.environment.Command;
import com.bartodelini.pixel.environment.Environment;
import com.bartodelini.pixel.environment.EnvironmentManager;
import com.bartodelini.pixel.environment.Variable;
import com.bartodelini.pixel.modules.terminal.shell.ExecuteResult;
import com.bartodelini.pixel.modules.terminal.shell.parser.ParseResult;
import com.bartodelini.pixel.modules.terminal.shell.parser.Parser;

import java.util.*;

/**
 * A <i>SimpleDispatcher</i> is used to dispatch parsed commands.
 *
 * @author Bartolini
 * @version 1.1
 */
public class SimpleDispatcher implements Dispatcher {

    private final EnvironmentManager environmentManager;
    private final Parser parser;
    private final Map<String, String> aliasMap;
    private final Variable<Boolean> varCheats;
    private final Set<String> dispatchedAliasSet = new HashSet<>();

    /**
     * Allocates a new {@code SimpleDispatcher} by passing in an {@linkplain EnvironmentManager}, a {@linkplain Parser},
     * a {@linkplain Map} with command aliases as well as a {@linkplain Variable} used to control the dispatching of
     * {@linkplain Command Commands} and {@linkplain Variable Variables} marked as {@code cheat}.
     *
     * @param environmentManager the {@code EnvironmentManager} used for dispatching.
     * @param parser             the {@code Parser} used for dispatching.
     * @param aliasMap           the {@code Map} of aliased commands used for dispatching.
     * @param varCheats          the {@code Variable} used to control the dispatching of {@code Commands} and
     *                           {@code Variables} marked as {@code cheat}.
     * @throws NullPointerException if any of the specified parameters are {@code null}.
     */
    public SimpleDispatcher(EnvironmentManager environmentManager, Parser parser, Map<String, String> aliasMap, Variable<Boolean> varCheats) {
        this.environmentManager = Objects.requireNonNull(environmentManager, "environmentManager must not be null");
        this.parser = Objects.requireNonNull(parser, "parser must not be null");
        this.aliasMap = Objects.requireNonNull(aliasMap, "aliasMap must not be null");
        this.varCheats = Objects.requireNonNull(varCheats, "varCheats must not be null");
    }

    @Override
    public ExecuteResult dispatch(ParseResult parseResult) {
        // Return if the parseResult is null
        if (parseResult == null) {
            return new ExecuteResult(0, "", "");
        }

        // Return if no Environment with matching prefix was found
        if (parseResult.hasPrefix()
                && environmentManager.getAllEnvironments().stream()
                .noneMatch(env -> env.getPrefix().equals(parseResult.getPrefix()))) {
            return new ExecuteResult(-1, parseResult.getInputString(),
                    String.format(
                            "There is no environment with the prefix '[noparse]%s[/noparse]'.",
                            parseResult.getPrefix()));
        }

        // Check for available Environments containing a Command with matching name
        Collection<Environment> environmentsContainingCommand =
                environmentManager.getEnvironmentsContainingCommand(parseResult.getOperableName());
        // Dispatch Command
        if (!environmentsContainingCommand.isEmpty()) {
            // Allocate the StringBuilder for the command dispatching
            StringBuilder stringBuilder = new StringBuilder();
            return dispatchCommand(environmentsContainingCommand, parseResult);
        }

        // Check for available Environments containing a Variable with matching name
        Collection<Environment> environmentsContainingVariable =
                environmentManager.getEnvironmentsContainingVariable(parseResult.getOperableName());
        // Dispatch Variable
        if (!environmentsContainingVariable.isEmpty()) {
            return dispatchVariable(environmentsContainingVariable, parseResult);
        }

        // Check for available command aliases with matching name
        String aliasedCommand = aliasMap.get(parseResult.getOperableName());
        // Dispatch Alias
        if (aliasedCommand != null) {
            return dispatchAlias(aliasedCommand, parseResult);
        }

        // Nothing to dispatch found
        return new ExecuteResult(-1, parseResult.getInputString(),
                "'[noparse]" + (parseResult.hasPrefix() ? parseResult.getPrefix() + "." : "")
                        + parseResult.getOperableName()
                        + "[/noparse]' is not recognized as an internal command or variable.");
    }

    /**
     * Helper method used dispatch a {@linkplain ParseResult} and return the resulting {@linkplain ExecuteResult}.
     *
     * @param environmentsContainingCommand a {@linkplain Collection} with {@linkplain Environment Environments}
     *                                      containing a {@linkplain Command} with matching name.
     * @param parseResult                   the command to dispatch.
     * @return the {@code ExecuteResult} of the dispatching.
     */
    private ExecuteResult dispatchCommand(Collection<Environment> environmentsContainingCommand, ParseResult parseResult) {
        // Allocate the StringBuilder for the command dispatching
        StringBuilder stringBuilder = new StringBuilder();

        if (environmentsContainingCommand.size() == 1 ||
                (parseResult.hasPrefix()
                        && environmentsContainingCommand.stream()
                        .anyMatch(env -> env.getPrefix().equals(parseResult.getPrefix())))) {
            Command command;
            if (environmentsContainingCommand.size() != 1) {
                command = environmentsContainingCommand.stream()
                        .filter(env -> env.getPrefix().equals(parseResult.getPrefix()))
                        .findFirst()
                        .orElseThrow()
                        .getCommand(parseResult.getOperableName());
            } else {
                command = environmentsContainingCommand.iterator().next().getCommand(parseResult.getOperableName());
            }
            if (!command.hasFlags(Command.CHEAT) || varCheats.getValue()) {
                return new ExecuteResult(command.execute(stringBuilder, parseResult.getArgs()),
                        parseResult.getInputString(), stringBuilder.toString());
            }
            return new ExecuteResult(-1, parseResult.getInputString(),
                    "Cannot execute a cheat protected command when cheats are disabled.");
        }

        // Multiple Environments with the same Command found
        StringBuilder sb = new StringBuilder();
        environmentsContainingCommand.forEach(env -> sb.append(env.getPrefix())
                .append(":[color=orange]").append(parseResult.getOperableName()).append("\t\t - ")
                .append(env.getCommand(parseResult.getOperableName()).getDescription()));
        return new ExecuteResult(1, parseResult.getInputString(),
                "Multiple commands with the same name found. Use the prefix to specify the wished command.\n" + sb);
    }

    /**
     * Helper method used dispatch a {@linkplain ParseResult} and return the resulting {@linkplain ExecuteResult}.
     *
     * @param environmentsContainingVariable a {@linkplain Collection} with {@linkplain Environment Environments}
     *                                       containing a {@linkplain Variable} with matching name.
     * @param parseResult                    the command to dispatch.
     * @return the {@code ExecuteResult} of the dispatching.
     */
    @SuppressWarnings("unchecked")
    private ExecuteResult dispatchVariable(Collection<Environment> environmentsContainingVariable, ParseResult parseResult) {
        if (environmentsContainingVariable.size() == 1 ||
                (parseResult.hasPrefix()
                        && environmentsContainingVariable.stream()
                        .anyMatch(env -> env.getPrefix().equals(parseResult.getPrefix())))) {
            Variable<?> variable;
            if (environmentsContainingVariable.size() != 1) {
                variable = environmentsContainingVariable.stream()
                        .filter(env -> env.getPrefix().equals(parseResult.getPrefix()))
                        .findFirst()
                        .orElseThrow()
                        .getVariable(parseResult.getOperableName());
            } else {
                variable = environmentsContainingVariable.iterator().next().getVariable(parseResult.getOperableName());
            }
            if (parseResult.getArgs().isEmpty()) {
                String value = "'" + variable.getValue().toString() + "'";
                if (variable.getType().equals(Integer.class)) {
                    value += " [color=gray](= '0x" + Integer.toHexString((Integer) variable.getValue()) + "'";
                    value += " = '0b" + Integer.toBinaryString((Integer) variable.getValue()) + "')[/color]";
                }
                if (variable.getType().equals(Long.class)) {
                    value += " [color=gray](= '0x" + Long.toHexString((Long) variable.getValue()) + "'";
                    value += " = '0b" + Long.toBinaryString((Long) variable.getValue()) + "')[/color]";
                }
                if (variable.getType().equals(Float.class)) {
                    value += " [color=gray](= '" + Float.toHexString((Float) variable.getValue()) + "'";
                    value += " = '0b" + Integer.toBinaryString(Float.floatToIntBits((Float) variable.getValue())) + "')[/color]";
                }
                if (variable.getType().equals(Double.class)) {
                    value += " [color=gray](= '" + Double.toHexString((Double) variable.getValue()) + "'";
                    value += " = '0b" + Long.toBinaryString(Double.doubleToLongBits((Double) variable.getValue())) + "')[/color]";
                }
                return new ExecuteResult(1, parseResult.getInputString(),
                        "var:[color=cyan]" + variable.getName() + "[/color]"
                                + " [color=gray](" + variable.getType().getSimpleName() + ")[/color]"
                                + " = " + value
                                + " [color=gray](Default = '" + variable.getDefaultValue() + "')[/color]"
                                + "\n └─ " + variable.getDescription());
            }

            if (!variable.hasFlags(Variable.CHEAT) || varCheats.getValue()) {
                Class<?> variableType = variable.getType();
                try {
                    // Variable of type Byte
                    if (variableType.equals(Byte.class)) {
                        String a = parseResult.getArgs().get(0).toLowerCase();
                        byte newVal;
                        if (a.startsWith("0b")) {
                            newVal = Byte.parseByte(parseResult.getArgs().get(0).substring(2), 2);
                        } else if (a.startsWith("-0b")) {
                            newVal = Byte.parseByte("-" + parseResult.getArgs().get(0).substring(3), 2);
                        } else {
                            newVal = Byte.decode(parseResult.getArgs().get(0));
                        }
                        return dispatchVariableByType((Variable<Byte>) variable, newVal, parseResult);
                    }
                    // Variable of type Short
                    if (variableType.equals(Short.class)) {
                        String a = parseResult.getArgs().get(0).toLowerCase();
                        short newVal;
                        if (a.startsWith("0b")) {
                            newVal = Short.parseShort(parseResult.getArgs().get(0).substring(2), 2);
                        } else if (a.startsWith("-0b")) {
                            newVal = Short.parseShort("-" + parseResult.getArgs().get(0).substring(3), 2);
                        } else {
                            newVal = Short.decode(parseResult.getArgs().get(0));
                        }
                        return dispatchVariableByType((Variable<Short>) variable, newVal, parseResult);
                    }
                    // Variable of type Integer
                    if (variableType.equals(Integer.class)) {
                        String a = parseResult.getArgs().get(0).toLowerCase();
                        int newVal;
                        if (a.startsWith("0b")) {
                            newVal = Integer.parseInt(parseResult.getArgs().get(0).substring(2), 2);
                        } else if (a.startsWith("-0b")) {
                            newVal = Integer.parseInt("-" + parseResult.getArgs().get(0).substring(3), 2);
                        } else {
                            newVal = Integer.decode(parseResult.getArgs().get(0));
                        }
                        return dispatchVariableByType((Variable<Integer>) variable, newVal, parseResult);
                    }
                    // Variable of type Long
                    if (variableType.equals(Long.class)) {
                        String a = parseResult.getArgs().get(0).toLowerCase();
                        long newVal;
                        if (a.startsWith("0b")) {
                            newVal = Long.parseLong(parseResult.getArgs().get(0).substring(2), 2);
                        } else if (a.startsWith("-0b")) {
                            newVal = Long.parseLong("-" + parseResult.getArgs().get(0).substring(3), 2);
                        } else {
                            newVal = Long.decode(parseResult.getArgs().get(0));
                        }
                        return dispatchVariableByType((Variable<Long>) variable, newVal, parseResult);
                    }
                    // Variable of type Float
                    if (variableType.equals(Float.class)) {
                        float newVal = Float.parseFloat(parseResult.getArgs().get(0));
                        return dispatchVariableByType((Variable<Float>) variable, newVal, parseResult);
                    }
                    // Variable of type Double
                    if (variableType.equals(Double.class)) {
                        double newVal = Double.parseDouble(parseResult.getArgs().get(0));
                        return dispatchVariableByType((Variable<Double>) variable, newVal, parseResult);
                    }
                    // Variable of type Boolean
                    if (variableType.equals(Boolean.class)) {
                        String argument = parseResult.getArgs().get(0);
                        if (argument.equalsIgnoreCase("true") || argument.equals("1")) {
                            return dispatchVariableByType((Variable<Boolean>) variable, true, parseResult);
                        }
                        if (argument.equalsIgnoreCase("false") || argument.equals("0")) {
                            return dispatchVariableByType((Variable<Boolean>) variable, false, parseResult);
                        }
                        return new ExecuteResult(-1, parseResult.getInputString(),
                                "cannot assign '[noparse]" + parseResult.getArgs().get(0)
                                        + "[/noparse]' to a variable of type '" + variableType.getSimpleName() + "'.");
                    }
                    // Variable of type String
                    if (variableType.equals(String.class)) {
                        String newVal = parseResult.getArgs().get(0);
                        return dispatchVariableByType((Variable<String>) variable, newVal, parseResult);
                    }
                    // Variable of unsupported type
                    return new ExecuteResult(-1, parseResult.getInputString(),
                            "unsupported variable type '[noparse]" + variableType.getSimpleName()
                                    + "'[/noparse], cannot alter the value of this variable via terminal.");

                } catch (NumberFormatException ex) {
                    // Variable cannot be assigned to the specified value
                    return new ExecuteResult(-1, parseResult.getInputString(),
                            "cannot assign '[noparse]" + parseResult.getArgs().get(0)
                                    + "[/noparse]' to a variable of type '" + variableType.getSimpleName() + "'.");
                }
            }
            return new ExecuteResult(-1, parseResult.getInputString(),
                    "Cannot alter a cheat protected variable when cheats are disabled.");
        }
        // Multiple Environments with the same Variable name found
        StringBuilder sb = new StringBuilder();
        environmentsContainingVariable.forEach(env -> sb.append("\n").append(env.getPrefix())
                .append(".[color=cyan]")
                .append(parseResult.getOperableName())
                .append("[/color]\t\t - ")
                .append(env.getVariable(parseResult.getOperableName()).getDescription()));
        return new ExecuteResult(1, parseResult.getInputString(),
                "[color=yellow]Multiple variables with the same name found. Use the prefix to specify the exact variable.[/color]" + sb);
    }

    /**
     * Helper method to dispatch a concrete type of {@linkplain Variable}.
     *
     * @param variable    the {@code Variable} to dispatch.
     * @param newVal      the new value for the {@code Variable}.
     * @param parseResult the command to dispatch.
     * @param <T>         the type of the {@code Variable}.
     * @return the {@code ExecuteResult} of the dispatching.
     */
    private <T extends Comparable<T>> ExecuteResult dispatchVariableByType(Variable<T> variable, T newVal,
                                                                           ParseResult parseResult) {
        String output = "";
        if (variable.hasMax() && newVal.compareTo(variable.getMax()) > 0) {
            output += "[color=yellow]The max value of '[noparse]" + variable.getName()
                    + "[/noparse]' is " + variable.getMax() + ".[/color]";
        } else if (variable.hasMin() && newVal.compareTo(variable.getMin()) < 0) {
            output += "[color=yellow]The min value of '[noparse]" + variable.getName()
                    + "[/noparse]' is " + variable.getMin() + ".[/color]";
        }
        if (variable.setValue(newVal) && variable.hasFlags(Variable.NOTIFY)) {
            output += (output.isEmpty() ? "" : "\n") + "[color=blue]The value of '[color=cyan][noparse]"
                    + variable.getName()
                    + "[/noparse][/color]' has been updated to [color=yellow]";
            output += variable.getValue();
            output += "[/color].[/color]";

            // check for varCheats
            if (variable.equals(varCheats)) {
                if (!varCheats.getValue()) {
                    StringBuilder sb = new StringBuilder();
                    environmentManager.getAllEnvironments().forEach(env -> env.getAllVariables().stream().filter(var -> var.hasFlags(Variable.CHEAT)).forEach(fvar -> {
                        if (fvar.resetValue()) {
                            sb.append("\n[color=blue]The value of '[color=cyan][noparse]")
                                    .append(fvar.getName()).append("[/noparse][/color]' has been reset to [color=yellow]")
                                    .append(fvar.getValue()).append("[/color].[/color]");
                        }
                    }));
                    output += sb.toString();
                } else {
                    output += "\n[color=yellow]Altering cheat protected variables and executing cheat protected commands may have unforeseen consequences, be cautious.[/color] ";
                    return new ExecuteResult(2, parseResult.getInputString(), output);
                }
            }
        }
        return new ExecuteResult(1, parseResult.getInputString(), output);
    }

    /**
     * Helper method used dispatch a {@linkplain ParseResult} and return the resulting {@linkplain ExecuteResult}.
     *
     * @param aliasedCommand the matching aliased command chain.
     * @param parseResult    the command to dispatch.
     * @return the {@code ExecuteResult} of the dispatching.
     */
    private ExecuteResult dispatchAlias(String aliasedCommand, ParseResult parseResult) {
        if (!dispatchedAliasSet.add(parseResult.getOperableName())) {
            return new ExecuteResult(-1, parseResult.getInputString(),
                    String.format(
                            "Circular alias loop found, not executing '%s' to avoid endless dispatch loop.",
                            parseResult.getOperableName()));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("[color=gray]Alias for '").append(parseResult.getOperableName()).append("' found")
                .append(", dispatching: '").append(aliasedCommand).append("'[/color]");
        parser.parseInput(aliasedCommand).forEach(pr -> {
            ExecuteResult executeResult = dispatch(pr);
            sb.append("\n");
            if (executeResult.exitCode() == -1) {
                sb.append("[color=red]")
                        .append(executeResult.output())
                        .append("[/color]");
            } else {
                sb.append(executeResult.output());
            }
        });
        dispatchedAliasSet.remove(parseResult.getOperableName());
        return new ExecuteResult(1, parseResult.getInputString(), sb.toString());
    }
}