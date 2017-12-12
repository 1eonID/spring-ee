package springee.doctor;

import lombok.Getter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Getter
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("doctor")
public class DoctorConfig {

    private List<String> specializations = new ArrayList<>();

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

        };
    }
}
