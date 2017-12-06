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

    @GetMapping(value = "/doctors/specializations")
    public List<String> getSpecieList () {

        return doctorService.getSpecieList();
    }

    @GetMapping(value = "/doctors/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable UUID id) {

        Optional<Doctor> findDoctor = doctorService.getById(id);

        return findDoctor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/doctors")
    public ResponseEntity<Void> createDoctor(@RequestBody Doctor doctor) {
        List<String> specieList = doctorService.getSpecieList();

        if (doctor.getId() == null && specieList.contains(doctor.getSpecialization())) {
            Doctor created = doctorService.create(doctor);
            return ResponseEntity.created(URI.create("doctors/" + created.getId())).build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<Void> updateDoctor(@PathVariable UUID id,
                                             @RequestBody Doctor doctor) {

        return doctorService.update(id, doctor);
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable UUID id) {

        Optional<Doctor> isDeleted = doctorService.delete(id);

        return isDeleted.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}