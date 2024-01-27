package ch.hearc.nde.pulsaryapi.repository;

import ch.hearc.nde.pulsaryapi.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity,Long> {
    Optional<UserEntity> findByUsername(String username);
    Optional<UserEntity> findByToken(String token);

}
