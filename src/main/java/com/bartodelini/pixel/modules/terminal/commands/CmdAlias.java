package com.bartodelini.pixel.modules.terminal.commands;

import com.bartodelini.pixel.environment.Command;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * A <i>CmdAlias</i> is used to manipulate and retrieve aliases for command chains.
 *
 * @author Bartolini
 * @version 1.0
 */
public class CmdAlias extends Command {

    private final Map<String, String> aliasMap;

    /**
     * Allocates a new {@code CmdAlias} by passing in its {@linkplain Map} of aliases.
     *
     * @param aliasMap the {@code Map} with aliases.
     * @throws NullPointerException if the specified {@code Map} is {@code null}.
     */
    public CmdAlias(Map<String, String> aliasMap) {
        super("alias", "Creates an alias for a command chain.");
        this.aliasMap = Objects.requireNonNull(aliasMap, "aliasMap must not be null");
    }

    @Override
    public int execute(StringBuilder stringBuilder, List<String> args) {
        if (args == null || args.size() == 0) {
            // Return if there are no aliased command chains
            if (aliasMap.isEmpty()) {
                stringBuilder.append("No aliased command chains.");
                return -1;
            }
            // Show all aliased command chains
            stringBuilder
                    .append(String.format(
                            "[color=blue]Displaying [color=yellow]%d[/color] aliased command chain", aliasMap.size()))
                    .append(aliasMap.size() == 1 ? ":" : "s:").append("[/color]");
            aliasMap.forEach((key, value) -> stringBuilder.append("\n").append(key).append(":\t '").append(value).append("'"));
        } else if (args.size() == 1) {
            String alias = aliasMap.get(args.get(0));
            if (alias != null) {
                // Show the specified aliased command chain
                stringBuilder.append(args.get(0)).append(" : '").append(alias).append("'");
            } else {
                // No aliased command chain found under the specified name
                stringBuilder.append("alias usage: <alias name> <command chain>");
                return -1;
            }
        } else {
            // Put the alias into the aliasMap
            aliasMap.put(args.get(0), args.get(1));
        }
        return 1;
    }
}