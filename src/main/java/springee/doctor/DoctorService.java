package springee.doctor;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import springee.SpringeeConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DoctorService {

    private final JpaDoctorRepository doctorRepository;
    private final SpringeeConfig springeeConfig;

    public List<Doctor> getDoctorsUsingSingleJpaMethods(Optional<String> name, Optional<List<String>> specialization) {
        return doctorRepository.findNullableByNameAndSpecialization(name.orElse(null), specialization.orElse(null));
    }

    public List<String> getSpecializations() {
      return springeeConfig.getSpecializations();
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
