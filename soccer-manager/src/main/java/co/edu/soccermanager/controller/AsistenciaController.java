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

import co.edu.soccermanager.dto.CorreccionAsistenciaDto;
import co.edu.soccermanager.dto.ResumenAsistenciaDto;
import co.edu.soccermanager.modelo.Asistencia;
import co.edu.soccermanager.service.IAsistenciaService;

/** Capa de presentación: recibe las peticiones HTTP de /api/asistencias y delega en el servicio. */
@RestController
@RequestMapping("/api/asistencias")
public class AsistenciaController {

    @Autowired
    private IAsistenciaService asistenciaService;

    @GetMapping
    public ResponseEntity<List<Asistencia>> listar(@RequestParam(required = false) String actividadId,
                                                   @RequestParam(required = false) String estudianteId) {
        return ResponseEntity.ok(asistenciaService.listar(actividadId, estudianteId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Asistencia> buscar(@PathVariable String id) {
        return ResponseEntity.ok(asistenciaService.buscarPorId(id));
    }

    @GetMapping("/resumen/{estudianteId}")
    public ResponseEntity<ResumenAsistenciaDto> resumen(@PathVariable String estudianteId) {
        return ResponseEntity.ok(asistenciaService.resumen(estudianteId));
    }

    @PostMapping
    public ResponseEntity<Asistencia> registrar(@RequestBody Asistencia asistencia) {
        return ResponseEntity.status(HttpStatus.CREATED).body(asistenciaService.registrar(asistencia));
    }

    @PatchMapping("/{id}/correccion")
    public ResponseEntity<Asistencia> corregir(@PathVariable String id, @RequestBody CorreccionAsistenciaDto dto) {
        return ResponseEntity.ok(asistenciaService.corregir(id, dto.estado(), dto.motivo(), dto.usuario()));
    }
}
