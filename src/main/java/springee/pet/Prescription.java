package springee.pet;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Entity
public class Prescription {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String description;
    private LocalDate start;
    private Integer timesPerDay;

    public Prescription(String description, LocalDate start, Integer timesPerDay) {
        this.description = description;
        this.start = start;
        this.timesPerDay = timesPerDay;
    }
}