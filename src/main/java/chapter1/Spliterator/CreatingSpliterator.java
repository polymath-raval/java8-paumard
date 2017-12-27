package chapter1.Spliterator;

import static java.util.function.Function.identity;

import chapter1.Spliterator.model.Person;
import chapter1.Utility;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class CreatingSpliterator {

  private static final String FILE_NAME = "files/people.txt";

  public static void main(String[] args) {
    CreatingSpliterator creatingSpliterator = new CreatingSpliterator();
    PersonSpliterator personSlipterator = new PersonSpliterator(
        Utility.getStreamFromFile(FILE_NAME).spliterator());
    Stream<Person> personStream = StreamSupport.stream(personSlipterator, false);
    Stream.of(personStream).forEach(System.out::println);
    System.out.println(
        Stream.of(personStream).flatMap(identity()).min(Comparator.comparing(Person::getAge))
            .get());
    System.out.println(
        Stream.of(personStream).flatMap(identity()).max(Comparator.comparing(Person::getAge))
            .get());
    Stream.of(personStream).flatMap(identity()).sorted(Comparator.comparing(Person::getName))
        .map(Person::toString).forEach(System.out::println);
    Stream.of(personStream).flatMap(identity()).sorted(Comparator.comparing(Person::getAge))
        .map(Person::toString)
        .forEach(System.out::println);
  }


}
