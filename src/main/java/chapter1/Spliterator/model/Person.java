package chapter1.Spliterator.model;

import lombok.Builder;
import lombok.Value;

@Value(staticConstructor = "of")
@Builder
public class Person {

  String name;
  int age;
  String city;
}
