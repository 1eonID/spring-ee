package springee.schedule;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springee.pet.dto.PetIdInputDto;
import springee.validation.DoctorValidator;
import springee.validation.PetValidator;
import springee.validation.ScheduleValidator;

import java.net.URI;
import java.time.LocalDate;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

@RestController
@AllArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final DoctorValidator doctorValidator;
    private final ScheduleValidator scheduleValidator;
    private final PetValidator petValidator;

    @GetMapping(value = "/doctors/{id}/schedule/{date}")
    public ResponseEntity<?> getSchedule(@PathVariable Integer id,
                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (!doctorValidator.exists(id)) {
            return ResponseEntity.notFound().build();
        } else if (date.isBefore(LocalDate.now())) {
            return ResponseEntity.badRequest().build();
        }
        Set<Schedule> set = scheduleService.getScheduleByDoctorId(id);
        SortedMap<Integer, Integer> sortedSchedule = new TreeMap<>();
        for (Schedule schedule: set) {
            if(schedule.getDate().equals(date)) {
                sortedSchedule.put(schedule.getTime(), schedule.getPetId());
            }
        }
        if (sortedSchedule.isEmpty()) {return ResponseEntity.ok("In this day, all time of reception free.");}
        return ResponseEntity.ok(sortedSchedule);
    }

    @PostMapping("/doctors/{id}/schedule/{date}/{recordingTime}")
    public ResponseEntity<Void> recordingToDoctor(@PathVariable Integer id,
                                                  @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
                                                  @PathVariable Integer recordingTime,
                                                  @RequestBody PetIdInputDto dto) {
        if (!doctorValidator.exists(id) || !petValidator.exists(dto.getPetId())) {
            return ResponseEntity.notFound().build();
        } else if ((date.isBefore(LocalDate.now())) || (!scheduleValidator.recordingTimeIsValid(recordingTime))) {
            return ResponseEntity.badRequest().build();
        }
        Set<Schedule> set = scheduleService.getScheduleByDoctorId(id);
        Schedule tempSchedule = new Schedule(date, recordingTime, dto.getPetId(), id);
        for (Schedule schedule: set) {
            if (schedule.getDate().equals(tempSchedule.getDate()) && schedule.getTime().equals(tempSchedule.getTime())) {
                return ResponseEntity.badRequest().build();
            }
        }
        scheduleService.save(tempSchedule);
        return ResponseEntity.created(URI.create("doctors/" + id + "/schedule/" + date +
                "/" + recordingTime + "/" + dto.getPetId())).build();
    }
}
