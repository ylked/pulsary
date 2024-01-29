package ch.hearc.nde.pulsaryapi.service;

import ch.hearc.nde.pulsaryapi.dto.User;
import ch.hearc.nde.pulsaryapi.model.UserEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.Optional;

@SpringBootTest
public class TokenServiceTests {
    private @Autowired TokenService tokenService;
    private @Autowired UserService userService;

    public UserEntity createUser(){
        User user = new User();
        String username = "test" + System.currentTimeMillis();
        user.setUsername(username);
        user.setPassword("test");
        return Assertions.assertDoesNotThrow(() -> userService.create(user));
    }

    @Test
    @Transactional
    @Rollback
    public void testGenerateNewToken() {
        UserEntity user = createUser();
        tokenService.generateNewToken(user);

        Optional<UserEntity> userFromToken = tokenService.getUserFromToken(user.getToken());
        Assertions.assertTrue(userFromToken.isPresent());
        Assertions.assertEquals(user, userFromToken.get());
    }

    @Test
    @Transactional
    @Rollback
    public void testDeleteToken(){
        UserEntity user = createUser();
        tokenService.generateNewToken(user);
        String token = user.getToken();

        Optional<UserEntity> userFromToken = tokenService.getUserFromToken(token);
        Assertions.assertTrue(userFromToken.isPresent());
        Assertions.assertEquals(user, userFromToken.get());
        Assertions.assertEquals(userFromToken.get().getToken(), token);

        tokenService.deleteToken(token);

        Optional<UserEntity> userFromToken2 = tokenService.getUserFromToken(token);
        Assertions.assertFalse(userFromToken2.isPresent());
    }
}
