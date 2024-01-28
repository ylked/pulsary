package ch.hearc.nde.pulsaryapi.service;

import ch.hearc.nde.pulsaryapi.dto.User;
import ch.hearc.nde.pulsaryapi.exceptions.FailedLoginException;
import ch.hearc.nde.pulsaryapi.exceptions.MissingParametersException;
import ch.hearc.nde.pulsaryapi.exceptions.UnavailableUsernameException;
import ch.hearc.nde.pulsaryapi.model.UserEntity;

import java.util.Optional;

public interface UserService {
    UserEntity create(User dto) throws UnavailableUsernameException, MissingParametersException;

    UserEntity login(User dto) throws FailedLoginException, MissingParametersException;

    void logout();

    void edit(User dto) throws UnavailableUsernameException, FailedLoginException;

    void delete() throws FailedLoginException;

    UserEntity currentUser();

}
