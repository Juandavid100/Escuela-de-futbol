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
import co.edu.soccermanager.dto.ResultadoDto;
import co.edu.soccermanager.modelo.Partido;
import co.edu.soccermanager.service.IPartidoService;

/** Capa de presentación: recibe las peticiones HTTP de /api/partidos y delega en el servicio. */
@RestController
@RequestMapping("/api/partidos")
public class PartidoController {

    @Autowired
    private IPartidoService partidoService;

    @GetMapping
    public ResponseEntity<List<Partido>> listar(@RequestParam(required = false) String campeonatoId) {
        return ResponseEntity.ok(partidoService.listar(campeonatoId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Partido> buscar(@PathVariable String id) {
        return ResponseEntity.ok(partidoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Partido> crear(@RequestBody Partido partido) {
        return ResponseEntity.status(HttpStatus.CREATED).body(partidoService.crear(partido));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Partido> actualizar(@PathVariable String id, @RequestBody Partido partido) {
        return ResponseEntity.ok(partidoService.actualizar(id, partido));
    }

    @PatchMapping("/{id}/reprogramacion")
    public ResponseEntity<Partido> reprogramar(@PathVariable String id, @RequestBody ReprogramacionDto dto) {
        return ResponseEntity.ok(partidoService.reprogramar(id, dto.fechaInicio(), dto.fechaFin(), dto.usuario()));
    }

    @PatchMapping("/{id}/cancelacion")
    public ResponseEntity<Partido> cancelar(@PathVariable String id, @RequestBody MotivoDto dto) {
        return ResponseEntity.ok(partidoService.cancelar(id, dto.motivo(), dto.usuario()));
    }

    @PatchMapping("/{id}/resultado")
    public ResponseEntity<Partido> registrarResultado(@PathVariable String id, @RequestBody ResultadoDto dto) {
        return ResponseEntity.ok(partidoService.registrarResultado(id, dto.resultado()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        partidoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
