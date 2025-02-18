package com.bartodelini.pixel.modules.terminal.shell;

/**
 * An <i>ExecuteResult</i> is used to store the result of the execution of commands.
 *
 * @param exitCode the code the command exited with.
 * @param input    the original input string of the command.
 * @param output   the output of the command execution.
 * @author Bartolini
 * @version 1.0
 */
public record ExecuteResult(int exitCode, String input, String output) {
}