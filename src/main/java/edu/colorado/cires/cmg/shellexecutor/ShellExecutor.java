package edu.colorado.cires.cmg.shellexecutor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

/**
 *  Interface to execute system commands
 */
public interface ShellExecutor {

  /**
   * Execute a system command, consuming text output from command
   * @param workingDir directory to execute the command in
   * @param outputConsumer a {@link Consumer} that consumes standard out and standard error output from the command
   * @param timeoutMs maximum time to wait for the command to complete
   * @param clearSystemEnv By default, environment variables for the parent java process are available for the command being called.
   * Set this to true to exclude these environment variables from the command.
   * @param args command and arguments to pass to the command
   * @return exit code from the command
   * @throws IOException if an I/O error occurs
   * @throws InterruptedException if the command process is interrupted
   */
  int execute(Path workingDir, Consumer<String> outputConsumer, long timeoutMs, boolean clearSystemEnv, String... args) throws IOException, InterruptedException;

  /**
   * Execute a system command, consuming text output from command
   * @param workingDir directory to execute the command in
   * @param outputConsumer a {@link Consumer} that consumes standard out and standard error output from the command
   * @param timeoutMs maximum time to wait for the command to complete
   * @param args command and arguments to pass to the command
   * @return exit code from the command
   * @throws IOException if an I/O error occurs
   * @throws InterruptedException if the command process is interrupted
   */
  int execute(Path workingDir, Consumer<String> outputConsumer, long timeoutMs, String... args) throws IOException, InterruptedException;

  /**
   * Execute a system command, consuming text output from command
   * @param workingDir directory to execute the command in
   * @param outputConsumer a {@link Consumer} that consumes standard out and standard error output from the command
   * @param timeoutMs maximum time to wait for the command to complete
   * @param environment Environment variables available to the command. Note: Environment variables for the parent java process are also available for the command being called.
   * @param args command and arguments to pass to the command
   * @return exit code from the command
   * @throws IOException if an I/O error occurs
   * @throws InterruptedException if the command process is interrupted
   */
  int execute(Path workingDir, Consumer<String> outputConsumer, long timeoutMs, Map<String, String> environment, String... args) throws IOException, InterruptedException;

  /**
   * Execute a system command, consuming text output from command
   * @param workingDir directory to execute the command in
   * @param outputConsumer a {@link Consumer} that consumes standard out and standard error output from the command
   * @param timeoutMs maximum time to wait for the command to complete
   * @param environment Environment variables available to the command.
   * @param clearSystemEnv By default, environment variables for the parent java process are available for the command being called.
   * Set this to true to exclude these environment variables from the command.
   * @param args command and arguments to pass to the command
   * @return exit code from the command
   * @throws IOException if an I/O error occurs
   * @throws InterruptedException if the command process is interrupted
   */
  int execute(Path workingDir, Consumer<String> outputConsumer, long timeoutMs, Map<String, String> environment, boolean clearSystemEnv, String... args) throws IOException, InterruptedException;

}
