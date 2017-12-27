package chapter1.flatmap;

import static chapter1.Utility.getStreamFromFile;

import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class ConcatStreamExample {

  private static final String TEXT = "files/TomSawyer.txt";
  private static final String TEXT1 = "files/TomSawyer_1.txt";
  private static final String TEXT2 = "files/TomSawyer_2.txt";
  private static final String TEXT3 = "files/TomSawyer_3.txt";
  private static final String TEXT4 = "files/TomSawyer_4.txt";
  private static Pattern pattern = Pattern.compile(" ");

  public static void main(String[] args) {
    System.out.println(getStreams()
        .flatMap(Function.identity())
        .flatMap(pattern::splitAsStream)
        .map(String::toLowerCase)
        .distinct()
        .count());

    System.out.println(getStreamFromFile(TEXT)
        .flatMap(pattern::splitAsStream)
        .map(String::toLowerCase)
        .distinct()
        .count());

  }

  private static Stream<Stream<String>> getStreams() {
    return Stream.of(
        getStreamFromFile(TEXT1),
        getStreamFromFile(TEXT2),
        getStreamFromFile(TEXT3),
        getStreamFromFile(TEXT4));
  }


}
