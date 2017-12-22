package springee.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaDoctorRepository extends JpaRepository<Doctor, Integer> {

  Optional<Doctor> findById(Integer id);

  //JOIN doctor.specialization s WHERE s IN :specialization

  @Query("SELECT doctor FROM Doctor AS doctor " +
         "WHERE ((LOWER(doctor.name) = LOWER(cast :name as text)) OR :name IS NULL) " +
         " AND (doctor.specialization s WHERE s IN :specialization OR :specialization IS NULL) ")
  List<Doctor> findNullableByNameAndSpecialization(@Param("name") String name,
                                           @Param("specialization")List<String> specialization);
}
