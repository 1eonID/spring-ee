package springee.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.Set;

public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {
    Optional<Schedule> findById(Integer id);

    @Query("SELECT schedule FROM Schedule AS schedule " +
            "WHERE schedule.doctorId = :id ORDER BY schedule.time")
    Set<Schedule> findScheduleByDoctorId(@Param("id") Integer id);
}
