package ch.hearc.nde.pulsaryapi.repository;

import ch.hearc.nde.pulsaryapi.model.ChronoEntity;
import ch.hearc.nde.pulsaryapi.model.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ChronoRepository extends CrudRepository<ChronoEntity,Long> {
    List<ChronoEntity> findAllByUser(UserEntity user);
}
