package ch.hearc.nde.pulsaryapi.service;

import ch.hearc.nde.pulsaryapi.dto.Project;
import ch.hearc.nde.pulsaryapi.exceptions.MissingParametersException;
import ch.hearc.nde.pulsaryapi.exceptions.NotFoundException;
import ch.hearc.nde.pulsaryapi.model.ProjectEntity;

import java.util.List;

public interface ProjectService {

    List<ProjectEntity> index();
    ProjectEntity create(Project dto) throws MissingParametersException;
    ProjectEntity get(long id) throws NotFoundException;
    ProjectEntity update(long id, Project dto) throws NotFoundException;
    void delete(long id) throws NotFoundException;
}
