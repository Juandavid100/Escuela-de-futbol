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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.soccermanager.dto.MotivoDto;
import co.edu.soccermanager.dto.PagoDto;
import co.edu.soccermanager.modelo.Mensualidad;
import co.edu.soccermanager.service.IMensualidadService;

/** Capa de presentación: recibe las peticiones HTTP de /api/mensualidades y delega en el servicio. */
@RestController
@RequestMapping("/api/mensualidades")
public class MensualidadController {

    @Autowired
    private IMensualidadService mensualidadService;

    @GetMapping
    public ResponseEntity<List<Mensualidad>> listar(@RequestParam(required = false) String estudianteId) {
        return ResponseEntity.ok(mensualidadService.listar(estudianteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Mensualidad> buscar(@PathVariable String id) {
        return ResponseEntity.ok(mensualidadService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Mensualidad> crear(@RequestBody Mensualidad mensualidad) {
        return ResponseEntity.status(HttpStatus.CREATED).body(mensualidadService.crear(mensualidad));
    }

    @PostMapping("/{id}/pagos")
    public ResponseEntity<Mensualidad> registrarPago(@PathVariable String id, @RequestBody PagoDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(mensualidadService.registrarPago(id, dto.valor(), dto.metodo(), dto.comprobante()));
    }

    @PatchMapping("/{id}/pagos/{indice}/anulacion")
    public ResponseEntity<Mensualidad> anularPago(@PathVariable String id, @PathVariable int indice, @RequestBody MotivoDto dto) {
        return ResponseEntity.ok(mensualidadService.anularPago(id, indice, dto.motivo()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable String id) {
        mensualidadService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
