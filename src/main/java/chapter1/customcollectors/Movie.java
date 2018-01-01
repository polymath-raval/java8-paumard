package chapter1.customcollectors;

import java.util.Set;
import lombok.Builder;
import lombok.Value;

@Value(staticConstructor = "of")
@Builder
public class Movie {
  String title;
  int releaseYear;
  Set<Actor> actors;
}
