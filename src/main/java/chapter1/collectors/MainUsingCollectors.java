package chapter1.collectors;

import static chapter1.Utility.getStreamFromFile;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.Collectors.toSet;

import java.util.Comparator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class MainUsingCollectors {

  private static final String OSPD = "files/ospd.txt";
  private static final String WORDS_USED_BY_SHAKESPEARE = "files/words.shakespeare.txt";

  private final static int[] scrabbleENScore = {
      // a, b, c, d, e, f, g, h, i, j, k, l, m, n, o, p,  q, r, s, t, u, v, w, x, y,  z
      1, 3, 3, 2, 1, 4, 2, 4, 1, 8, 5, 1, 3, 1, 1, 3, 10, 1, 1, 1, 1, 4, 4, 8, 4, 10};
  private static final int[] scrabbleENDistribution = {
      // a, b, c, d,  e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z
      9, 2, 2, 1, 12, 2, 3, 2, 9, 1, 1, 4, 2, 6, 8, 2, 1, 6, 4, 6, 4, 2, 2, 1, 2, 1};

  private final static Function<String, Set<String>> GET_WORDS =
      path -> getStreamFromFile(path).map(String::toLowerCase).collect(toSet());

  private final static Function<String, Map<Integer, Long>> HISTOGRAM_CREATOR =
      word -> word.chars().boxed()
          .collect(groupingBy(i -> i - 'a', counting()));

  private final static Predicate<String> NUMBER_OF_BLANKS_FINDER =
      word -> HISTOGRAM_CREATOR.apply(word).entrySet().stream()
          .collect(Collectors.summingInt(
              entry -> Integer
                  .max(entry.getValue().intValue() - scrabbleENDistribution[entry.getKey()], 0)))
          < 3;

  private final static Function<String, Integer> SCORER_FOR_WORD =
      word -> HISTOGRAM_CREATOR.apply(word).entrySet().stream()
          .mapToInt(entry -> Integer.min(entry.getKey(), scrabbleENDistribution[entry.getKey()])
              * scrabbleENScore[entry.getKey()])
          .sum();

  public static void main(String[] args) {
    Map<Integer, Set<String>> histogramOfWordsByScore =
        GET_WORDS.apply(WORDS_USED_BY_SHAKESPEARE).stream()
            .filter(GET_WORDS.apply(OSPD)::contains)
            .filter(NUMBER_OF_BLANKS_FINDER)
            .collect(groupingBy(SCORER_FOR_WORD
                , () -> new TreeMap<>(Comparator.comparing(Integer::intValue).reversed())
                , toCollection(TreeSet::new)));

    histogramOfWordsByScore.entrySet()
        .stream()
        .limit(3)
        .forEach(entry -> System.out.println(entry));

  }
}
