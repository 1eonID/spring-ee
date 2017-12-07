package springee.doctor;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@RequiredArgsConstructor
@Repository
public class DoctorRepository {

    private final DoctorConfig doctorConfig;

    private final UUID uuid1 = UUID.randomUUID();
    private final UUID uuid2 = UUID.randomUUID();
    private final UUID uuid3 = UUID.randomUUID();

    private Map<UUID, Doctor> doctorMap = new ConcurrentHashMap<UUID, Doctor>() {{
        put(uuid1, new Doctor(uuid1, "Robert Martin", "psychologist"));
        put(uuid2, new Doctor(uuid2, "Josh Long", "surgeon"));
        put(uuid3, new Doctor(uuid3, "Joshua Bloch", "ophthalmologist"));
    }};

    public List<String> getSpecieList() {
        return doctorConfig.getSpecializations();
    }

    public Collection<Doctor> findAll() {
        return doctorMap.values();
    }

    public Optional<Doctor> findById(UUID id) {
        return Optional.ofNullable(doctorMap.get(id));
    }

    public Doctor create(Doctor doctor) {

        UUID uuid = UUID.randomUUID();
        doctor.setId(uuid);
        doctorMap.put(uuid, doctor);
        return doctor;
    }

    public ResponseEntity<Void> update(UUID id, Doctor doctor) {
        List<String> specieList = getSpecieList();

        if (!doctorMap.containsKey(id)) {
            return ResponseEntity.notFound().build();
        } else if (!doctor.getId().equals(id) && specieList.contains(doctor.getSpecialization())) {
            return ResponseEntity.badRequest().build();
        }
        doctorMap.put(id, doctor);
        return ResponseEntity.noContent().build();
    }

    public Optional<Doctor> delete(UUID id) {
        return Optional.ofNullable(doctorMap.remove(id));
    }
}
