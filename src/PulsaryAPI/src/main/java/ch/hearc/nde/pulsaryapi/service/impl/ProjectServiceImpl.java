package ch.hearc.nde.pulsaryapi.service.impl;

import ch.hearc.nde.pulsaryapi.dto.Project;
import ch.hearc.nde.pulsaryapi.exceptions.MissingParametersException;
import ch.hearc.nde.pulsaryapi.exceptions.NotFoundException;
import ch.hearc.nde.pulsaryapi.model.ProjectEntity;
import ch.hearc.nde.pulsaryapi.repository.ProjectRepository;
import ch.hearc.nde.pulsaryapi.service.ProjectService;
import ch.hearc.nde.pulsaryapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectServiceImpl implements ProjectService {
    private @Autowired ProjectRepository repo;
    private @Autowired UserService userService;

    @Override
    public List<ProjectEntity> index() {
        return repo.findAllByUser(userService.currentUser());
    }

    @Override
    public ProjectEntity create(Project dto) throws MissingParametersException {
        if(dto.getName() == null){
            throw new MissingParametersException();
        }

        ProjectEntity project = new ProjectEntity();
        project.setName(dto.getName());
        project.setUser(userService.currentUser());

        if(dto.getDescription() != null)
        {
            project.setDescription(dto.getDescription());
        }

        return repo.save(project);
    }

    @Override
    public ProjectEntity get(long id) throws NotFoundException {
        ProjectEntity project = repo.findById(id).orElseThrow(NotFoundException::new);

        if (project.getUser().getId() != userService.currentUser().getId()) {
            throw new NotFoundException();
        }

        return project;
    }

    @Override
    public ProjectEntity update(long id, Project dto) throws NotFoundException {
        ProjectEntity project = repo.findById(id).orElseThrow(NotFoundException::new);

        if(project.getUser().getId() != userService.currentUser().getId()){
            throw new NotFoundException();
        }

        if(dto.getName() != null){
            project.setName(dto.getName());
        }

        if(dto.getDescription() != null){
            project.setDescription(dto.getDescription());
        }

        return repo.save(project);
    }

    @Override
    public void delete(long id) throws NotFoundException {
        ProjectEntity project = repo.findById(id).orElseThrow(NotFoundException::new);

        if(project.getUser().getId() != userService.currentUser().getId()){
            throw new NotFoundException();
        }

        repo.delete(project);
    }
}
