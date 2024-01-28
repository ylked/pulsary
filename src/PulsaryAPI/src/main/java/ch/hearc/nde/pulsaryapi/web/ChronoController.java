package ch.hearc.nde.pulsaryapi.web;

import ch.hearc.nde.pulsaryapi.dto.Chrono;
import ch.hearc.nde.pulsaryapi.exceptions.MissingParametersException;
import ch.hearc.nde.pulsaryapi.model.ChronoEntity;
import ch.hearc.nde.pulsaryapi.service.ChronoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ChronoController {
    private @Autowired ChronoService service;

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
}
