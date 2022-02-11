package edu.colorado.cires.cmg.shellexecutor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

class DefaultShellExecutorTest {
  @Test
  public void testSuccessNoClearEnv() throws Exception{
    Path workingDir = Paths.get("foo");
    List<String> output = new ArrayList<>();
    Consumer<String> outputConsumer = output::add;
    long timeoutMs = 1000;
    Map<String, String> environment = new HashMap<>();
    environment.put("fido", "lost");
    environment.put("env1", "true");
    boolean clearSystemEnv = false;
    String[] args = new String[]{"cmd", "arg1", "arg2"};


    InputStream processOutput = new ByteArrayInputStream("line1\nline2".getBytes(StandardCharsets.UTF_8));
    Map<String, String> mockEnvironment = new HashMap<>();
    mockEnvironment.put("fido", "found");
    mockEnvironment.put("env2", "false");
    ProcessBuilderWrapper processBuilder = mock(ProcessBuilderWrapper.class);
    Process process = mock(Process.class);
    when(processBuilder.start()).thenReturn(process);
    when(processBuilder.environment()).thenReturn(mockEnvironment);
    when(process.getInputStream()).thenReturn(processOutput);
    when(process.exitValue()).thenReturn(3);
    boolean exited = true;
    when(process.waitFor(timeoutMs, TimeUnit.MILLISECONDS)).thenAnswer(new Answer<Boolean>() {
      @Override
      public Boolean answer(InvocationOnMock invocationOnMock) throws Throwable {
        Thread.sleep(200);
        return exited;
      }
    });
    DefaultShellExecutor shellExecutor = new DefaultShellExecutor(){
      @Override
      protected ProcessBuilderWrapper newProcessBuilder() {
        return processBuilder;
      }
    };

    int exitcode = shellExecutor.execute(workingDir, outputConsumer, timeoutMs, environment, clearSystemEnv, args);
    verify(processBuilder,times(1)).command(args);
    verify(processBuilder,times(1)).directory(eq(workingDir.toFile()));
    Map<String,String> expectedEnvironment = new HashMap<>();
    expectedEnvironment.put("fido", "lost");
    expectedEnvironment.put("env1", "true");
    expectedEnvironment.put("env2", "false");
    assertEquals(expectedEnvironment, mockEnvironment);
    verify(processBuilder,times(1)).redirectErrorStream(true);
    List<String> expectedOutput = Arrays.asList("line1", "line2");
    assertEquals(expectedOutput, output);
    assertEquals(3, exitcode);
  }

  @Test
  public void testSuccessClearEnv() throws Exception{
    Path workingDir = Paths.get("foo");
    List<String> output = new ArrayList<>();
    Consumer<String> outputConsumer = output::add;
    long timeoutMs = 1000;
    Map<String, String> environment = new HashMap<>();
    environment.put("fido", "lost");
    environment.put("env1", "true");
    boolean clearSystemEnv = true;
    String[] args = new String[]{"cmd", "arg1", "arg2"};


    InputStream processOutput = new ByteArrayInputStream("line1\nline2".getBytes(StandardCharsets.UTF_8));
    Map<String, String> mockEnvironment = new HashMap<>();
    mockEnvironment.put("fido", "found");
    mockEnvironment.put("env2", "false");
    ProcessBuilderWrapper processBuilder = mock(ProcessBuilderWrapper.class);
    Process process = mock(Process.class);
    when(processBuilder.start()).thenReturn(process);
    when(processBuilder.environment()).thenReturn(mockEnvironment);
    when(process.getInputStream()).thenReturn(processOutput);
    when(process.exitValue()).thenReturn(3);
    boolean exited = true;
    when(process.waitFor(timeoutMs, TimeUnit.MILLISECONDS)).thenAnswer(new Answer<Boolean>() {
      @Override
      public Boolean answer(InvocationOnMock invocationOnMock) throws Throwable {
        Thread.sleep(200);
        return exited;
      }
    });
    DefaultShellExecutor shellExecutor = new DefaultShellExecutor(){
      @Override
      protected ProcessBuilderWrapper newProcessBuilder() {
        return processBuilder;
      }
    };

    int exitcode = shellExecutor.execute(workingDir, outputConsumer, timeoutMs, environment, clearSystemEnv, args);
    verify(processBuilder,times(1)).command(args);
    verify(processBuilder,times(1)).directory(eq(workingDir.toFile()));
    Map<String,String> expectedEnvironment = new HashMap<>();
    expectedEnvironment.put("fido", "lost");
    expectedEnvironment.put("env2", "false");
    assertEquals(expectedEnvironment, mockEnvironment);
    verify(processBuilder,times(1)).redirectErrorStream(true);
    List<String> expectedOutput = Arrays.asList("line1", "line2");
    assertEquals(expectedOutput, output);
    assertEquals(3, exitcode);
  }


  @Test
  public void testRunTimeException() throws Exception {
    Path workingDir = Paths.get("foo");
    List<String> output = new ArrayList<>();
    Consumer<String> outputConsumer = output::add;
    long timeoutMs = 1000;
    Map<String, String> environment = new HashMap<>();
    environment.put("fido", "lost");
    environment.put("env1", "true");
    boolean clearSystemEnv = true;
    String[] args = new String[]{"cmd", "arg1", "arg2"};
    try {
      InputStream processOutput = new ByteArrayInputStream("line1\nline2".getBytes(StandardCharsets.UTF_8));
      Map<String, String> mockEnvironment = new HashMap<>();
      mockEnvironment.put("fido", "found");
      mockEnvironment.put("env2", "false");
      ProcessBuilderWrapper processBuilder = mock(ProcessBuilderWrapper.class);
      Process process = mock(Process.class);
      when(processBuilder.start()).thenReturn(process);
      when(processBuilder.environment()).thenReturn(mockEnvironment);
      when(process.getInputStream()).thenReturn(processOutput);
      when(process.exitValue()).thenReturn(3);
      boolean exited = false;
      when(process.waitFor(timeoutMs, TimeUnit.MILLISECONDS)).thenAnswer(new Answer<Boolean>() {
        @Override
        public Boolean answer(InvocationOnMock invocationOnMock) throws Throwable {
          Thread.sleep(200);
          return exited;
        }
      });
      DefaultShellExecutor shellExecutor = new DefaultShellExecutor() {
        @Override
        protected ProcessBuilderWrapper newProcessBuilder() {
          return processBuilder;
        }
      };

      shellExecutor.execute(workingDir, outputConsumer, timeoutMs, environment, clearSystemEnv, args);
    } catch (final RuntimeException e) {
      final String msg = "shell command failed to complete in " + timeoutMs + " milliseconds";
      assertEquals(msg, e.getMessage());
    }
  }
}