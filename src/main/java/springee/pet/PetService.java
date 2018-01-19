package springee.pet;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import springee.store.StoreService;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PetService {

    private final JpaPetRepository petRepository;
    private final StoreService storeService;

    public Page<Pet> getPetsUsingSeparateJpaMethods(Optional<String> specie, Optional<Integer> age, Pageable pageable) {
        if (specie.isPresent() && age.isPresent()) {
            petRepository.findBySpecieAndAge(specie.get(), age.get(), pageable);
        }
        if (specie.isPresent()) {
            return petRepository.findBySpecie(specie.get(), pageable);
        }
        if (age.isPresent()) {
            return petRepository.findByAge(age.get(), pageable);
        }
        return petRepository.findAll(pageable);
    }

    public List<Pet> getPetsUsingStreamFilters(Optional<String> specie, Optional<Integer> age) {
        Predicate<Pet> specieFilter = specie.map(this::filterBySpecie)
                .orElse(pet -> true);

        Predicate<Pet> ageFilter = age.map(this::filterByAge)
                .orElse(pet -> true);

        Predicate<Pet> complexFilter = ageFilter.and(specieFilter);

        return petRepository.findAll().stream()
                .filter(complexFilter)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<Pet> getPetsUsingSingleJpaMethods(Optional<String> specie, Optional<Integer> age/*, Optional<LocalDate> birthDay*/) {
        List<Pet> nullableBySpecieAndAge = petRepository.findNullableBySpecieAndAge(specie.orElse(null),
            age.orElse(null)/*, birthDay.orElse(null)*/);

        nullableBySpecieAndAge.forEach(pet -> System.out.println(pet.getPrescriptions()));

        return nullableBySpecieAndAge;
    }

    private Predicate<Pet> filterBySpecie(String specie) {
        return pet -> pet.getSpecie().equals(specie);
    }

    private Predicate<Pet> filterByAge(Integer age) {
        return pet -> pet.getAge().equals(age);
    }

    public Optional<Pet> getById(Integer id) {
        return petRepository.findById(id);
    }

    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }

    public Optional<Pet> delete(Integer id) {
        Optional<Pet> mayBePet = petRepository.findById(id);
        mayBePet.ifPresent(pet -> petRepository.delete(pet.getId()));
        return mayBePet;
    }

    @Transactional
    @Retryable(ObjectOptimisticLockingFailureException.class)
    public void prescribe(Integer petId,
                          String desctiption,
                          String medicineName,
                          Integer quantity,
                          Integer timesPerDay,
                          MedicineType medicineType) {
        Pet pet = petRepository.findById(petId).orElseThrow(NoSuchMedicineException::new);
        pet.getPrescriptions().add(new Prescription(desctiption, LocalDate.now(), timesPerDay, medicineType)); //LocalDate.now() for test
        petRepository.save(pet);

        storeService.decrement(medicineName, quantity);
    }
}
