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
import org.springframework.web.bind.annotation.RestController;

import co.edu.soccermanager.dto.PermisosEntrenadorDto;
import co.edu.soccermanager.modelo.Entrenador;
import co.edu.soccermanager.service.IEntrenadorService;

/** Capa de presentación: recibe las peticiones HTTP de /api/entrenadores y delega en el servicio. */
@RestController
@RequestMapping("/api/entrenadores")
public class EntrenadorController {

    @Autowired
    private IEntrenadorService entrenadorService;

    @GetMapping
    public ResponseEntity<List<Entrenador>> listar() {
        return ResponseEntity.ok(entrenadorService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Entrenador> buscar(@PathVariable String id) {
        return ResponseEntity.ok(entrenadorService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Entrenador> crear(@RequestBody Entrenador entrenador) {
        return ResponseEntity.status(HttpStatus.CREATED).body(entrenadorService.crear(entrenador));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Entrenador> actualizar(@PathVariable String id, @RequestBody Entrenador entrenador) {
        return ResponseEntity.ok(entrenadorService.actualizar(id, entrenador));
    }

    @PatchMapping("/{id}/permisos")
    public ResponseEntity<Entrenador> cambiarPermisos(@PathVariable String id, @RequestBody PermisosEntrenadorDto dto) {
        return ResponseEntity.ok(entrenadorService.cambiarPermisos(id, dto.puedeConvocar(), dto.puedeModificarActividades()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        entrenadorService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
