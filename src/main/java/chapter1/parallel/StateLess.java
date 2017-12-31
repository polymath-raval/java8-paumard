package chapter1.parallel;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.Builder;
import lombok.SneakyThrows;
import lombok.Value;

public class StateLess {
  public static final int MAX_SIZE = 10_000_000;

  @SneakyThrows
  public static void main(String[] args) {
    System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism","0");
    System.out.println("Sleeping "+Instant.now());
    TimeUnit.SECONDS.sleep(20);
    System.out.println("Starting "+Instant.now());
    List<Pair> pairs =
        IntStream.range(0, 10).mapToObj(StateLess::sort).collect(Collectors.toList());

    System.out.println("Parallel "+pairs.stream().mapToLong(Pair::getParallel).summaryStatistics());
    System.out.println("NonParallel "+pairs.stream().mapToLong(Pair::getNonParallel).summaryStatistics());
  }


  public static List<Long> fillData(){
    Stream<Long> stream = Stream.generate(() -> ThreadLocalRandom.current().nextLong());
    return stream.parallel().limit(MAX_SIZE).collect(Collectors.toList());
  }

  public static Pair sort(int index){
    List<Long> input = fillData();
    Pair.PairBuilder builder = Pair.builder();
    Temporal start = Instant.now();
    input.parallelStream().unordered().mapToLong(Long::longValue).summaryStatistics();
    builder.parallel(ChronoUnit.MILLIS.between(start, Instant.now()));
    start = Instant.now();
    input.stream().unordered().mapToLong(Long::longValue).summaryStatistics();
    builder.nonParallel(ChronoUnit.MILLIS.between(start, Instant.now()));
    Pair pair = builder.build();
    System.out.println(index + " " + pair);
    return pair;
  }

  @Builder
  @Value
  static class Pair{
    long parallel;
    long nonParallel;
  }
}
