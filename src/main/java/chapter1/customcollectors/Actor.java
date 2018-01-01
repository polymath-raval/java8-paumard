package chapter1.customcollectors;

import lombok.Builder;
import lombok.Value;

@Value(staticConstructor = "of")
@Builder
public class Actor {
  String firstName, lastName;
}
