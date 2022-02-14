# shell-executor

The shell-executor project contains utilities to execute system commands and handle output appropriately

Additional project information, javadocs, and test coverage is located at https://ci-cmg.github.io/project-documentation/shell-executor/

## Adding To Your Project

Add the following dependency to your Maven pom.xml

```xml
    <dependency>
      <groupId>io.github.ci-cmg</groupId>
      <artifactId>shell-executor</artifactId>
      <version>1.0.0</version>
    </dependency>
```
## Usage
The shell executor can be executed in the following ways:
```java
  ShellExecutor shellExecutor = new new DefaultShellExecutor();
  shellExecutor.execute(workingDir, logger, timeout, clearSystemEnv, args);
  shellExecutor.execute(workingDir, logger, timeout, environment, args);
  shellExecutor.execute(workingDir, logger, timeout, environment, clearSystemEnv args);
```
1. workingDir - directory path to execute the command
2. logger - a consumer that consumes standard out and standard error output from the command.
3. timeout - maximum time in milliseconds for the command to complete
4. environment - environment variables available to the command
5. clearSystemEnv - Set this to true to exclude environment variables for the parent java process from the command
6. args - array of command and arguments to pass to the command
