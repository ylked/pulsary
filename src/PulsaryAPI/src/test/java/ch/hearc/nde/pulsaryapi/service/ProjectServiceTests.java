package ch.hearc.nde.pulsaryapi.service;

import ch.hearc.nde.pulsaryapi.dto.Project;
import ch.hearc.nde.pulsaryapi.dto.User;
import ch.hearc.nde.pulsaryapi.exceptions.FailedLoginException;
import ch.hearc.nde.pulsaryapi.exceptions.MissingParametersException;
import ch.hearc.nde.pulsaryapi.exceptions.NotFoundException;
import ch.hearc.nde.pulsaryapi.exceptions.UnavailableUsernameException;
import ch.hearc.nde.pulsaryapi.model.ProjectEntity;
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
public class ProjectServiceTests {
    private @Autowired ProjectService projectService;
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

    @Test
    @Transactional
    @Rollback
    void testCreate() throws UnavailableUsernameException, MissingParametersException {
        Project project = new Project();
        project.setName("test");
        ProjectEntity projectEntity = Assertions.assertDoesNotThrow(() -> projectService.create(project));
        Assertions.assertEquals(project.getName(), projectEntity.getName());
    }

    @Test
    @Transactional
    @Rollback
    void testCreateMissingParameters() throws UnavailableUsernameException, MissingParametersException {
        Project project = new Project();
        Assertions.assertThrows(MissingParametersException.class, () -> projectService.create(project));
    }

    @Test
    @Transactional
    @Rollback
    void testGet() throws UnavailableUsernameException, MissingParametersException {
        Project project = new Project();
        project.setName("test");
        ProjectEntity projectEntity = projectService.create(project);
        ProjectEntity projectEntity2 = Assertions.assertDoesNotThrow(() -> projectService.get(projectEntity.getId()));
        Assertions.assertEquals(projectEntity, projectEntity2);
    }

    @Test
    @Transactional
    @Rollback
    void testUpdate() throws UnavailableUsernameException, MissingParametersException {
        Project project = new Project();
        project.setName("test");
        ProjectEntity projectEntity = projectService.create(project);
        project.setName("test2");
        ProjectEntity projectEntity2 = Assertions.assertDoesNotThrow(() -> projectService.update(projectEntity.getId(), project));
        Assertions.assertEquals(project.getName(), projectEntity2.getName());
    }

    @Test
    @Transactional
    @Rollback
    void testDelete() throws UnavailableUsernameException, MissingParametersException {
        Project project = new Project();
        project.setName("test");
        ProjectEntity projectEntity = projectService.create(project);
        Assertions.assertDoesNotThrow(() -> projectService.delete(projectEntity.getId()));
        Assertions.assertThrows(NotFoundException.class, () -> projectService.get(projectEntity.getId()));
    }
}
