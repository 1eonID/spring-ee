package springee;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springee.doctor.Doctor;
import springee.doctor.DoctorService;
import springee.doctor.JpaDoctorRepository;

@Configuration
public class SpringeeConfig {

  @Bean
  DoctorService doctorService(JpaDoctorRepository doctorRepository) {
    return new DoctorService(doctorRepository);
  }

  @Bean
  CommandLineRunner initDb(JpaDoctorRepository repository) {
    return args -> {
      if (!repository.findAll().isEmpty()) {
        return;
      }
      repository.save(new Doctor("Robert Martin", "psychologist"));
      repository.save(new Doctor("Josh Long", "surgeon"));
      repository.save(new Doctor("Joshua Bloch", "ophthalmologist"));

//      repository.createSpecializationsList();
//      repository.insertInSpecializationsList("psychologist");
//      repository.insertInSpecializationsList("surgeon");
//      repository.insertInSpecializationsList("ophthalmologist");
    };
  }
}
