package springee.doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaDoctorRepository extends JpaRepository<Doctor, Integer> {

  Optional<Doctor> findById(Integer id);

  //List<String> getSpecializations();

  List<Doctor> findBySpecialization (String specialization);

  List<Doctor> findByName(String name);

  @Query("SELECT doctor FROM Doctor AS doctor " +
         "WHERE (doctor.name = :name OR :name IS NULL) " +
         " AND (doctor.specialization = :specialization OR :specialization IS NULL) ")
  List<Doctor> findNullebleByNameAndSpecialization(@Param("name") String name,
                                           @Param("specialization")String specialization);
}
