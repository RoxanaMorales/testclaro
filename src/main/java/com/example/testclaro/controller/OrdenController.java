package com.example.testclaro.controller;

import com.example.testclaro.model.Orden;
import com.example.testclaro.repository.OrdenRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    private final OrdenRepository repository;

    public OrdenController(OrdenRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Orden> findAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Orden> findById(@PathVariable Long id) {
        return repository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // NOTA: para crear/actualizar necesitas mandar los ids de cliente, tecnico,
    // tipo_servicio y estado ya existentes, por ejemplo:
    // { "cliente": {"idCliente": 1}, "tipoServicio": {"idTipoSer": 1}, "estado": {"idEstado": 2} }
    // Esto es un punto de partida — cuando tengas el enunciado real, probablemente
    // convenga cambiar esto por DTOs planos (idCliente, idTipoServicio, etc.) en vez
    // de mandar los objetos anidados directo.
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Orden create(@RequestBody Orden orden) {
        orden.setIdOrden(null);
        return repository.save(orden);
    }

    // Igual que en el POST: manda los objetos con solo el id, ej.
    // { "estado": {"idEstado": 3}, "tecnico": {"idTecnico": 1}, ... }
    @PutMapping("/{id}")
    public ResponseEntity<Orden> update(@PathVariable Long id, @RequestBody Orden incoming) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setEstado(incoming.getEstado());
                    existing.setCliente(incoming.getCliente());
                    existing.setTecnico(incoming.getTecnico());
                    existing.setTipoServicio(incoming.getTipoServicio());
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
