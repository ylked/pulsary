package ch.hearc.nde.pulsaryapi.service.impl;

import ch.hearc.nde.pulsaryapi.dto.UserRegistrationForm;
import ch.hearc.nde.pulsaryapi.model.UserEntity;
import ch.hearc.nde.pulsaryapi.repository.UserRepository;
import ch.hearc.nde.pulsaryapi.exceptions.FailedLoginException;
import ch.hearc.nde.pulsaryapi.exceptions.UnavailableUsernameException;
import ch.hearc.nde.pulsaryapi.service.PasswordService;
import ch.hearc.nde.pulsaryapi.service.TokenService;
import ch.hearc.nde.pulsaryapi.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private @Autowired PasswordService passwordService;
    private @Autowired UserRepository repo;
    private @Autowired TokenService tokenService;

    @Override
    public UserEntity create(UserRegistrationForm form) throws UnavailableUsernameException {

        if(repo.findByUsername(form.getUsername()).isPresent()){
            throw new UnavailableUsernameException();
        }

        String hashedPassword = passwordService.hashPassword(form.getPlainPassword());

        UserEntity user = new UserEntity();
        user.setUsername(form.getUsername());
        user.setPasswordHash(hashedPassword);

        repo.save(user);

        return user;
    }

    @Override
    public UserEntity login(UserRegistrationForm form) throws FailedLoginException {
        UserEntity user = repo.findByUsername(form.getUsername()).orElseThrow(FailedLoginException::new);

        if(!passwordService.check(form.getPlainPassword(), user.getPasswordHash())){
            throw new FailedLoginException();
        }

        tokenService.generateNewToken(user);
        return user;
    }

    @Override
    public Optional<UserEntity> currentUser() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        UserEntity user = (UserEntity) request.getAttribute("user");

        return user == null ? Optional.empty() : Optional.of(user);
    }

    @Override
    public void logout() {
        currentUser().ifPresent(user -> tokenService.deleteToken(user.getToken()));
    }
}
