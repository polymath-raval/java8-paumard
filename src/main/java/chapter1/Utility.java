package chapter1;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;
import lombok.SneakyThrows;

public class Utility {

  @SneakyThrows
  public static Stream<String> getStreamFromFile(String filename) {
    Path path = Paths.get(ClassLoader.getSystemResource(filename).toURI());
    return Files.lines(path);
  }
}
