package ch.hearc.nde.pulsaryapi.service;

import ch.hearc.nde.pulsaryapi.model.UserEntity;

import java.util.Optional;

public interface TokenService {
    void generateNewToken(UserEntity user);
    Optional<UserEntity> getUsernameFromToken(String token);
    void deleteToken(String token);
}
