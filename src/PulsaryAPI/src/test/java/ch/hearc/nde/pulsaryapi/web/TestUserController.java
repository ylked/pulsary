package ch.hearc.nde.pulsaryapi.web;

import com.jayway.jsonpath.JsonPath;
import jakarta.transaction.Transactional;
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
public class TestUserController {
    private @Autowired MockMvc mockMvc;

    @Test
    @Transactional
    @Rollback
    public void testCreate() throws Exception {
        String username = "test" + System.currentTimeMillis();
        String password = "test";
        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateUnavailableUsername() throws Exception {
        String username = "test" + System.currentTimeMillis();
        String password = "test";
        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @Transactional
    @Rollback
    public void testCreateMissingParameters() throws Exception {
        String username = "test" + System.currentTimeMillis();
        String json = "{\"username\":\"" + username + "\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Transactional
    @Rollback
    public void testLogin() throws Exception {
        String username = "test" + System.currentTimeMillis();
        String password = "test";
        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    @Rollback
    public void testLoginFailedLogin() throws Exception {
        String username = "test" + System.currentTimeMillis();
        String password = "test";
        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "a\"}"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Transactional
    @Rollback
    public void testEditProfile() throws Exception {
        String username = "test" + System.currentTimeMillis();
        String password = "test";
        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = JsonPath.parse(response).read("$.token", String.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content("{\"username\":\"" + username + "a\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    @Transactional
    @Rollback
    public void testEditProfileUnavailableUsername() throws Exception {
        String username = "test" + System.currentTimeMillis();
        String password = "test";
        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = JsonPath.parse(response).read("$.token", String.class);

        mockMvc.perform(MockMvcRequestBuilders.put("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token)
                .content("{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}"))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteAccount() throws Exception {
        String username = "test" + System.currentTimeMillis();
        String password = "test";
        String json = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";

        mockMvc.perform(MockMvcRequestBuilders.post("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isCreated());

        String response = mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        String token = JsonPath.parse(response).read("$.token", String.class);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", token))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        mockMvc.perform(MockMvcRequestBuilders.post("/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

}
