package chapter1.customcollectors;

import static chapter1.Utility.getStreamFromFile;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collector;
import java.util.stream.Collector.Characteristics;
import java.util.stream.Stream;
import org.apache.commons.lang3.tuple.Pair;

@SuppressWarnings("ALL")
public class MainMoviesActor {

  private static final String MOVIES_FILE = "files/movies-mpaa.txt";
  private static final String strPattern = "\\((.*?)\\)";
  private static final Pattern pattern = Pattern.compile(strPattern);
  private static final String SLASH = "/";
  private static final String BLANK = "";
  private static final String COMMA = ",";


  public static void main(String[] args) {
    List<Movie> movies = movies();
    printNumberOfActors(movies);
    printActorWithMaximumMovies(movies);
    printActorWithMaximumMoviesInOneYear(movies);
    printActorWithMaximumMoviesInOneYearWithCustomCollector(movies);
  }

  private static void printNumberOfActors(List<Movie> movies) {
    System.out.printf("# Actors: %d \n",
        movies.stream().flatMap(movie -> movie.getActors().stream()).distinct().count());
  }

  private static void printActorWithMaximumMovies(List<Movie> movies) {
    Map<Actor, Long> actorHistogram = movies.stream().flatMap(movie -> movie.getActors().stream())
        .collect(groupingBy(Function.identity(), counting()));

    System.out.printf("# Actor with maximum number of movies %s \n",
        actorHistogram.entrySet().stream().max(Map.Entry.comparingByValue()).get());
  }

  private static void printActorWithMaximumMoviesInOneYear(List<Movie> movies) {
    Function<Movie, List<Pair<Integer, Actor>>> deNormalizer =
        movie -> movie.getActors().stream().map(actor -> Pair.of(movie.getReleaseYear(), actor))
            .collect(toList());
    Map<Pair<Integer, Actor>, Long> yearActorHistogram = movies.stream()
        .flatMap(movie -> deNormalizer.apply(movie).stream())
        .collect(groupingBy(Function.identity(), counting()));

    System.out.printf("# Actor with maximum number of movies in an year %s \n",
        yearActorHistogram.entrySet().stream().max(Map.Entry.comparingByValue()).get());
  }

  private static void printActorWithMaximumMoviesInOneYearWithCustomCollector(List<Movie> movies) {
    BinaryOperator<HashMap<Actor, AtomicLong>> combiner =
        (map1, map2) -> {
          map2.entrySet().forEach(
              entry -> map1.merge(entry.getKey(), entry.getValue(), (al1, al2) -> {
                al1.addAndGet(al2.get());
                return al1;
              }
          ));
          return map1;
        };

    Map<Integer, HashMap<Actor, AtomicLong>> yearActorHistogram = movies.stream()
        .collect(groupingBy(Movie::getReleaseYear,
            Collector.of(
                () -> new HashMap<Actor, AtomicLong>(),
                (map, movie) -> movie.getActors().forEach(
                    actor -> map.computeIfAbsent(actor, a -> new AtomicLong()).incrementAndGet()),
                combiner,
                Characteristics.IDENTITY_FINISH
            )
        ));

    System.out.printf("# Actor with maximum number of movies in an year with custom collector %s \n",
        yearActorHistogram.entrySet().stream()
            .flatMap(entry -> entry.getValue().entrySet().stream())
            .max(Comparator.comparing(al -> al.getValue().get())).get());
  }

  private static List<Movie> movies() {
    Stream<String> lines = getStreamFromFile(MOVIES_FILE);
    return lines.parallel().map(MainMoviesActor::transform).collect(toList());
  }

  private static Movie transform(String line) {
    String[] tokens = line.split(SLASH);
    return Movie.builder()
        .title(tokens[0].replaceAll(strPattern, BLANK))
        .releaseYear(releaseYear(tokens[0]))
        .actors(Stream.of(tokens).skip(1).map(MainMoviesActor::buildActor).collect(toSet()))
        .build();
  }

  private static int releaseYear(String token) {
    Matcher matcher = pattern.matcher(token);
    int year = -1;
    while (matcher.find()) {
      year = Integer.parseInt(matcher.group(1).substring(0, 4));
    }
    return year;
  }

  private static Actor buildActor(String token) {
    String[] names = token.split(COMMA);
    return names.length == 0 ?
        Actor.builder().build() :
        names.length == 1 ?
            Actor.builder().firstName(names[0].trim()).build() :
            Actor.builder().lastName(names[0].trim()).firstName(names[1].trim()).build();
  }
}
