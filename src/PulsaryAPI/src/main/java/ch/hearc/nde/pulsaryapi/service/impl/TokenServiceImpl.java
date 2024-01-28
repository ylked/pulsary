package ch.hearc.nde.pulsaryapi.service.impl;

import ch.hearc.nde.pulsaryapi.model.UserEntity;
import ch.hearc.nde.pulsaryapi.repository.UserRepository;
import ch.hearc.nde.pulsaryapi.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class TokenServiceImpl implements TokenService {
    private @Autowired UserRepository repo;

    @Override
    public void generateNewToken(UserEntity user) {
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        repo.save(user);
    }

    @Override
    public Optional<UserEntity> getUsernameFromToken(String token) {
        return repo.findByToken(token);
    }

    @Override
    public void deleteToken(String token) {
        Optional<UserEntity> user = repo.findByToken(token);
        user.ifPresent(user_ -> user_.setToken(null));
        user.ifPresent(repo::save);
    }
}
