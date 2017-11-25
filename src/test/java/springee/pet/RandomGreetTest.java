package springee.pet;

import org.junit.Assert;
import org.junit.Test;

import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomGreetTest {

  @Test
  public void greetingRandomization() {
    RandomGreet randomGreet = new RandomGreet();
    int maxDelta = 10;
    int poolSize = 1000;
    Stream.generate(randomGreet::getRandomGreet)
        .limit(poolSize)
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()))
        .values().stream()
        .forEach(v -> Assert.assertTrue(calculateDelta(v, poolSize) <= maxDelta));
  }

  private double calculateDelta(Long actual, Integer poolSize) {
    double expected = poolSize / 3;
    return (Math.abs(actual - expected) * 100.0) / expected;
  }
}
