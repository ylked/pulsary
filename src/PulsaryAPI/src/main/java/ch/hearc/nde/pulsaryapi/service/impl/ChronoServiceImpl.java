package ch.hearc.nde.pulsaryapi.service.impl;

import ch.hearc.nde.pulsaryapi.dto.Chrono;
import ch.hearc.nde.pulsaryapi.exceptions.IncoherentDatesException;
import ch.hearc.nde.pulsaryapi.exceptions.InvalidOperationException;
import ch.hearc.nde.pulsaryapi.exceptions.MissingParametersException;
import ch.hearc.nde.pulsaryapi.exceptions.NotFoundException;
import ch.hearc.nde.pulsaryapi.model.ChronoEntity;
import ch.hearc.nde.pulsaryapi.repository.ChronoRepository;
import ch.hearc.nde.pulsaryapi.service.ChronoService;
import ch.hearc.nde.pulsaryapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChronoServiceImpl implements ChronoService {
    private @Autowired ChronoRepository repo;
    private @Autowired UserService userService;

    @Override
    public List<ChronoEntity> index() {
        return repo.findAllByUser(userService.currentUser());
    }

    @Override
    public ChronoEntity create(Chrono dto) throws MissingParametersException {
        if (dto.getName() == null) {
            throw new MissingParametersException();
        }

        ChronoEntity chrono = new ChronoEntity();
        chrono.setName(dto.getName());
        chrono.setStart(LocalDateTime.now());
        chrono.setEnd(null);
        chrono.setUser(userService.currentUser());
        repo.save(chrono);

        return chrono;
    }

    @Override
    public ChronoEntity stop(long id) throws InvalidOperationException, NotFoundException {
        ChronoEntity chrono = repo.findById(id).orElseThrow(NotFoundException::new);

        if(chrono.getUser().getId() != userService.currentUser().getId()){
            throw new NotFoundException();
        }

        if(chrono.getEnd() != null){
            throw new InvalidOperationException();
        }

        chrono.setEnd(LocalDateTime.now());
        repo.save(chrono);

        return chrono;
    }

    @Override
    public ChronoEntity get(long id) throws NotFoundException {
        ChronoEntity chrono = repo.findById(id).orElseThrow(NotFoundException::new);

        if(chrono.getUser().getId() != userService.currentUser().getId()){
            throw new NotFoundException();
        }

        return chrono;
    }

    @Override
    public ChronoEntity update(long id, Chrono dto)
            throws NotFoundException, IncoherentDatesException, InvalidOperationException {
        ChronoEntity chrono = repo.findById(id).orElseThrow(NotFoundException::new);

        if(chrono.getUser().getId() != userService.currentUser().getId()){
            throw new NotFoundException();
        }

        if(dto.getName() != null){
            chrono.setName(dto.getName());
        }

        if(dto.getStart() != null){
            chrono.setStart(dto.getStart());
        }

        if(dto.getEnd() != null){
            chrono.setEnd(dto.getEnd());
        }

        if(chrono.getStart() != null && chrono.getEnd() != null && chrono.getStart().isAfter(chrono.getEnd())){
            throw new IncoherentDatesException();
        }

        if(chrono.getStart() != null && chrono.getEnd() != null && (
                chrono.getEnd().isAfter(LocalDateTime.now()) || chrono.getStart().isAfter(LocalDateTime.now())))
        {
            throw new InvalidOperationException();
        }

        repo.save(chrono);

        return chrono;
    }

    @Override
    public void delete(long id) throws NotFoundException {
        ChronoEntity chrono = repo.findById(id).orElseThrow(NotFoundException::new);

        if(chrono.getUser().getId() != userService.currentUser().getId()){
            throw new NotFoundException();
        }

        repo.deleteById(id);
    }
}
