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

import co.edu.soccermanager.dto.MotivoDto;
import co.edu.soccermanager.modelo.Campeonato;
import co.edu.soccermanager.service.ICampeonatoService;

/** Capa de presentación: recibe las peticiones HTTP de /api/campeonatos y delega en el servicio. */
@RestController
@RequestMapping("/api/campeonatos")
public class CampeonatoController {

    @Autowired
    private ICampeonatoService campeonatoService;

    @GetMapping
    public ResponseEntity<List<Campeonato>> listar() {
        return ResponseEntity.ok(campeonatoService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Campeonato> buscar(@PathVariable String id) {
        return ResponseEntity.ok(campeonatoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Campeonato> crear(@RequestBody Campeonato campeonato) {
        return ResponseEntity.status(HttpStatus.CREATED).body(campeonatoService.crear(campeonato));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Campeonato> actualizar(@PathVariable String id, @RequestBody Campeonato campeonato) {
        return ResponseEntity.ok(campeonatoService.actualizar(id, campeonato));
    }

    @PatchMapping("/{id}/cancelacion")
    public ResponseEntity<Campeonato> cancelar(@PathVariable String id, @RequestBody MotivoDto dto) {
        return ResponseEntity.ok(campeonatoService.cancelar(id, dto.motivo(), dto.usuario()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        campeonatoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
