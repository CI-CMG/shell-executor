package edu.colorado.cires.cmg.shellexecutor;

import java.io.File;
import java.io.IOException;
import java.util.Map;

class ProcessBuilderWrapper {

  private final ProcessBuilder processBuilder;

  ProcessBuilderWrapper(ProcessBuilder processBuilder) {
    this.processBuilder = processBuilder;
  }

  void command(String... command) {
    processBuilder.command(command);
  }

  void directory(File directory) {
    processBuilder.directory(directory);
  }

  Map<String, String> environment() {
    return processBuilder.environment();
  }

  Process start() throws IOException {
    return processBuilder.start();
  }

  void redirectErrorStream(boolean redirectErrorStream) {
    processBuilder.redirectErrorStream(redirectErrorStream);
  }
}
