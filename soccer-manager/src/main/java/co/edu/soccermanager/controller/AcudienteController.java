package co.edu.soccermanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.edu.soccermanager.modelo.Acudiente;
import co.edu.soccermanager.modelo.Estudiante;
import co.edu.soccermanager.service.IAcudienteService;

/** Capa de presentación: recibe las peticiones HTTP de /api/acudientes y delega en el servicio. */
@RestController
@RequestMapping("/api/acudientes")
public class AcudienteController {

    @Autowired
    private IAcudienteService acudienteService;

    @GetMapping
    public ResponseEntity<List<Acudiente>> listar() {
        return ResponseEntity.ok(acudienteService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Acudiente> buscar(@PathVariable String id) {
        return ResponseEntity.ok(acudienteService.buscarPorId(id));
    }

    @GetMapping("/{id}/estudiantes")
    public ResponseEntity<List<Estudiante>> listarEstudiantes(@PathVariable String id) {
        return ResponseEntity.ok(acudienteService.listarEstudiantes(id));
    }

    @PostMapping
    public ResponseEntity<Acudiente> crear(@RequestBody Acudiente acudiente) {
        return ResponseEntity.status(HttpStatus.CREATED).body(acudienteService.crear(acudiente));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Acudiente> actualizar(@PathVariable String id, @RequestBody Acudiente acudiente) {
        return ResponseEntity.ok(acudienteService.actualizar(id, acudiente));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        acudienteService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
