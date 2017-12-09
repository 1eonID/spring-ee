package springee.pet;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class PetService {

    private final JpaPetRepository petRepository;

    public List<Pet> getPetsUsingSeparateJpaMethods(Optional<String> specie, Optional<Integer> age) {
        if (specie.isPresent() && age.isPresent()) {
            petRepository.findBySpecieAndAge(specie.get(), age.get());
        }
        if (specie.isPresent()) {
            return petRepository.findBySpecie(specie.get());
        }
        if (age.isPresent()) {
            return petRepository.findByAge(age.get());
        }
        return petRepository.findAll();
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

    public List<Pet> getPetsUsingSingleJpaMethods(Optional<String> specie, Optional<Integer> age) {
        return petRepository.findNullebleBySpecieAndAge(specie.orElse(null), age.orElse(null));
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
}
