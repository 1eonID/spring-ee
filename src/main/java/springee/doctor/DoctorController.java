package springee.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.Doc;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@RestController
public class DoctorController {

    private Map<Integer, Doctor> doctorMap = new HashMap<Integer, Doctor>() {{
        put(0, new Doctor(0, "Robert Martin", "psychologist"));
        put(1, new Doctor(1, "Josh Long", "surgeon"));
        put(2, new Doctor(2, "Joshua Bloch", "ophthalmologist"));
    }};

    private volatile AtomicInteger idCounter = new AtomicInteger(3);


    @GetMapping(value = "/doctors")
    public List<Doctor> getDoctors (@RequestParam Optional<String> specialization,
                                    @RequestParam Optional<String> name) {

        Predicate<Doctor> specFilter = specialization.map(this::filterBySpec)
            .orElse(doctor -> true);

        Predicate<Doctor> nameFilter = name.map(this::filterByFirstLetterOfName)
            .orElse(pet -> true);

        Predicate<Doctor> complexFilter = nameFilter.and(specFilter);

        return doctorMap.values().stream()
            .filter(complexFilter)
            .collect(Collectors.toList());
    }

    @GetMapping(value = "/doctors/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Integer id) {

        if (doctorMap.containsKey(id)) {
            return ResponseEntity.ok(doctorMap.get(id));
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/doctors")
    public ResponseEntity<Void> createDoctor(@RequestBody Doctor doctor) {

        if (doctor.getId() == null) {
            doctor.setId(idCounter.get());
            doctorMap.put(idCounter.get(), doctor);
            return ResponseEntity.created(URI.create("doctors/" + idCounter.getAndIncrement())).build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<Void> updateDoctor(@PathVariable Integer id,
                                             @RequestBody Doctor doctor) {
        if (!doctorMap.containsKey(id)) {
            return ResponseEntity.notFound().build();
        } else if (doctor.getId() != id) {
            return ResponseEntity.badRequest().build();
        }
        doctorMap.put(id, doctor);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<Void> deleteDoctor(@PathVariable Integer id) {

        if (doctorMap.containsKey(id)) {
            doctorMap.remove(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    private Predicate<Doctor> filterBySpec(String specialization) {
        return doctor -> doctor.getSpecialization().equals(specialization);
    }

    private Predicate<Doctor> filterByFirstLetterOfName(String name) {
        return doctor -> doctor.getName().startsWith(name);
    }

}

@AllArgsConstructor
@NoArgsConstructor
@Data
class Doctor {
    private Integer id;
    private String name;
    private String specialization;
}