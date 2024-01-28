package ch.hearc.nde.pulsaryapi.service.impl;

import ch.hearc.nde.pulsaryapi.dto.Chrono;
import ch.hearc.nde.pulsaryapi.exceptions.MissingParametersException;
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
}
