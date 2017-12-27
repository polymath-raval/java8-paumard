package chapter1;

import java.nio.file.Files;
import java.nio.file.Paths;
import lombok.SneakyThrows;

public class SplitFile {

  private static final String TomSawyer = "files/TomSawyer.txt";
  private static String newline = System.getProperty("line.separator");

  public static void main(String[] args) {
    final long totalNumberOfLines = Utility.getStreamFromFile(TomSawyer).count();
    final long firstCut = (long) (totalNumberOfLines * 0.2);
    final long secondCut = (long) (totalNumberOfLines * 0.5);
    final long thridCut = (long) (totalNumberOfLines * 0.75);
    System.out.printf("firstCut %d secondCut %d thirdCut %d \r\n", firstCut, secondCut, thridCut);
    writeToFile(1, 0, firstCut);
    writeToFile(2, firstCut, secondCut);
    writeToFile(3, secondCut, thridCut);
    writeToFile(4, thridCut, totalNumberOfLines);
  }

  @SneakyThrows
  private static void writeToFile(int suffix, long skip, long limit) {
    Files.write(Paths.get("./TomSawyer_" + suffix + ".txt")
        , Utility.getStreamFromFile(TomSawyer)
            .skip(skip)
            .limit(limit - skip).reduce((s, s2) -> s + newline + s2).get().getBytes());
  }


}
