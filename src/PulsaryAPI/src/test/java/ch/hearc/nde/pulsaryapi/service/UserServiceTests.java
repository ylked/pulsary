package ch.hearc.nde.pulsaryapi.service;

import ch.hearc.nde.pulsaryapi.dto.User;
import ch.hearc.nde.pulsaryapi.exceptions.FailedLoginException;
import ch.hearc.nde.pulsaryapi.exceptions.MissingParametersException;
import ch.hearc.nde.pulsaryapi.exceptions.UnavailableUsernameException;
import ch.hearc.nde.pulsaryapi.model.UserEntity;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@SpringBootTest
public class UserServiceTests {
    private @Autowired UserService userService;
    @Test
    @Transactional
    @Rollback
    void testCreate() throws UnavailableUsernameException, MissingParametersException {
        String username = "test" + System.currentTimeMillis();
        User user = new User();
        user.setUsername(username);
        user.setPassword("test");
        Assertions.assertDoesNotThrow(() -> userService.create(user));
    }

    @Test
    @Transactional
    @Rollback
    void testCreateUnavailableUsername() throws UnavailableUsernameException, MissingParametersException {
        String username = "test" + System.currentTimeMillis();
        User user = new User();
        user.setUsername(username);
        user.setPassword("test");
        Assertions.assertDoesNotThrow(() -> userService.create(user));
        Assertions.assertThrows(UnavailableUsernameException.class, () -> userService.create(user));
    }

    @Test
    @Transactional
    @Rollback
    void testCreateMissingParameters() throws UnavailableUsernameException, MissingParametersException {
        String username = "test" + System.currentTimeMillis();
        User user = new User();
        user.setUsername(username);
        Assertions.assertThrows(MissingParametersException.class, () -> userService.create(user));
    }

    @Test
    @Transactional
    @Rollback
    void testLogin() throws FailedLoginException, MissingParametersException {
        String username = "test" + System.currentTimeMillis();
        User user = new User();
        user.setUsername(username);
        user.setPassword("test");
        Assertions.assertDoesNotThrow(() -> userService.create(user));
        UserEntity userEntity = Assertions.assertDoesNotThrow(() -> userService.login(user));

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setAttribute("user", userEntity);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        Assertions.assertDoesNotThrow(() -> userService.logout());
    }

    @Test
    @Transactional
    @Rollback
    void testLoginFailedLogin() throws FailedLoginException, MissingParametersException {
        String username = "test" + System.currentTimeMillis();
        User user = new User();
        user.setUsername(username);
        user.setPassword("test");
        Assertions.assertDoesNotThrow(() -> userService.create(user));
        user.setPassword("wrong");
        Assertions.assertThrows(FailedLoginException.class, () -> userService.login(user));
    }

    @Test
    @Transactional
    @Rollback
    void testLoginMissingParameters() throws FailedLoginException, MissingParametersException {
        String username = "test" + System.currentTimeMillis();
        User user = new User();
        user.setUsername(username);
        user.setPassword("test");
        Assertions.assertDoesNotThrow(() -> userService.create(user));
        user.setPassword(null);
        Assertions.assertThrows(MissingParametersException.class, () -> userService.login(user));
    }
}
