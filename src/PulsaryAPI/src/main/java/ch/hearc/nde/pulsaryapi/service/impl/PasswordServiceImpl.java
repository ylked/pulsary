package ch.hearc.nde.pulsaryapi.service.impl;

import ch.hearc.nde.pulsaryapi.service.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordServiceImpl implements PasswordService {
    private @Autowired PasswordEncoder encoder;

    @Override
    public String hashPassword(String password) {
        return encoder.encode(password);
    }

    @Override
    public boolean check(String password, String hash) {
        return encoder.matches(password, hash);
    }
}
