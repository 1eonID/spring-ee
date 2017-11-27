package springee.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class DoctorController {

    private Map<Integer, Doctor> doctorMap = new HashMap<Integer, Doctor>() {{
        put(0, new Doctor(0, "Robert Martin", "Psychologist"));
        put(1, new Doctor(1, "Josh Long", "Surgeon"));
        put(2, new Doctor(2, "Joshua Bloch", "Ophthalmologist"));
    }};

    @GetMapping(value = "/doctors")
    public List<Doctor> getDoctors (@RequestParam Optional<String> specialization) {

        return null; //TODO
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

        return null;
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<Void> updateDoctor(@PathVariable Integer id,
                                             @RequestBody Doctor doctor) {

        doctorMap.put(id, doctor);

        return null;
    }

    @DeleteMapping("/doctors/{id}")
    //@ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteDoctor(@PathVariable Integer id) {

        if (doctorMap.containsKey(id)) {
            doctorMap.remove(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
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