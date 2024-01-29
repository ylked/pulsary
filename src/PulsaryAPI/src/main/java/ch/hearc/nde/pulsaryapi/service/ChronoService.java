package ch.hearc.nde.pulsaryapi.service;

import ch.hearc.nde.pulsaryapi.dto.Chrono;
import ch.hearc.nde.pulsaryapi.exceptions.IncoherentDatesException;
import ch.hearc.nde.pulsaryapi.exceptions.InvalidOperationException;
import ch.hearc.nde.pulsaryapi.exceptions.MissingParametersException;
import ch.hearc.nde.pulsaryapi.exceptions.NotFoundException;
import ch.hearc.nde.pulsaryapi.model.ChronoEntity;
import ch.hearc.nde.pulsaryapi.repository.ChronoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

public interface ChronoService {
    List<ChronoEntity> index();
    ChronoEntity create(Chrono dto) throws MissingParametersException, NotFoundException;

    ChronoEntity stop(long id) throws InvalidOperationException, NotFoundException;
    ChronoEntity get(long id) throws NotFoundException;

    ChronoEntity update(long id, Chrono dto)
            throws NotFoundException, IncoherentDatesException, InvalidOperationException;

    void delete(long id) throws NotFoundException;
}
