package chapter1.Spliterator;

import chapter1.Spliterator.model.Person;
import java.util.Spliterator;
import java.util.function.Consumer;

public class PersonSpliterator implements Spliterator<Person> {

  private Spliterator<String> baseSpliterator;

  public PersonSpliterator(Spliterator<String> spliterator) {
    this.baseSpliterator = spliterator;
  }

  @Override
  public boolean tryAdvance(Consumer<? super Person> action) {
    Person.PersonBuilder builder = Person.builder();
    if (baseSpliterator.tryAdvance(builder::name) &&
        baseSpliterator.tryAdvance(age -> builder.age(Integer.parseInt(age))) &&
        baseSpliterator.tryAdvance(builder::city)) {
      action.accept(builder.build());
      return true;
    }
    return false;
  }

  @Override
  public Spliterator<Person> trySplit() {
    return null;
  }

  @Override
  public long estimateSize() {
    return baseSpliterator.estimateSize() / 3;
  }

  @Override
  public int characteristics() {
    return baseSpliterator.characteristics();
  }
}
