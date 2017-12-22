package springee;

import lombok.Data;
import org.springframework.boot.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springee.doctor.Doctor;
import springee.doctor.DoctorService;
import springee.doctor.JpaDoctorRepository;
import springee.doctor.Schedule;
import springee.pet.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Configuration
@ConfigurationProperties("doctor")
@Data
public class SpringeeConfig {

  private List<String> specializations;

  @Bean
  DoctorService doctorService(JpaDoctorRepository doctorRepository) {
    return new DoctorService(doctorRepository);
  }

  @Bean
  PetService petService(JpaPetRepository petRepository) {
    return new PetService(petRepository);
  }

  @Bean
  CommandLineRunner initDoctors(JpaDoctorRepository repository) {
    return args -> {
      if (!repository.findAll().isEmpty()) {
        return;
      }

      List<String> martinSpec = new ArrayList<>();
      martinSpec.add("psychologist");
      martinSpec.add("neurologist");

      List<String> longSpec = new ArrayList<>();
      longSpec.add("cardiologist");
      longSpec.add("surgeon");

      List<String> blochSpec = new ArrayList<>();
      blochSpec.add("ophthalmologist");
      blochSpec.add("urologist");

      Schedule martinSched = new Schedule();
      Schedule longSched = new Schedule();
      Schedule blochSched = new Schedule();


      repository.save(new Doctor("Robert Martin", martinSpec, martinSched));
      repository.save(new Doctor("Josh Long", longSpec, longSched));
      repository.save(new Doctor("Joshua Bloch", blochSpec, blochSched));

    };
  }

  @Bean
  CommandLineRunner initPets(JpaPetRepository repository) {
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
