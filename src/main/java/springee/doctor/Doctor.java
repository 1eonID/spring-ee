package springee.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Component
@ConfigurationProperties("doctor")
public class Doctor {
    private UUID id;
    private String name;
    private String specialization;
}
