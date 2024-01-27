package ch.hearc.nde.pulsaryapi.service.impl;

import ch.hearc.nde.pulsaryapi.UserRegistrationForm;
import ch.hearc.nde.pulsaryapi.model.UserEntity;
import ch.hearc.nde.pulsaryapi.repository.UserRepository;
import ch.hearc.nde.pulsaryapi.service.FailedLoginException;
import ch.hearc.nde.pulsaryapi.service.UnavailableUsernameException;
import ch.hearc.nde.pulsaryapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.StreamSupport;

@Configuration
public class UserServiceImpl implements UserService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository repo;

    @Override
    public UserEntity create(UserRegistrationForm form) throws UnavailableUsernameException {

        if(repo.findByUsername(form.getUsername()).isPresent()){
            throw new UnavailableUsernameException();
        }

        String hashedPassword = passwordEncoder.encode(form.getPlainPassword());

        UserEntity user = new UserEntity();
        user.setUsername(form.getUsername());
        user.setPasswordHash(hashedPassword);

        repo.save(user);

        return user;
    }

    @Override
    public UserEntity login(UserRegistrationForm form) throws FailedLoginException {
        UserEntity user = repo.findByUsername(form.getUsername()).orElseThrow(FailedLoginException::new);

        if(!passwordEncoder.matches(form.getPlainPassword(), user.getPasswordHash())){
            throw new FailedLoginException();
        }

        // generate a random api token for the user
        // UUID guarantees uniqueness
        String token = UUID.randomUUID().toString();

        user.setToken(token);
        repo.save(user);

        return user;
    }
}
