package ch.hearc.nde.pulsaryapi.web;

import ch.hearc.nde.pulsaryapi.dto.User;
import ch.hearc.nde.pulsaryapi.exceptions.MissingParametersException;
import ch.hearc.nde.pulsaryapi.model.UserEntity;
import ch.hearc.nde.pulsaryapi.exceptions.FailedLoginException;
import ch.hearc.nde.pulsaryapi.exceptions.UnavailableUsernameException;
import ch.hearc.nde.pulsaryapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Controller
public class UserController {
    private @Autowired UserService service;

    private Map<String, String> getErrorBody(String error) {
        HashMap<String, String> errorBody = new HashMap<>();
        errorBody.put("error", error);
        return errorBody;
    }

    @PostMapping("/register")
    public @ResponseBody ResponseEntity<?> create_user(@RequestBody User dto) {
        try {
            UserEntity user = service.create(dto);
            return ResponseEntity.created(
                    ServletUriComponentsBuilder
                            .fromCurrentRequest()
                            .path("/{id}")
                            .buildAndExpand(user.getId())
                            .toUri()).build();

        } catch (UnavailableUsernameException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(getErrorBody("Username already taken"));
        } catch (MissingParametersException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(getErrorBody("Missing parameters"));
        }
    }

    @PostMapping("/login")
    public @ResponseBody ResponseEntity<?> login(@RequestBody User dto) {
        try {
            UserEntity user = service.login(dto);
            return ResponseEntity.ok(user);

        } catch (FailedLoginException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(getErrorBody("Invalid credentials"));
        } catch (MissingParametersException e){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(getErrorBody("Missing parameters"));
        }
    }

    @PostMapping("/logout")
    public @ResponseBody ResponseEntity<UserEntity> logout() {
        service.logout();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users")
    public @ResponseBody ResponseEntity<?> edit(@RequestBody User dto){
        try {
            service.edit(dto);
            return ResponseEntity.ok().build();
        } catch (UnavailableUsernameException e) {
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(getErrorBody("Username already taken"));
        } catch (FailedLoginException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(getErrorBody("Invalid credentials"));
        }
    }

    @DeleteMapping("/users")
    public @ResponseBody ResponseEntity<?> delete() {
        try {
            service.delete();
            return ResponseEntity.noContent().build();
        } catch (FailedLoginException e) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(getErrorBody("Invalid credentials"));
        }
    }
}
