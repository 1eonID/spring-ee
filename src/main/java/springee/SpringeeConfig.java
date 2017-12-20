package springee;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springee.pet.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SpringeeConfig {

    @Bean
    public PetService petService(JpaPetRepository petRepository) {
        return new PetService(petRepository);
    }

    @Bean
    CommandLineRunner initDb(JpaPetRepository repository) {
        return args -> {
            List<Prescription> tomsPrescriptions = new ArrayList<>();
            tomsPrescriptions.add(new Prescription("paracetatamol", LocalDate.now(), 3));
            tomsPrescriptions.add(new Prescription("asperin", LocalDate.now(), 3));

            List<Prescription> jerrysPrescriptions = new ArrayList<>();

            jerrysPrescriptions.add(new Prescription("asperin", LocalDate.now(), 3));

            MedicalCard tomsCard = new MedicalCard(LocalDate.now(), "bla-bla");
            MedicalCard jerrysCard = new MedicalCard(LocalDate.now(), "foo-bar");

            repository.save(new Pet("Tom", "Cat", 3, LocalDate.now(), tomsCard, tomsPrescriptions));
            repository.save(new Pet("Jerry", "Mouse", 1, LocalDate.now(), jerrysCard, jerrysPrescriptions));
        };
    }
}
