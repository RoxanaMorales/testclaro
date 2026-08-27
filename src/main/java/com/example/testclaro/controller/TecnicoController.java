package com.example.testclaro.controller;

import com.example.testclaro.model.Tecnico;
import com.example.testclaro.repository.TecnicoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tecnicos")
public class TecnicoController {

    private final TecnicoRepository repository;

    public TecnicoController(TecnicoRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Tecnico> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tecnico> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Tecnico create(@RequestBody Tecnico tecnico) {
        tecnico.setIdTecnico(null);
        return repository.save(tecnico);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tecnico> update(@PathVariable Long id, @RequestBody Tecnico incoming) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setNoTecnico(incoming.getNoTecnico());
                    existing.setNombre(incoming.getNombre());
                    existing.setEstado(incoming.getEstado());
                    return ResponseEntity.ok(repository.save(existing));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
