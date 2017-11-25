package springee.pet;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PetController {

  @GetMapping(value = "/greeting")
  public String helloWorld() {
    return "Hello World!";
  }
}


