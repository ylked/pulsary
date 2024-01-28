package ch.hearc.nde.pulsaryapi.web;

import ch.hearc.nde.pulsaryapi.UserRegistrationForm;
import ch.hearc.nde.pulsaryapi.model.UserEntity;
import ch.hearc.nde.pulsaryapi.exceptions.FailedLoginException;
import ch.hearc.nde.pulsaryapi.exceptions.UnavailableUsernameException;
import ch.hearc.nde.pulsaryapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class UserController {
    private @Autowired UserService service;

    @PostMapping("/register")
    public @ResponseBody ResponseEntity<UserEntity> create_user(@RequestBody UserRegistrationForm form) {
        try {
            UserEntity user = service.create(form);
            return ResponseEntity.created(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(user.getId())
                            .toUri()).build();

        } catch (UnavailableUsernameException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT).build();

        }
    }

    @PostMapping("/login")
    public @ResponseBody ResponseEntity<UserEntity> login(@RequestBody UserRegistrationForm form) {
        try {
            UserEntity user = service.login(form);
            return ResponseEntity.ok(user);

        } catch (FailedLoginException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED).build();

        }
    }

    @PostMapping("/logout")
    public @ResponseBody ResponseEntity<UserEntity> logout() {
        service.logout();
        return ResponseEntity.ok().build();
    }
}
