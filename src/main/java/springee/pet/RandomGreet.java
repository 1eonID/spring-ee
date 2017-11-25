package springee.pet;

import org.springframework.stereotype.Component;
import java.util.Random;

@Component
public class RandomGreet {

  private static final String[] GREETS = {
      "hello world",
      "hola world",
      "bonjour world"
  };

  String getRandomGreet() {
    return GREETS[new Random().nextInt(3)];
  }
}
