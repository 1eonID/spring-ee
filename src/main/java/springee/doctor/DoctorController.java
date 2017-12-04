package springee.doctor;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;

@RestController
@AllArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;

    @GetMapping(value = "/doctors")
    public List<Doctor> getDoctors (@RequestParam Optional<String> specialization,
                                    @RequestParam Optional<String> name) {

        return doctorService.getDoctors(specialization, name);
    }

    @GetMapping(value = "/doctors/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable UUID id) {

        Optional<Doctor> findDoctor = doctorService.getById(id);

        return findDoctor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/doctors")
    public ResponseEntity<Void> createDoctor(@RequestBody Doctor doctor) {

        if (doctor.getId() == null) {
            Doctor created = doctorService.create(doctor);
            return ResponseEntity.created(URI.create("doctors/" + created.getId())).build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<Void> updateDoctor(@PathVariable UUID id,
                                             @RequestBody Doctor doctor) {

        Optional<Doctor> isUpdated = doctorService.update(id, doctor);

        return isUpdated.map(ResponseEntity::noContent)
                .orElse(ResponseEntity.notFound().build());

//        if (!doctorMap.containsKey(id)) {
//            return ResponseEntity.notFound().build();
//        } else if (!doctor.getId().equals(id)) {
//            return ResponseEntity.badRequest().build();
//        }
//        doctorMap.put(id, doctor);
//        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id) {

        Optional<Doctor> isDeleted = doctorService.delete(id);

        return isDeleted.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}