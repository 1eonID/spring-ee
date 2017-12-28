package springee.doctor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> specialization;
    @OneToOne(cascade = CascadeType.ALL)
    private Schedule schedule;

    public Doctor(String name, List<String> specialization, Schedule schedule) {
        this.name = name;
        this.specialization = specialization;
        this.schedule = schedule;
    }
}
