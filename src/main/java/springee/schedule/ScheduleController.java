package springee.schedule;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springee.doctor.DoctorService;

import java.net.URI;
import java.time.LocalDate;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

@RestController
@AllArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final DoctorService doctorService;

    @GetMapping(value = "/doctors/{id}/schedule/{date}")
    public ResponseEntity<?> getSchedule(@PathVariable Integer id,
                                         @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (!doctorService.exists(id)) {
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
                                                  @RequestParam("petId") Integer petId) {
        if (!doctorService.exists(id)) {
            return ResponseEntity.notFound().build();
        } else if (date.isBefore(LocalDate.now()) || (recordingTime <= 7 && recordingTime >= 16)) {
            return ResponseEntity.badRequest().build();
        }
        Set<Schedule> set = scheduleService.getScheduleByDoctorId(id);
        Schedule tempSchedule = new Schedule(date, recordingTime, petId, id);
        for (Schedule schedule: set) {
            if (schedule.getDate().equals(tempSchedule.getDate()) && schedule.getTime().equals(tempSchedule.getTime())) {
                return ResponseEntity.badRequest().build();
            }
        }
        scheduleService.save(tempSchedule);
        return ResponseEntity.created(URI.create("doctors/" + id + "/schedule/" + date + "/" + petId)).build();
    }
}
