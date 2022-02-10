package edu.colorado.cires.cmg.shellexecutor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;
import java.util.function.Consumer;

public interface ShellExecutor {

  int execute(Path workingDir, Consumer<String> outputConsumer, long timeoutMs, boolean clearSystemEnv, String... args) throws IOException, InterruptedException;
  int execute(Path workingDir, Consumer<String> outputConsumer, long timeoutMs, String... args) throws IOException, InterruptedException;
  int execute(Path workingDir, Consumer<String> outputConsumer, long timeoutMs, Map<String, String> environment, String... args) throws IOException, InterruptedException;
  int execute(Path workingDir, Consumer<String> outputConsumer, long timeoutMs, Map<String, String> environment, boolean clearSystemEnv, String... args) throws IOException, InterruptedException;

}
