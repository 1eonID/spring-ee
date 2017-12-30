package springee;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springee.pet.*;
import springee.store.Medicine;
import springee.store.MedicineRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class SpringeeConfig {

    @Bean
    @Profile("prod")
    CommandLineRunner initDb(JpaPetRepository repository) {
        return args -> {
            List<Prescription> tomsPrescriptions = new ArrayList<>();
            tomsPrescriptions.add(new Prescription("paracetamol", LocalDate.now(), 3, MedicineType.PERORAL));
            tomsPrescriptions.add(new Prescription("asperin", LocalDate.now(), 3, MedicineType.PERORAL));

            List<Prescription> jerrysPrescriptions = new ArrayList<>();

            jerrysPrescriptions.add(new Prescription("asperin", LocalDate.now(), 3, MedicineType.PERORAL));

            MedicalCard tomsCard = new MedicalCard(LocalDate.now(), "bla-bla");
            MedicalCard jerrysCard = new MedicalCard(LocalDate.now(), "foo-bar");

            repository.save(new Pet("Tom", "Cat", 3, LocalDate.now(), tomsCard, tomsPrescriptions));
            repository.save(new Pet("Jerry", "Mouse", 1, LocalDate.now(), jerrysCard, jerrysPrescriptions));
        };
    }

    @Bean
    @Profile("prod")
    CommandLineRunner initMedicineStore(MedicineRepository medicineRepository) {
        return args -> {
          medicineRepository.save(new Medicine("Brilliant green", 1));
          medicineRepository.save(new Medicine("asperin", 2));
          medicineRepository.save(new Medicine("paracetamol", 3));
        };
    }
}
