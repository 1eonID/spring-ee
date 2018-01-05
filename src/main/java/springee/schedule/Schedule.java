package springee.schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Schedule{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate date;
    private Integer time;
    private Integer petId;
    private Integer doctorId;


    public Schedule(LocalDate date, Integer time, Integer petId, Integer doctorId) {
        this.date = date;
        this.time = time;
        this.petId = petId;
        this.doctorId = doctorId;
    }
}
