package springee.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    public Optional<Schedule> getById(Integer id) {
        return scheduleRepository.findById(id);
    }

    @Transactional
    public Set<Schedule> getScheduleByDoctorId(Integer id) {
        return scheduleRepository.findScheduleByDoctorId(id);
    }

    public Schedule save(Schedule Schedule) {
        return scheduleRepository.save(Schedule);
    }

    public void update(Integer id, Schedule schedule) {
        Schedule updSchedule = scheduleRepository.getOne(id);
        if(!(updSchedule == null)){
            updSchedule.setDate(schedule.getDate());
            updSchedule.setTime(schedule.getTime());
            updSchedule.setPetId(schedule.getPetId());
            updSchedule.setDoctorId(schedule.getDoctorId());
            scheduleRepository.saveAndFlush(updSchedule);
        }
    }

    public Boolean exists(Integer id) {
        return scheduleRepository.exists(id);
    }

    public Optional<Schedule> delete(Integer id) {
        Optional<Schedule> foundSchedule = scheduleRepository.findById(id);
        foundSchedule.ifPresent(Schedule -> scheduleRepository.delete(Schedule.getId()));
        return foundSchedule;
    }
}
