package springee.validation;

import lombok.Data;
import org.springframework.stereotype.Component;
import springee.schedule.ScheduleService;

@Data
@Component
public class ScheduleValidator implements Validator {
    private final ScheduleService scheduleService;

    @Override
    public boolean exists(Integer id) {
        return scheduleService.exists(id);
    }

    public boolean recordingTimeIsValid(Integer time) {
        return ((time >= 8) && (time <= 15));
    }
}
