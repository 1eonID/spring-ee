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
@EnableConfigurationProperties
@ConfigurationProperties("doctor")
public class DoctorConfig {

    private List<String> specializations = new ArrayList<>();

}
