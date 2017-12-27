package chapter1;

import chapter1.model.Person;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.SneakyThrows;

public class CreatingSpliterator {

  private static final String FILE_NAME = "files/people.txt";

  public static void main(String[] args) {
    CreatingSpliterator creatingSpliterator = new CreatingSpliterator();
    PersonSpliterator personSlipterator = new PersonSpliterator(
        creatingSpliterator.getStreamFromFile().spliterator());
    Stream<Person> personStream = StreamSupport.stream(personSlipterator, false);
    //personStream.forEach(System.out::println);
    //System.out.println(personStream.min(Comparator.comparing(Person::getAge)).get());
    //System.out.println(personStream.max(Comparator.comparing(Person::getAge)).get());
    //personStream.sorted(Comparator.comparing(Person::getName)).map(Person::toString).forEach(System.out::println);
    personStream.sorted(Comparator.comparing(Person::getAge)).map(Person::toString)
        .forEach(System.out::println);
  }

  @SneakyThrows
  private Stream<String> getStreamFromFile() {
    this.getClass().getClassLoader();
    Path path = Paths.get(ClassLoader.getSystemResource(FILE_NAME).toURI());
    return Files.lines(path);
  }
}
