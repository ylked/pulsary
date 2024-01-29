package ch.hearc.nde.pulsaryapi.repository;

import ch.hearc.nde.pulsaryapi.model.ProjectEntity;
import ch.hearc.nde.pulsaryapi.model.UserEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends CrudRepository<ProjectEntity,Long> {
    List<ProjectEntity> findAllByUser(UserEntity user);
}
