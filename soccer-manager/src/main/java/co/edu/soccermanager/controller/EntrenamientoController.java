package co.edu.soccermanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.soccermanager.dto.MotivoDto;
import co.edu.soccermanager.dto.ReprogramacionDto;
import co.edu.soccermanager.modelo.Entrenamiento;
import co.edu.soccermanager.service.IEntrenamientoService;

/** Capa de presentación: recibe las peticiones HTTP de /api/entrenamientos y delega en el servicio. */
@RestController
@RequestMapping("/api/entrenamientos")
public class EntrenamientoController {

    @Autowired
    private IEntrenamientoService entrenamientoService;

    @GetMapping
    public ResponseEntity<List<Entrenamiento>> listar(@RequestParam(required = false) String grupoId) {
        return ResponseEntity.ok(entrenamientoService.listar(grupoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrenamiento> buscar(@PathVariable String id) {
        return ResponseEntity.ok(entrenamientoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Entrenamiento> crear(@RequestBody Entrenamiento entrenamiento) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entrenamientoService.crear(entrenamiento));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entrenamiento> actualizar(@PathVariable String id, @RequestBody Entrenamiento entrenamiento) {
        return ResponseEntity.ok(entrenamientoService.actualizar(id, entrenamiento));
    }

    @PatchMapping("/{id}/reprogramacion")
    public ResponseEntity<Entrenamiento> reprogramar(@PathVariable String id, @RequestBody ReprogramacionDto dto) {
        return ResponseEntity.ok(entrenamientoService.reprogramar(id, dto.fechaInicio(), dto.fechaFin(), dto.usuario()));
    }

    @PatchMapping("/{id}/cancelacion")
    public ResponseEntity<Entrenamiento> cancelar(@PathVariable String id, @RequestBody MotivoDto dto) {
        return ResponseEntity.ok(entrenamientoService.cancelar(id, dto.motivo(), dto.usuario()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        entrenamientoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
