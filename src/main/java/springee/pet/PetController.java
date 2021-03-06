package springee.pet;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springee.pet.dto.PrescriptionInputDto;
import springee.util.ErrorBody;

import javax.validation.Valid;
import java.net.URI;
import java.util.*;

@RestController
@AllArgsConstructor
public class PetController {

  private final PetService petService;

  @GetMapping(value = "/greeting")
  public String helloWorld() {
    return "Hello World!";
  }

  @GetMapping(value = "/pets")
  public Page<Pet> getPets(@RequestParam Optional<String> specie,
                           @RequestParam Optional<Integer> age,
                           Pageable pageable
                           /*@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<LocalDate> birthDay*/) {

    return petService.getPetsUsingSeparateJpaMethods(specie, age, pageable/*, birthDay*/);
  }

  @GetMapping("/pets/{id}")
  public ResponseEntity<?> getPetById(@PathVariable Integer id) {

    Optional<Pet> mayBePet = petService.getById(id);

    return mayBePet.map(Object.class::cast)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.badRequest()
                    .body(new ErrorBody("There is no pet with ID = " + id)));
  }

  @PostMapping("/pets")
  public ResponseEntity<Void> createPet(@RequestBody Pet pet) {

    Pet saved = petService.save(pet);
    return ResponseEntity.created(URI.create("pets/" + saved.getId())).build();
  }
  @PutMapping("/pets/{id}")
  public void updatePet(@PathVariable Integer id,
                        @RequestBody Pet pet) {
    pet.setId(id);
    petService.save(pet);
  }

  @DeleteMapping("/pets/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void deletePet(@PathVariable Integer id) {

    petService.delete(id)
              .orElseThrow(NoSuchPetException::new);
  }

  @PostMapping("/pets/{id}/prescriptions")
  public void prescribe(@PathVariable Integer id,
                        @Valid @RequestBody PrescriptionInputDto dto) {
    petService.prescribe(id,
                          dto.getDescription(),
                          dto.getMedicineName(),
                          dto.getQuantity(),
                          dto.getTimesPerDay(),
                          dto.getMedicineType());
  }

  @ExceptionHandler(NoSuchMedicineException.class)
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  public void noSuchMedicineException(){}

//  @ExceptionHandler(MyException.class)
//  @ResponseStatus(HttpStatus.BAD_REQUEST)
//  public void exceptionHandler(MyException exception) {
//    log.error("error thtows");
//  }
}


