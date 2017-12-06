package springee.doctor;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
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

    //////////
    private static Properties prop = new Properties();

    /**read properties from file to Properties object.*/
    public DoctorRepository() {
        try (FileInputStream fStream = new FileInputStream("/src/main/resources/application.yml");
             InputStreamReader in = new InputStreamReader(fStream)) {
            try {
                prop.load(in);
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String> getSpecieList() {
        List<String> result = new ArrayList<>();
        String[] specializations = (prop.getProperty("specializations")).split("\n");
        Collections.addAll(result, specializations);
        return result;
    }
    //////////

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
