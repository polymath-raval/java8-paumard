package chapter1.streamOfNumbers;

import static chapter1.Utility.getStreamFromFile;
import static java.util.stream.Collectors.toSet;

import java.util.Comparator;
import java.util.Set;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public class NumberOfWords {

  private static final String OSPD = "files/ospd.txt";
  private static final String WORDS_USED_BY_SHAKESPEARE = "files/words.shakespeare.txt";
  private final static int[] scrabbleENScore = {
      // a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p,  q, r, s, t, u, v, w, x, y,  z
      1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
  private final static Function<String, Set<String>> getWords =
      path -> getStreamFromFile(path).map(String::toLowerCase).collect(toSet());
  private final static ToIntFunction<String> getScore =
      word -> word.chars().map(ch -> scrabbleENScore[ch - 'a']).sum();

  public static void main(String[] args) {
    Set<String> ospd = getWords.apply(OSPD);
    Set<String> shakespeare = getWords.apply(WORDS_USED_BY_SHAKESPEARE);
    String bestWordOnScrabble = shakespeare.stream()
        .filter(ospd::contains)
        .max(Comparator.comparingInt(getScore))
        .get();

    String bestWordOnCheating = shakespeare.stream()
        .max(Comparator.comparingInt(getScore))
        .get();

    System.out.printf("BestWordOnScrabble %s Score %d \r\n", bestWordOnScrabble,
        getScore.applyAsInt(bestWordOnScrabble));
    System.out.printf("BestWordOnCheatingord %s Score %d \r\n", bestWordOnCheating,
        getScore.applyAsInt(bestWordOnCheating));
  }
}
