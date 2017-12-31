package chapter1.optional;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class NewMath {

  public static void main(String[] args) {
    /*System.out.println("sqrt(-2)" + sqrt(-2d));
    System.out.println("sqrt(0)" + sqrt(0d));
    System.out.println("sqrt(2)" + sqrt(2d));
    System.out.println("sqrt(null)" + sqrt(null));


    System.out.println("inverse(-2)" + inverse(-2d));
    System.out.println("inverse(0)" + inverse(0d));
    System.out.println("inverse(2)" + inverse(2d));
    System.out.println("inverse(null)" + inverse(null));*/

    List<Double> doubles = Arrays.asList(new Double[]{0d, null, 200d, -500d, 773d, 1d});
    System.out.println(doubles.stream()
        .map(Optional::ofNullable)
        .map(NewMath::sqrt)
        .map(NewMath::inverse)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList()));

    Function<Double, Stream<Double>> function = d -> sqrt(d)
        .flatMap(NewMath::inverse)
        .map(Stream::of)
        .orElseGet(Stream::empty);

    System.out.println(doubles.stream()
        .filter(Objects::nonNull)
        .flatMap(function)
        .collect(Collectors.toList()));

    System.out.println(ThreadLocalRandom.current()
        .doubles(10_000)
        .boxed()
        .parallel()
        .map(Optional::of)
        .map(NewMath::sqrt)
        .map(NewMath::inverse)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList()).size());
  }

  public static Optional<Double> sqrt(Double d) {
    return d > 0d ? Optional.of(Math.sqrt(d)) : Optional.empty();
  }

  public static Optional<Double> inverse(Double d) {
    return d != 0d ? Optional.of(1d / d) : Optional.empty();
  }


  public static Optional<Double> sqrt(Optional<Double> d) {
    return d.filter(d1 -> d1 > 0d).map(Math::sqrt);
  }

  public static Optional<Double> inverse(Optional<Double> d) {
    return d.filter(d1 -> d1 != 0d).map(d1 -> 1d / d1);
  }
}
