package ch.hearc.nde.pulsaryapi.web;

import ch.hearc.nde.pulsaryapi.dto.User;
import ch.hearc.nde.pulsaryapi.model.UserEntity;
import ch.hearc.nde.pulsaryapi.service.UserService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class TestProjectController {
    private @Autowired MockMvc mockMvc;
    private @Autowired UserService userService;

    private UserEntity getUser() {
        User user = new User();
        user.setUsername("test" + System.currentTimeMillis());
        user.setPassword("test");
        Assertions.assertDoesNotThrow(() -> userService.create(user));
        return Assertions.assertDoesNotThrow(() -> userService.login(user));
    }

    @Test
    @Transactional
    @Rollback
    public void testCreate() throws Exception {
        String name = "test" + System.currentTimeMillis();
        String json = "{\"name\":\"" + name + "\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getUser().getToken())
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateMissingParameters() throws Exception {
        String json = "{}";
        mockMvc.perform(MockMvcRequestBuilders.post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", getUser().getToken())
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateUnauthorized() throws Exception {
        String name = "test" + System.currentTimeMillis();
        String json = "{\"name\":\"" + name + "\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/projects")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}
