package edu.colorado.cires.cmg.shellexecutor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

/**
 * This is the default implementation of {@link ShellExecutor}.
 */
public class DefaultShellExecutor implements ShellExecutor {

  @Override
  public int execute(Path workingDir, Consumer<String> outputConsumer, long timeoutMs, boolean clearSystemEnv, String... args)
      throws IOException, InterruptedException {
    return execute(workingDir, outputConsumer, timeoutMs, Collections.emptyMap(), clearSystemEnv, args);
  }

  @Override
  public int execute(Path workingDir, Consumer<String> outputConsumer, long timeoutMs, String... args) throws IOException, InterruptedException {
    return execute(workingDir, outputConsumer, timeoutMs, Collections.emptyMap(), false, args);
  }

  @Override
  public int execute(Path workingDir, Consumer<String> outputConsumer, long timeoutMs, Map<String, String> environment, String... args)
      throws IOException, InterruptedException {
    return execute(workingDir, outputConsumer, timeoutMs, environment, false, args);
  }

  @Override
  public int execute(Path workingDir, Consumer<String> outputConsumer, long timeoutMs, Map<String, String> environment, boolean clearSystemEnv, String... args)
      throws IOException, InterruptedException {
    ProcessBuilderWrapper builder = newProcessBuilder();
    builder.command(args);
    builder.directory(workingDir.toFile());
    if (clearSystemEnv) {
      builder.environment().clear();
    }
    builder.environment().putAll(environment);
    builder.redirectErrorStream(true);
    Process process = builder.start();

    Thread gobbler = new Thread(new StreamGobbler(process.getInputStream(), outputConsumer));
    gobbler.start();

    if (process.waitFor(timeoutMs, TimeUnit.MILLISECONDS)) {
      gobbler.join(1000);
      return process.exitValue();
    } else {
      process.destroyForcibly();
      gobbler.join(1000);
      throw new RuntimeException("shell command failed to complete in " + timeoutMs + " milliseconds");
    }
  }

  // visible for testing
  protected ProcessBuilderWrapper newProcessBuilder(){
    return new ProcessBuilderWrapper(new ProcessBuilder());
  }
}
