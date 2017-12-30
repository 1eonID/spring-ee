package springee.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate date;
    @ElementCollection(fetch = FetchType.EAGER)
    private Map<Integer, Integer> hourToPetId;

    public Schedule(LocalDate date, Map<Integer, Integer> hourToPetId) {
        this.date = date;
        this.hourToPetId = hourToPetId;
    }

}
