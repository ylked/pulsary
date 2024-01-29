package ch.hearc.nde.pulsaryapi.web;

import ch.hearc.nde.pulsaryapi.dto.Chrono;
import ch.hearc.nde.pulsaryapi.exceptions.IncoherentDatesException;
import ch.hearc.nde.pulsaryapi.exceptions.InvalidOperationException;
import ch.hearc.nde.pulsaryapi.exceptions.MissingParametersException;
import ch.hearc.nde.pulsaryapi.exceptions.NotFoundException;
import ch.hearc.nde.pulsaryapi.model.ChronoEntity;
import ch.hearc.nde.pulsaryapi.service.ChronoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class ChronoController {
    private @Autowired ChronoService service;

    private Map<String, String> getErrorBody(String error) {
        HashMap<String, String> errorBody = new HashMap<>();
        errorBody.put("error", error);
        return errorBody;
    }

    @GetMapping("/chrono")
    public @ResponseBody ResponseEntity<?> index() {
        return ResponseEntity.ok(service.index());
    }

    @PostMapping("/chrono")
    public @ResponseBody ResponseEntity<?> create(@RequestBody Chrono dto) {
        try {
            ChronoEntity chrono = service.create(dto);
            return ResponseEntity.ok(chrono);
        } catch (MissingParametersException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PatchMapping("/chrono/{id}")
    public @ResponseBody ResponseEntity<?> stop(@PathVariable long id) {
        try {
            ChronoEntity chrono = service.stop(id);
            return ResponseEntity.ok(chrono);
        } catch (InvalidOperationException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(getErrorBody("This chronometer has already been stopped"));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/chrono/{id}")
    public @ResponseBody ResponseEntity<?> get(@PathVariable long id) {
        try {
            ChronoEntity chrono = service.get(id);
            return ResponseEntity.ok(chrono);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/chrono/{id}")
    public @ResponseBody ResponseEntity<?> update(@PathVariable long id, @RequestBody Chrono dto) {
        try {
            ChronoEntity chrono = service.update(id, dto);
            return ResponseEntity.ok(chrono);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IncoherentDatesException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(getErrorBody("The start date cannot be after the end date"));
        } catch (InvalidOperationException e) {
            return ResponseEntity
                    .status(HttpStatus.FORBIDDEN)
                    .body(getErrorBody("Both dates cannot be in the future"));
        }
    }

    @DeleteMapping("/chrono/{id}")
    public @ResponseBody ResponseEntity<?> delete(@PathVariable long id) {
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
