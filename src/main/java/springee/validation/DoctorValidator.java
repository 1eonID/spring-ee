package springee.validation;

import lombok.Data;
import org.springframework.stereotype.Component;
import springee.doctor.DoctorService;

@Data
@Component
public class DoctorValidator implements Validator {
    private final DoctorService doctorService;

    @Override
    public boolean exists(Integer id) {
        return doctorService.exists(id);
    }
}
