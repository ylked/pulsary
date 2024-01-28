package ch.hearc.nde.pulsaryapi.service;


public interface PasswordService {
        String hashPassword(String password);
        boolean check(String password, String hash);
}
