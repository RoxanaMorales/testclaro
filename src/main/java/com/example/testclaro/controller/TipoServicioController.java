package com.example.testclaro.controller;

import com.example.testclaro.model.TipoServicio;
import com.example.testclaro.repository.TipoServicioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tipos-servicio")
public class TipoServicioController {

    private final TipoServicioRepository repository;

    public TipoServicioController(TipoServicioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<TipoServicio> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoServicio> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TipoServicio create(@RequestBody TipoServicio tipoServicio) {
        tipoServicio.setIdTipoSer(null);
        return repository.save(tipoServicio);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoServicio> update(@PathVariable Long id, @RequestBody TipoServicio incoming) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setNombreServicio(incoming.getNombreServicio());
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
