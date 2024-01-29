package ch.hearc.nde.pulsaryapi.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PasswordServiceTests {
    private @Autowired PasswordService passwordService;

    @Test
    public void testHashPassword() {
        String password = "password" + System.currentTimeMillis();
        String hash = passwordService.hashPassword(password);
        Assertions.assertTrue(passwordService.check(password, hash));
        Assertions.assertFalse(passwordService.check(password + "a", hash));
        Assertions.assertFalse(passwordService.check(password, hash + "a"));
    }
}
