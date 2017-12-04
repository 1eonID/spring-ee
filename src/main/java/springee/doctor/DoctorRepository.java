package springee.doctor;

import org.springframework.stereotype.Repository;

import javax.print.Doc;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class DoctorRepository {

    private final UUID uuid1 = UUID.randomUUID();
    private final UUID uuid2 = UUID.randomUUID();
    private final UUID uuid3 = UUID.randomUUID();

    private Map<UUID, Doctor> doctorMap = new ConcurrentHashMap<UUID, Doctor>() {{
        put(uuid1, new Doctor(uuid1, "Robert Martin", "psychologist"));
        put(uuid2, new Doctor(uuid2, "Josh Long", "surgeon"));
        put(uuid3, new Doctor(uuid3, "Joshua Bloch", "ophthalmologist"));
    }};

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

    public Optional<Doctor> update(UUID id, Doctor doctor) {
        return Optional.ofNullable(doctorMap.put(id, doctor));
    }

    public Optional<Doctor> delete(UUID id) {
        return Optional.ofNullable(doctorMap.remove(id));
    }
}
