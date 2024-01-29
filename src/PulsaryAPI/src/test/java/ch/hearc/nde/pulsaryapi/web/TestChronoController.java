package ch.hearc.nde.pulsaryapi.web;

import ch.hearc.nde.pulsaryapi.dto.Chrono;
import ch.hearc.nde.pulsaryapi.dto.User;
import ch.hearc.nde.pulsaryapi.model.ChronoEntity;
import ch.hearc.nde.pulsaryapi.model.UserEntity;
import ch.hearc.nde.pulsaryapi.service.ChronoService;
import ch.hearc.nde.pulsaryapi.service.UserService;
import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class TestChronoController {
    private @Autowired MockMvc mockMvc;
    private @Autowired UserService userService;

    private UserEntity getUser() {
        User user = new User();
        user.setUsername("test" + System.currentTimeMillis());
        user.setPassword("test");
        Assertions.assertDoesNotThrow(() -> userService.create(user));
        return Assertions.assertDoesNotThrow(() -> userService.login(user));
    }

    private long createChronoAndGetId(String token) throws Exception {
        String name = "test" + System.currentTimeMillis();
        String result = mockMvc.perform(MockMvcRequestBuilders.post("/chrono")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" + name + "\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn().getResponse().getContentAsString();

        return JsonPath.parse(result).read("$.id", Long.class);
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateChrono() throws Exception {
        UserEntity user = getUser();
        String name = "test" + System.currentTimeMillis();
        mockMvc.perform(MockMvcRequestBuilders.post("/chrono")
                        .header("Authorization", user.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" + name + "\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name))
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.start").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.end").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(user.getUsername()));
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateChronoNoToken() throws Exception {
        UserEntity user = getUser();
        String name = "test" + System.currentTimeMillis();
        mockMvc.perform(MockMvcRequestBuilders.post("/chrono")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" + name + "\"}"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateChronoInvalidToken() throws Exception {
        UserEntity user = getUser();
        String name = "test" + System.currentTimeMillis();
        mockMvc.perform(MockMvcRequestBuilders.post("/chrono")
                        .header("Authorization", user.getToken() + "a")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" + name + "\"}"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateChronoInvalidJson() throws Exception {
        UserEntity user = getUser();
        String name = "test" + System.currentTimeMillis();
        mockMvc.perform(MockMvcRequestBuilders.post("/chrono")
                        .header("Authorization", user.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" + name + "\""))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    @Rollback
    public void testStopChrono() throws Exception {
        UserEntity user = getUser();
        long id = createChronoAndGetId(user.getToken());

        mockMvc.perform(MockMvcRequestBuilders.patch("/chrono/" + id)
                        .header("Authorization", user.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.end").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.end").isNotEmpty());


        mockMvc.perform(MockMvcRequestBuilders.patch("/chrono/" + id)
                        .header("Authorization", user.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    @Rollback
    public void testStopChronoNoToken() throws Exception {
        UserEntity user = getUser();
        long id = createChronoAndGetId(user.getToken());

        mockMvc.perform(MockMvcRequestBuilders.patch("/chrono/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    @Rollback
    public void testStopChronoInvalidId() throws Exception {
        UserEntity user = getUser();
        long id = createChronoAndGetId(user.getToken());

        mockMvc.perform(MockMvcRequestBuilders.patch("/chrono/" + (id + 1))
                        .header("Authorization", user.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetChrono() throws Exception {
        UserEntity user = getUser();
        long id = createChronoAndGetId(user.getToken());

        mockMvc.perform(MockMvcRequestBuilders.get("/chrono/" + id)
                        .header("Authorization", user.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.start").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.end").doesNotExist())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.id").value(user.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.user.username").value(user.getUsername()));
    }

    @Test
    @Transactional
    @Rollback
    public void testGetChronoNoToken() throws Exception {
        UserEntity user = getUser();
        long id = createChronoAndGetId(user.getToken());

        mockMvc.perform(MockMvcRequestBuilders.get("/chrono/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateChrono() throws Exception {
        UserEntity user = getUser();
        long id = createChronoAndGetId(user.getToken());

        String name = "test" + System.currentTimeMillis();
        mockMvc.perform(MockMvcRequestBuilders.put("/chrono/" + id)
                        .header("Authorization", user.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" + name + "\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(id))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(name));
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateChronoInvalidDates() throws Exception {
        UserEntity user = getUser();
        long id = createChronoAndGetId(user.getToken());

        String name = "test" + System.currentTimeMillis();
        mockMvc.perform(MockMvcRequestBuilders.put("/chrono/" + id)
                        .header("Authorization", user.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" + name + "\", " +
                                "\"start\": \"2024-01-01T00:00:00.000Z\", " +
                                "\"end\": \"2023-01-01T00:00:00.000Z\"}"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateChronoFutureDates() throws Exception {
        UserEntity user = getUser();
        long id = createChronoAndGetId(user.getToken());

        String name = "test" + System.currentTimeMillis();
        mockMvc.perform(MockMvcRequestBuilders.put("/chrono/" + id)
                        .header("Authorization", user.getToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\": \"" + name + "\", " +
                                "\"start\": \"2024-01-01T00:00:00.000Z\", " +
                                "\"end\": \"2025-01-01T00:00:00.000Z\"}"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Transactional
    @Rollback
    public void testUpdateChronoMissingParameters() throws Exception {
        UserEntity user = getUser();
        long id = createChronoAndGetId(user.getToken());

        String name = "test" + System.currentTimeMillis();
        mockMvc.perform(MockMvcRequestBuilders.put("/chrono/" + id)
                        .header("Authorization", user.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteChrono() throws Exception {
        UserEntity user = getUser();
        long id = createChronoAndGetId(user.getToken());

        mockMvc.perform(MockMvcRequestBuilders.delete("/chrono/" + id)
                        .header("Authorization", user.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.get("/chrono/" + id)
                        .header("Authorization", user.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteChronoNoToken() throws Exception {
        UserEntity user = getUser();
        long id = createChronoAndGetId(user.getToken());

        mockMvc.perform(MockMvcRequestBuilders.delete("/chrono/" + id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    @Rollback
    public void testGetOtherUserChrono() throws Exception {
        UserEntity user = getUser();
        long id = createChronoAndGetId(user.getToken());

        UserEntity user2 = getUser();
        mockMvc.perform(MockMvcRequestBuilders.get("/chrono/" + id)
                        .header("Authorization", user2.getToken())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
