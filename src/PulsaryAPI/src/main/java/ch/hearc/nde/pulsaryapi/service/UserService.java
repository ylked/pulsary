package ch.hearc.nde.pulsaryapi.service;

import ch.hearc.nde.pulsaryapi.UserRegistrationForm;
import ch.hearc.nde.pulsaryapi.exceptions.FailedLoginException;
import ch.hearc.nde.pulsaryapi.exceptions.UnavailableUsernameException;
import ch.hearc.nde.pulsaryapi.model.UserEntity;

public interface UserService {
    UserEntity create(UserRegistrationForm form) throws UnavailableUsernameException;
    UserEntity login(UserRegistrationForm form) throws FailedLoginException;

}
