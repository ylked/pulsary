package ch.hearc.nde.pulsaryapi.service;

import ch.hearc.nde.pulsaryapi.dto.Chrono;
import ch.hearc.nde.pulsaryapi.dto.User;
import ch.hearc.nde.pulsaryapi.exceptions.*;
import ch.hearc.nde.pulsaryapi.model.ChronoEntity;
import ch.hearc.nde.pulsaryapi.model.UserEntity;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.ArrayList;

@SpringBootTest
public class ChronoServiceTests {
    private @Autowired ChronoService service;
    private @Autowired UserService userService;
    private UserEntity user;


    @BeforeEach
    public void setUp() throws UnavailableUsernameException, MissingParametersException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String username = "test" + System.currentTimeMillis();
        User user = new User();
        user.setUsername(username);
        user.setPassword("test");
        this.user = userService.create(user);
        request.setAttribute("user", this.user);
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @AfterEach
    public void cleanUp() throws FailedLoginException {
        userService.delete();
        RequestContextHolder.resetRequestAttributes();
    }

    public ChronoEntity createChrono(String name){
        Chrono chrono = new Chrono();
        chrono.setName(name);
        return Assertions.assertDoesNotThrow(() -> service.create(chrono));
    }

    @Test
    void testIndex(){
        Assertions.assertEquals(new ArrayList<>(), service.index());
    }

    @Test
    @Transactional
    @Rollback
    void testCreate(){
        ChronoEntity chronoEntity1 = createChrono("test");
        Assertions.assertEquals(1, service.index().size());

        ChronoEntity chronoEntity = service.index().get(0);

        Assertions.assertEquals(chronoEntity1.getName(), chronoEntity.getName());
        Assertions.assertEquals(chronoEntity1.getStart(), chronoEntity.getStart());
        Assertions.assertNotNull(chronoEntity.getStart());
        Assertions.assertNull(chronoEntity.getEnd());
        Assertions.assertEquals(user, chronoEntity.getUser());
    }

    @Test
    @Transactional
    @Rollback
    void testStop() throws InvalidOperationException, NotFoundException {
        ChronoEntity chronoEntity1 = createChrono("test");
        Assertions.assertEquals(1, service.index().size());

        ChronoEntity chronoEntity = service.index().get(0);

        Assertions.assertEquals(chronoEntity1.getName(), chronoEntity.getName());
        Assertions.assertEquals("test", chronoEntity.getName());
        Assertions.assertNotNull(chronoEntity.getStart());
        Assertions.assertNull(chronoEntity.getEnd());
        Assertions.assertEquals(user, chronoEntity.getUser());

        ChronoEntity newChronoEntity = service.stop(chronoEntity.getId());

        Assertions.assertNotNull(chronoEntity.getEnd());
        Assertions.assertEquals(chronoEntity.getStart(), newChronoEntity.getStart());
        Assertions.assertEquals(chronoEntity.getName(), newChronoEntity.getName());
        Assertions.assertEquals(chronoEntity.getUser(), newChronoEntity.getUser());

        Assertions.assertThrows(InvalidOperationException.class, () -> service.stop(newChronoEntity.getId()));
    }

    @Test
    @Transactional
    @Rollback
    void testGet(){
        ChronoEntity chronoEntity = createChrono("test");
        Assertions.assertEquals(1, service.index().size());
        Assertions.assertEquals("test", chronoEntity.getName());

        Assertions.assertNotNull(chronoEntity.getStart());
        Assertions.assertNull(chronoEntity.getEnd());
        Assertions.assertEquals(user, chronoEntity.getUser());
        ChronoEntity getChronoEntity = Assertions.assertDoesNotThrow(() -> service.get(chronoEntity.getId()));
        Assertions.assertEquals(chronoEntity, getChronoEntity);
        Assertions.assertEquals(getChronoEntity, service.index().get(0));
    }

    @Test
    @Transactional
    @Rollback
    void testUpdate(){
        ChronoEntity chronoEntity = createChrono("test");
        Assertions.assertEquals(1, service.index().size());
        Assertions.assertEquals("test", chronoEntity.getName());

        Chrono newChrono = new Chrono();
        newChrono.setName("test2");
        Assertions.assertDoesNotThrow(() -> service.update(chronoEntity.getId(), newChrono));

        ChronoEntity newChronoEntity = Assertions.assertDoesNotThrow(() -> service.get(chronoEntity.getId()));

        Assertions.assertEquals("test2", newChronoEntity.getName());
        Assertions.assertEquals(chronoEntity.getStart(), newChronoEntity.getStart());
        Assertions.assertEquals(chronoEntity.getUser(), newChronoEntity.getUser());
        Assertions.assertNull(newChronoEntity.getEnd());
    }

    @Test
    @Transactional
    @Rollback
    void testDelete(){
        ChronoEntity chronoEntity = createChrono("test");
        Assertions.assertEquals(1, service.index().size());
        Assertions.assertEquals("test", chronoEntity.getName());

        Assertions.assertDoesNotThrow(() -> service.delete(chronoEntity.getId()));
        Assertions.assertEquals(0, service.index().size());

        Assertions.assertThrows(NotFoundException.class, () -> service.get(chronoEntity.getId()));
    }

}
