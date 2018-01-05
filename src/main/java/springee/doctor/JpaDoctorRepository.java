package springee.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import springee.schedule.Schedule;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface JpaDoctorRepository extends JpaRepository<Doctor, Integer> {

  Optional<Doctor> findById(Integer id);

  @Query("SELECT DISTINCT doctor FROM Doctor AS doctor JOIN doctor.specialization s \n" +
          " WHERE (s IN :specialization OR :specialization IS NULL) \n" +
          " AND (LOWER(doctor.name) = LOWER(cast(:name as text)) OR :name IS NULL) \n" +
          " ORDER BY doctor.id ")
  List<Doctor> findNullableByNameAndSpecialization(@Param("name") String name,
                                           @Param("specialization")List<String> specialization);
}
