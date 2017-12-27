package chapter1.streamOfNumbers;

import chapter1.Spliterator.PersonSpliterator;
import chapter1.Spliterator.model.Person;
import chapter1.Utility;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class AverageOfPerson {

  private static final String FILE_NAME = "files/people.txt";

  public static void main(String[] args) {
    PersonSpliterator personSlipterator = new PersonSpliterator(
        Utility.getStreamFromFile(FILE_NAME).spliterator());
    Stream<Person> personStream = StreamSupport.stream(personSlipterator, false);

    System.out.println(personStream.mapToInt(Person::getAge).max());
  }
}
