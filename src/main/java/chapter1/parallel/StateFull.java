package chapter1.parallel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalAmount;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class StateFull {

  public static final int MAX_SIZE = 10_000_000;

  public static void main(String[] args) {
    Temporal start = Instant.now();
    parallel_1();
    System.out.println("parallel_1 " + ChronoUnit.MILLIS.between(start, Instant.now()));

    start = Instant.now();
    non_parallel_1();
    System.out.println("non_parallel_1 " + ChronoUnit.MILLIS.between(start, Instant.now()));

    start = Instant.now();
    parallel_2();
    System.out.println("parallel_2 " + ChronoUnit.MILLIS.between(start, Instant.now()));

    start = Instant.now();
    non_parallel_2();
    System.out.println("non_parallel_2 " + ChronoUnit.MILLIS.between(start, Instant.now()));
  }



  public static void parallel_1(){
    Stream<Long> stream = Stream.generate(() -> ThreadLocalRandom.current().nextLong());
    stream.parallel().limit(MAX_SIZE).collect(Collectors.toList());
  }

  public static void non_parallel_1(){
    Stream<Long> stream = Stream.generate(() -> ThreadLocalRandom.current().nextLong());
    stream.limit(MAX_SIZE).collect(Collectors.toList());
  }

  public static void parallel_2(){
    LongStream stream = ThreadLocalRandom.current().longs(MAX_SIZE);
    stream.parallel().mapToObj(value -> value).collect(Collectors.toList());
  }

  public static void non_parallel_2(){
    LongStream stream = ThreadLocalRandom.current().longs(MAX_SIZE);
    stream.mapToObj(value -> value).collect(Collectors.toList());
  }
}
