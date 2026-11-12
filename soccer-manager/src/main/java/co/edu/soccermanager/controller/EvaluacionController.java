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

import co.edu.soccermanager.modelo.Evaluacion;
import co.edu.soccermanager.service.IEvaluacionService;

/** Capa de presentación: recibe las peticiones HTTP de /api/evaluaciones y delega en el servicio. */
@RestController
@RequestMapping("/api/evaluaciones")
public class EvaluacionController {

    @Autowired
    private IEvaluacionService evaluacionService;

    @GetMapping
    public ResponseEntity<List<Evaluacion>> listar(@RequestParam(required = false) String estudianteId,
                                                   @RequestParam(defaultValue = "false") boolean soloVisibles) {
        return ResponseEntity.ok(evaluacionService.listar(estudianteId, soloVisibles));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Evaluacion> buscar(@PathVariable String id) {
        return ResponseEntity.ok(evaluacionService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Evaluacion> crear(@RequestBody Evaluacion evaluacion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(evaluacionService.crear(evaluacion));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Evaluacion> actualizar(@PathVariable String id, @RequestBody Evaluacion evaluacion) {
        return ResponseEntity.ok(evaluacionService.actualizar(id, evaluacion));
    }

    @PatchMapping("/{id}/publicacion")
    public ResponseEntity<Evaluacion> publicar(@PathVariable String id) {
        return ResponseEntity.ok(evaluacionService.publicar(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        evaluacionService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
