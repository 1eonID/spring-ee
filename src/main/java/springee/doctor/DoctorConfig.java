package springee.doctor;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties("doctor")
public class DoctorConfig {

    private List<String> specializations = new ArrayList<>();

    public List<String> getSpecializations() {
        return specializations;
    }
}
