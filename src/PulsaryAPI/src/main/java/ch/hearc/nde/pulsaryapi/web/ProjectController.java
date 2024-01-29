package ch.hearc.nde.pulsaryapi.web;

import ch.hearc.nde.pulsaryapi.dto.Project;
import ch.hearc.nde.pulsaryapi.exceptions.MissingParametersException;
import ch.hearc.nde.pulsaryapi.exceptions.NotFoundException;
import ch.hearc.nde.pulsaryapi.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProjectController {
    private @Autowired ProjectService service;

    @PostMapping(value = {"/projects"})
    public ResponseEntity<?> create(@RequestBody Project dto){
        try{
            return ResponseEntity.ok(service.create(dto));
        } catch (MissingParametersException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping(value = {"/projects/{id}"})
    public ResponseEntity<?> update(@PathVariable long id, @RequestBody Project dto){
        try {
            return ResponseEntity.ok(service.update(id, dto));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = {"/projects"})
    public ResponseEntity<?> index(){
        return ResponseEntity.ok(service.index());
    }

    @GetMapping(value = {"/projects/{id}"})
    public ResponseEntity<?> get(@PathVariable long id){
        try {
            return ResponseEntity.ok(service.get(id));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping(value = {"/projects/{id}"})
    public ResponseEntity<?> delete(@PathVariable long id){
        try {
            service.delete(id);
            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

}
