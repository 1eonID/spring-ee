package springee.pet;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaPetRepository extends JpaRepository<Pet, Integer> {
    Optional<Pet> findById(Integer id);

    Page<Pet> findBySpecieAndAge(String specie, Integer age, Pageable pageable);

    Page<Pet> findBySpecie(String specie, Pageable pageable);

    Page<Pet> findByAge(Integer age, Pageable pageable);

    @Query("SELECT pet FROM Pet AS pet " +
            "WHERE (pet.specie = :specie OR :specie IS NULL) " +
            " AND (pet.age = :age OR :age IS NULL ) "
            /*" AND (pet.birthDay = :birthDay OR :birthDay IS NULL ) "*/)
    List<Pet> findNullableBySpecieAndAge(@Param("specie")String specie,
                                         @Param("age") Integer age
                                         /*@Param("birthDay") LocalDate birthDay*/);
}
