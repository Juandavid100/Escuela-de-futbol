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

import co.edu.soccermanager.dto.AsignacionGrupoDto;
import co.edu.soccermanager.dto.MotivoDto;
import co.edu.soccermanager.modelo.Estudiante;
import co.edu.soccermanager.service.IEstudianteService;

/** Capa de presentación: recibe las peticiones HTTP de /api/estudiantes y delega en el servicio. */
@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private IEstudianteService estudianteService;

    @GetMapping
    public ResponseEntity<List<Estudiante>> listar() {
        return ResponseEntity.ok(estudianteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> buscar(@PathVariable String id) {
        return ResponseEntity.ok(estudianteService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Estudiante> crear(@RequestBody Estudiante estudiante) {
        return ResponseEntity.status(HttpStatus.CREATED).body(estudianteService.crear(estudiante));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Estudiante> actualizar(@PathVariable String id, @RequestBody Estudiante estudiante) {
        return ResponseEntity.ok(estudianteService.actualizar(id, estudiante));
    }

    @PatchMapping("/{id}/grupo")
    public ResponseEntity<Estudiante> asignarGrupo(@PathVariable String id, @RequestBody AsignacionGrupoDto dto) {
        return ResponseEntity.ok(estudianteService.asignarGrupo(id, dto.grupoId()));
    }

    @PatchMapping("/{id}/retiro")
    public ResponseEntity<Estudiante> retirar(@PathVariable String id, @RequestBody MotivoDto dto) {
        return ResponseEntity.ok(estudianteService.retirar(id, dto.motivo(), dto.usuario()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        estudianteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
