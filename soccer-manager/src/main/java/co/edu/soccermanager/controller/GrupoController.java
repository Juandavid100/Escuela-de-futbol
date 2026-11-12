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

import co.edu.soccermanager.modelo.Estudiante;
import co.edu.soccermanager.modelo.Grupo;
import co.edu.soccermanager.service.IGrupoService;

/** Capa de presentación: recibe las peticiones HTTP de /api/grupos y delega en el servicio. */
@RestController
@RequestMapping("/api/grupos")
public class GrupoController {

    @Autowired
    private IGrupoService grupoService;

    @GetMapping
    public ResponseEntity<List<Grupo>> listar() {
        return ResponseEntity.ok(grupoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Grupo> buscar(@PathVariable String id) {
        return ResponseEntity.ok(grupoService.buscarPorId(id));
    }

    @GetMapping("/{id}/estudiantes")
    public ResponseEntity<List<Estudiante>> listarEstudiantes(@PathVariable String id) {
        return ResponseEntity.ok(grupoService.listarEstudiantes(id));
    }

    @PostMapping
    public ResponseEntity<Grupo> crear(@RequestBody Grupo grupo) {
        return ResponseEntity.status(HttpStatus.CREATED).body(grupoService.crear(grupo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Grupo> actualizar(@PathVariable String id, @RequestBody Grupo grupo) {
        return ResponseEntity.ok(grupoService.actualizar(id, grupo));
    }

    @PatchMapping("/{id}/entrenadores/{entrenadorId}")
    public ResponseEntity<Grupo> asignarEntrenador(@PathVariable String id, @PathVariable String entrenadorId) {
        return ResponseEntity.ok(grupoService.asignarEntrenador(id, entrenadorId));
    }

    @DeleteMapping("/{id}/entrenadores/{entrenadorId}")
    public ResponseEntity<Grupo> quitarEntrenador(@PathVariable String id, @PathVariable String entrenadorId) {
        return ResponseEntity.ok(grupoService.quitarEntrenador(id, entrenadorId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        grupoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
