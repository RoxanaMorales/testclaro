package com.example.testclaro.controller;

import com.example.testclaro.model.Orden;
import com.example.testclaro.repository.HistorialRepository;
import com.example.testclaro.repository.OrdenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.testclaro.model.Historial;
import com.example.testclaro.model.Estado;


import java.util.List;

@RestController
@RequestMapping("/api/ordenes")
public class OrdenController {

    @Autowired
    private OrdenRepository ordenRepository;

    @Autowired
    private HistorialRepository historialRepository;

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


    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<Orden> actualizarOrden(@PathVariable Long id, @RequestBody Orden datosActualizados) {
        return ordenRepository.findById(id).map(orden -> {
                    Estado estadoAnterior = orden.getEstado();
                    Estado estadoNuevo= datosActualizados.getEstado();

                    if (estadoNuevo != null && !estadoNuevo.getIdEstado().equals(estadoAnterior.getIdEstado())){
                        Historial registro = new Historial();
                        registro.setOrden(orden);
                        registro.setEstadoAnterior(estadoAnterior);
                        registro.setEstadoNuevo(estadoNuevo);
                        historialRepository.save(registro);
                    }
                    orden.setEstado(estadoNuevo);
                    orden.setCliente(datosActualizados.getCliente());
                    orden.setTecnico (datosActualizados.getTecnico());
                    orden.setTipoServicio (datosActualizados.getTipoServicio());

                    Orden ordenActualizada = ordenRepository.save(orden);
                    return ResponseEntity.ok (ordenActualizada);
                }).orElse(ResponseEntity.notFound().build());
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
