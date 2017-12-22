package springee.doctor;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;
import java.util.*;

@RestController
@AllArgsConstructor
public class DoctorController {

    private final DoctorService doctorService;
    private Schedule schedule;

    @GetMapping(value = "/doctors")
    public List<Doctor> getDoctors (@RequestParam Optional<String> name,
                                    @RequestParam Optional<List<String>> specialization) {

        return doctorService.getDoctorsUsingSingleJpaMethods(name, specialization);
    }

    @GetMapping(value = "/doctors/specializations")
    public List<String> getSpecieList () {

        return doctorService.getSpecializations();
    }

    @GetMapping(value = "/doctors/{id}")
    public ResponseEntity<?> getDoctorById(@PathVariable Integer id) {

        Optional<Doctor> findDoctor = doctorService.getById(id);

        return findDoctor.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/doctors")
    public ResponseEntity<Void> createDoctor(@RequestBody Doctor doctor) {
        List<String> specieList = doctorService.getSpecializations();

        if (doctor.getId() == null && specieList.contains(doctor.getSpecialization())) {
            Doctor created = doctorService.save(doctor);
            return ResponseEntity.created(URI.create("doctors/" + created.getId())).build();
        }

        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/doctors/{id}/schedule/{date}/{recordingTime}")
    public ResponseEntity<Void> recordingToDoctor(@PathVariable Integer id,
                                                  @PathVariable LocalDate date,
                                                  @PathVariable Integer recordingTime,
                                                  @RequestBody Integer petId) {
        if (!doctorService.exists(id)) {
            return ResponseEntity.notFound().build();
        } else if (date.isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().build();
        }
        Optional<Doctor> doctor = doctorService.getById(id);
        schedule = doctor.get().getSchedule();
        //TODO: schedule update
        return ResponseEntity.created(URI.create("doctors/" + id + "/schedule/" + date + "/" + petId)).build();
    }

    @PutMapping("/doctors/{id}")
    public ResponseEntity<Void> updateDoctor(@PathVariable Integer id,
                                             @RequestBody Doctor doctor) {

        List<String> specieList = doctorService.getSpecializations();

        if (!doctorService.exists(id)) {
            return ResponseEntity.notFound().build();
        } else if (!specieList.contains(doctor.getSpecialization())) {
            return ResponseEntity.badRequest().build();
        }
        doctorService.update(id, doctor);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/doctors/{id}")
    public ResponseEntity<?> deleteDoctor(@PathVariable Integer id) {

        Optional<Doctor> isDeleted = doctorService.delete(id);

        return isDeleted.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

}