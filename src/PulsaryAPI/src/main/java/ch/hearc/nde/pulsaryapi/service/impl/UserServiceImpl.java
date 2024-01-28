package ch.hearc.nde.pulsaryapi.service.impl;

import ch.hearc.nde.pulsaryapi.dto.User;
import ch.hearc.nde.pulsaryapi.exceptions.MissingParametersException;
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
    public UserEntity create(User dto) throws UnavailableUsernameException, MissingParametersException {
        if(dto.getUsername() == null || dto.getPassword() == null){
            throw new MissingParametersException();
        }

        if(repo.findByUsername(dto.getUsername()).isPresent()){
            throw new UnavailableUsernameException();
        }

        String hashedPassword = passwordService.hashPassword(dto.getPassword());

        UserEntity user = new UserEntity();
        user.setUsername(dto.getUsername());
        user.setPasswordHash(hashedPassword);

        repo.save(user);

        return user;
    }

    @Override
    public UserEntity login(User dto) throws FailedLoginException, MissingParametersException {
        if(dto.getUsername() == null || dto.getPassword() == null){
            throw new MissingParametersException();
        }

        UserEntity user = repo.findByUsername(dto.getUsername()).orElseThrow(FailedLoginException::new);

        if(!passwordService.check(dto.getPassword(), user.getPasswordHash())){
            throw new FailedLoginException();
        }

        tokenService.generateNewToken(user);
        return user;
    }

    @Override
    public UserEntity currentUser() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = attr.getRequest();
        UserEntity user = (UserEntity) request.getAttribute("user");

        if(user == null){
            throw new RuntimeException("No user logged in but filter did not refuse access");
        }

        return user;
    }

    @Override
    public void logout() {
        tokenService.deleteToken(currentUser().getToken());
    }

    @Override
    public void edit(User dto) throws UnavailableUsernameException {
        UserEntity user = currentUser();

        if(dto.getUsername() != null){
            if(repo.findByUsername(dto.getUsername()).isPresent()){
                throw new UnavailableUsernameException();
            }
            user.setUsername(dto.getUsername());
        }

        if(dto.getPassword() != null){
            user.setPasswordHash(passwordService.hashPassword(dto.getPassword()));
        }

        repo.save(user);
    }

    @Override
    public void delete() {
        UserEntity user = currentUser();

        repo.delete(user);
    }
}
