package springee;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springee.pet.PetRepository;
import springee.pet.PetService;

@Configuration
public class SpringeeConfig {

    @Bean
    public PetService petService(PetRepository petRepository) {
        return new PetService(petRepository);
    }
}
