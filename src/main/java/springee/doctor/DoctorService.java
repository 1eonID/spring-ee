package springee.doctor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    public List<Doctor> getDoctors(Optional<String> specialization, Optional<String> name) {
        Predicate<Doctor> specFilter = specialization.map(this::filterBySpec)
                .orElse(doctor -> true);

        Predicate<Doctor> nameFilter = name.map(this::filterByFirstLetterOfName)
                .orElse(doctor -> true);

        Predicate<Doctor> complexFilter = nameFilter.and(specFilter);

        return doctorRepository.findAll().stream()
                .filter(complexFilter)
                .collect(Collectors.toList());
    }

    private Predicate<Doctor> filterBySpec(String specialization) {
        return doctor -> doctor.getSpecialization().equals(specialization);
    }

    private Predicate<Doctor> filterByFirstLetterOfName(String name) {
        return doctor -> doctor.getName().startsWith(name);
    }

    public List<String> getSpecieList() {
        return doctorRepository.getSpecieList();
    }

    public Optional<Doctor> getById(UUID id) {
        return doctorRepository.findById(id);
    }

    public Doctor create(Doctor doctor) {
        return doctorRepository.create(doctor);
    }

    public ResponseEntity<Void> update(UUID id, Doctor doctor) {
        return doctorRepository.update(id, doctor);
    }

    public Optional<Doctor> delete(UUID id) {
        return doctorRepository.delete(id);
    }
}
