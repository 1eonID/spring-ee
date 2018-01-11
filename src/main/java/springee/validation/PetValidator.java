package springee.validation;

import lombok.Data;
import org.springframework.stereotype.Component;
import springee.pet.PetService;

@Data
@Component
public class PetValidator implements Validator {
    private final PetService petService;

    @Override
    public boolean exists(Integer id) {
        return petService.exists(id);
    }
}
