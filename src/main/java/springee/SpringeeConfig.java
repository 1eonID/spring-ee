package springee;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springee.pet.JpaPetRepository;
import springee.pet.Pet;
import springee.pet.PetService;

@Configuration
public class SpringeeConfig {

    @Bean
    public PetService petService(JpaPetRepository petRepository) {
        return new PetService(petRepository);
    }

    @Bean
    CommandLineRunner initDb(JpaPetRepository repository) {
        return args -> {
            if (repository.findAll().isEmpty()) {
                return;
            }
            repository.save(new Pet( "Tom", "Cat", 3));
            repository.save(new Pet( "Jerry", "Mouse", 1));
        };
    }
}
