package springee.pet;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import com.google.common.io.Resources;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.http.HttpHeaders;
import springee.store.Medicine;
import springee.store.MedicineRepository;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureRestDocs(outputDir = "build/generated-snippets")
public class PetControllerTest {

    @Autowired
    JpaPetRepository petRepository;

    @Autowired
    MedicineRepository medicineRepository;

    @Autowired
    MockMvc mockMvc;

    @After
    public void cleanup() {
        petRepository.deleteAll();
        medicineRepository.deleteAll();
    }

    @Test
    public void getAllPets() throws Exception {
        petRepository.save(new Pet("Tom", "Cat", 3, LocalDate.now(), null, null));

        mockMvc.perform(get("/pets")
                .header(HttpHeaders.AUTHORIZATION, "Basic bGVvbl9pZDo0NTY="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].name", is("Tom")));
    }

    @Test
    public void sortByAge() throws Exception {
        petRepository.save(new Pet("Tom", "Cat", 3, LocalDate.now(), null, null));
        petRepository.save(new Pet("Jerry", "Mouse", 1, LocalDate.now(), null, null));

        mockMvc.perform(get("/pets")
                .header(HttpHeaders.AUTHORIZATION, "Basic bGVvbl9pZDo0NTY=")
                .param("sort", "age"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(2)))
                .andExpect(jsonPath("$.content[0].age", is(1)))
                .andExpect(jsonPath("$.content[1].age", is(3)));
    }

    @Test
    public void getPetById() throws Exception {
        Integer id = petRepository.save(new Pet("Tom", "Cat", 3, LocalDate.now(), null, null))
                .getId();

        mockMvc.perform(get("/pets/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Basic bGVvbl9pZDo0NTY="))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(id)))
                .andExpect(jsonPath("$.name", is("Tom")))
                .andExpect(jsonPath("$.*", hasSize(7)))
                        .andDo(document("pet/get-by-id",
                                pathParameters(parameterWithName("id").description("Pet ID")),
                                responseFields(
                                        fieldWithPath("id").description("some description"),
                                        fieldWithPath("name").description("some description"),
                                        fieldWithPath("age").description("some description"),
                                        fieldWithPath("specie").description("Pet type (Cat or Dog)"),
                                        fieldWithPath("birthDate").description("some description"),
                                        fieldWithPath("medicalCard").description("some description"),
                                        fieldWithPath("prescriptions").description("some description")
                                )));
    }

    @Test
    public void petNotFound() throws Exception {
        mockMvc.perform(get("/pets/1")
                .header(HttpHeaders.AUTHORIZATION, "Basic bGVvbl9pZDo0NTY="))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void createPet() throws Exception {
        String body = readFile("cat.json");

        mockMvc.perform(post("/pets")
                .header(HttpHeaders.AUTHORIZATION, "Basic bGVvbl9pZDo0NTY=")
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", containsString("/pets/")));

        List<Pet> all = petRepository.findAll();
        assertThat(all, hasSize(1));
        assertThat(all.get(0).getName().get(), is("Tom"));
    }

    @Test
    public void updatePet() throws Exception {
        Integer id = petRepository.save(new Pet("Jerry", "Mouse", 1, LocalDate.now(), null, null))
                .getId();

        String body = readFile("cat.json");

        mockMvc.perform(put("/pets/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Basic bGVvbl9pZDo0NTY=")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());

        Pet pet = petRepository.findById(id).get();

        assertThat(pet.getName().get(), is("Tom"));
        assertThat(pet.getSpecie(), is("Cat"));
    }

    @Test
    public void deletePet() throws Exception {
        Integer id = petRepository.save(new Pet("Tom", "Cat", 3, LocalDate.now(), null, null))
                .getId();

        mockMvc.perform(delete("/pets/{id}", id)
                .header(HttpHeaders.AUTHORIZATION, "Basic bGVvbl9pZDo0NTY="))
                .andExpect(status().isNoContent());

        Optional<Pet> mayBePet = petRepository.findById(id);

        assertFalse(mayBePet.isPresent());

    }

    @Test
    public void prescribeMedicine() throws Exception {
        Integer id = petRepository.save(new Pet("Tom", "Cat", 3, LocalDate.now(), null, null))
                .getId();

        medicineRepository.save(new Medicine("Briliantum Greenus", 1));

        String body = readFile("prescription.json");

        mockMvc.perform(post("/pets/{id}/prescriptions", id)
                .header(HttpHeaders.AUTHORIZATION, "Basic bGVvbl9pZDo0NTY=")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isOk());

        List<Prescription> prescriptions = petRepository.findById(id).get().getPrescriptions();

        assertThat(prescriptions, hasSize(1));

        Medicine greenus = medicineRepository.findByName("Briliantum Greenus").get();

        assertThat(greenus.getQuantity(), is(0));
    }

    @Test
    public void notEnoughMedicine() throws Exception {
        Integer id = petRepository.save(new Pet("Tom", "Cat", 3, LocalDate.now(), null, null))
                .getId();

        String body = readFile("prescription.json");

        mockMvc.perform(post("/pets/{id}/prescriptions", id)
                .header(HttpHeaders.AUTHORIZATION, "Basic bGVvbl9pZDo0NTY=")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body))
                .andExpect(status().isBadRequest());
    }

    private String readFile(String resourceName) throws IOException {
        return Resources.toString(Resources.getResource(resourceName), Charset.defaultCharset());
    }
}