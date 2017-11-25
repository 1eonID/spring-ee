package springee.pet;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
public class PetController {

  private Map<Integer, Pet> pets = new HashMap<Integer, Pet>() {{
    put(0, new Pet("Tom", "Cat", 3));
    put(1, new Pet("Jerry", "Mouse", 1));
  }};

  Integer counter = 2;

  @GetMapping(value = "/greeting")
  public String helloWorld() {
    return "Hello World!";
  }

  @GetMapping(value = "/pets")
  public List<Pet> getPets(@RequestParam Optional<String> specie,
                           @RequestParam Optional<Integer> age) {

    Predicate<Pet> specieFilter = specie.map(this::filterBySpecie)
            .orElse(pet -> true);

    Predicate<Pet> ageFilter = age.map(this::filterByAge)
            .orElse(pet -> true);

    Predicate<Pet> complexFilter = ageFilter.and(specieFilter);

    return pets.values().stream()
            .filter(complexFilter)
            .collect(Collectors.toList());
    }

    @GetMapping("/pets/{id}")
    public ResponseEntity<?> getPetById(@PathVariable Integer id) {
      if(id >= pets.size()) {
        return ResponseEntity.badRequest()
                .body(new ErrorBody("There is no pet with ID = " + id));
      }

      return  ResponseEntity.ok(pets.get(id));
    }

    @PostMapping("/pets")
    public ResponseEntity<Void> createPet(@RequestBody Pet pet) {
      pets.put(++counter, pet);
      return ResponseEntity.created(URI.create("pets/" + counter)).build();
    }

    @PutMapping("/pets/{id}")
    public void updatePet(@PathVariable Integer id,
                          @RequestBody Pet pet) {
      pets.put(id, pet);
    }

    @DeleteMapping("/pets/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePet(@PathVariable Integer id) {
//      if(!pets.containsKey(id)) {
//        ResponseEntity.badRequest();
//      }
      pets.remove(id);
    }

  private Predicate<Pet> filterBySpecie(String specie) {
    return pet -> pet.getSpecie().equals(specie);
  }

  private Predicate<Pet> filterByAge(Integer age) {
    return pet -> pet.getAge().equals(age);
  }
}

@Data
@AllArgsConstructor
class ErrorBody {
  private final Integer code = 400;
  private String message;
}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Pet {
  private String name;
  private String specie;
  private Integer age;
}


