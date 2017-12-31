package chapter1.parallel;

import java.util.stream.Stream;

public class Example1 {

  public static void main(String[] args) {
    System.out.println("java.util.concurrent.ForkJoinPool.common.parallelism" +
        System.getProperty("java.util.concurrent.ForkJoinPool.common.parallelism"));
    System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism","1");

    Stream.iterate(1, i -> i +1)
        .parallel()
        .limit(10)
        .forEach(i  -> System.out.println(Thread.currentThread().getName() + " "+i));
  }
}
