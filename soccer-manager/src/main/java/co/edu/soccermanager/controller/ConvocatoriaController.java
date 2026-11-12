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

import co.edu.soccermanager.dto.MotivoDto;
import co.edu.soccermanager.dto.RespuestaConvocatoriaDto;
import co.edu.soccermanager.modelo.Convocatoria;
import co.edu.soccermanager.service.IConvocatoriaService;

/** Capa de presentación: recibe las peticiones HTTP de /api/convocatorias y delega en el servicio. */
@RestController
@RequestMapping("/api/convocatorias")
public class ConvocatoriaController {

    @Autowired
    private IConvocatoriaService convocatoriaService;

    @GetMapping
    public ResponseEntity<List<Convocatoria>> listar(@RequestParam(required = false) String partidoId,
                                                     @RequestParam(required = false) String estudianteId) {
        return ResponseEntity.ok(convocatoriaService.listar(partidoId, estudianteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Convocatoria> buscar(@PathVariable String id) {
        return ResponseEntity.ok(convocatoriaService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Convocatoria> convocar(@RequestBody Convocatoria convocatoria) {
        return ResponseEntity.status(HttpStatus.CREATED).body(convocatoriaService.convocar(convocatoria));
    }

    @PatchMapping("/{id}/respuesta")
    public ResponseEntity<Convocatoria> responder(@PathVariable String id, @RequestBody RespuestaConvocatoriaDto dto) {
        return ResponseEntity.ok(convocatoriaService.responder(id, dto.estado(), dto.motivo()));
    }

    @PatchMapping("/{id}/retiro")
    public ResponseEntity<Convocatoria> retirar(@PathVariable String id, @RequestBody MotivoDto dto) {
        return ResponseEntity.ok(convocatoriaService.retirar(id, dto.motivo()));
    }
}
