package co.edu.soccermanager.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.edu.soccermanager.modelo.Notificacion;
import co.edu.soccermanager.service.INotificacionService;

/** Capa de presentación: recibe las peticiones HTTP de /api/notificaciones y delega en el servicio. */
@RestController
@RequestMapping("/api/notificaciones")
public class NotificacionController {

    @Autowired
    private INotificacionService notificacionService;

    @GetMapping
    public ResponseEntity<List<Notificacion>> listar(@RequestParam String personaId) {
        return ResponseEntity.ok(notificacionService.listarPorPersona(personaId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Notificacion> buscar(@PathVariable String id) {
        return ResponseEntity.ok(notificacionService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Notificacion> crear(@RequestBody Notificacion notificacion) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notificacionService.crear(notificacion));
    }

    @PatchMapping("/{id}/anulacion")
    public ResponseEntity<Notificacion> anular(@PathVariable String id) {
        return ResponseEntity.ok(notificacionService.anular(id));
    }
}
