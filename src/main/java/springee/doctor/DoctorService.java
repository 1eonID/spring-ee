package springee.doctor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class DoctorService {

    private final JpaDoctorRepository doctorRepository;

    public List<Doctor> getDoctorsUsingStreamFilters(Optional<String> specialization, Optional<String> name) {
        Predicate<Doctor> specFilter = specialization.map(this::filterBySpec)
                .orElse(doctor -> true);

        Predicate<Doctor> nameFilter = name.map(this::filterByFirstLetterOfName)
                .orElse(doctor -> true);

        Predicate<Doctor> complexFilter = nameFilter.and(specFilter);

        return doctorRepository.findAll().stream()
                .filter(complexFilter)
                .collect(Collectors.toList());
    }

    public List<Doctor> getDoctorsUsingSingleJpaMethods(Optional<String> name, Optional<String> specialization) {
        return doctorRepository.findNullebleByNameAndSpecialization(name.orElse(null), specialization.orElse(null));
    }

    private Predicate<Doctor> filterBySpec(String specialization) {
        return doctor -> doctor.getSpecialization().equals(specialization);
    }

    private Predicate<Doctor> filterByFirstLetterOfName(String name) {
        return doctor -> doctor.getName().startsWith(name);
    }

    public List<String> getSpecializations() {
      List<String> list = new ArrayList<>();
      list.add("psychologist");
      list.add("surgeon");
      list.add("ophthalmologist");
      return list;
    }

    public Optional<Doctor> getById(Integer id) {
        return doctorRepository.findById(id);
    }

    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public void update(Integer id, Doctor doctor) {
        Doctor updDoctor = doctorRepository.getOne(id);
        if(!(updDoctor == null)){
            updDoctor.setName(doctor.getName());
            updDoctor.setSpecialization(doctor.getSpecialization());
            doctorRepository.saveAndFlush(updDoctor);
        }
    }

    public Boolean exists(Integer id) {
        return doctorRepository.exists(id);
    }

    public Optional<Doctor> delete(Integer id) {
        Optional<Doctor> foundDoctor = doctorRepository.findById(id);
        foundDoctor.ifPresent(doctor -> doctorRepository.delete(doctor.getId()));
        return foundDoctor;
    }
}
