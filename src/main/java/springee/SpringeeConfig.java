package springee;

import lombok.Data;
import org.springframework.boot.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springee.doctor.*;
import springee.pet.*;
import springee.schedule.Schedule;
import springee.schedule.ScheduleRepository;

import java.time.LocalDate;
import java.util.*;

@Configuration
@ConfigurationProperties("doctor")
@Data
public class SpringeeConfig {

  private List<String> specializations;

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

      Doctor doctor1 = new Doctor("Robert Martin", martinSpec);
      Doctor doctor2 = new Doctor("Josh Long", longSpec);
      Doctor doctor3 = new Doctor("Joshua Bloch", blochSpec);

      repository.save(doctor1);
      repository.save(doctor2);
      repository.save(doctor3);

    };
  }

  @Bean
  CommandLineRunner initSchedule(ScheduleRepository repository) {
    return args -> {
      Schedule schedule1 = new Schedule(LocalDate.now(), 8, 1, 1);
      Schedule schedule2 = new Schedule(LocalDate.now(), 9, 2, 1);

      repository.save(schedule1);
      repository.save(schedule2);
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
