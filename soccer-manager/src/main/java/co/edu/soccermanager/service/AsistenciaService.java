package co.edu.soccermanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.soccermanager.dto.ResumenAsistenciaDto;
import co.edu.soccermanager.excepcion.ConflictoException;
import co.edu.soccermanager.excepcion.DatosInvalidosException;
import co.edu.soccermanager.excepcion.RecursoNoEncontradoException;
import co.edu.soccermanager.modelo.Actividad;
import co.edu.soccermanager.modelo.Asistencia;
import co.edu.soccermanager.modelo.Entrenamiento;
import co.edu.soccermanager.modelo.EstadoActividad;
import co.edu.soccermanager.modelo.EstadoAsistencia;
import co.edu.soccermanager.modelo.Estudiante;
import co.edu.soccermanager.repository.AsistenciaRepository;
import co.edu.soccermanager.repository.EntrenamientoRepository;
import co.edu.soccermanager.repository.EstudianteRepository;
import co.edu.soccermanager.repository.PartidoRepository;

@Service
public class AsistenciaService implements IAsistenciaService {

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private EntrenamientoRepository entrenamientoRepository;

    @Autowired
    private PartidoRepository partidoRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Override
    public List<Asistencia> listar(String actividadId, String estudianteId) {
        if (actividadId != null) {
            return asistenciaRepository.findByActividadId(actividadId);
        }
        if (estudianteId != null) {
            return asistenciaRepository.findByEstudianteId(estudianteId);
        }
        return asistenciaRepository.findAll();
    }

    @Override
    public Asistencia buscarPorId(String id) {
        return asistenciaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el registro de asistencia con id " + id));
    }

    @Override
    public Asistencia registrar(Asistencia asistencia) {
        Validacion.presente(asistencia, "cuerpo de la petición");
        Validacion.texto(asistencia.getActividadId(), "actividadId");
        Validacion.texto(asistencia.getEstudianteId(), "estudianteId");
        Validacion.presente(asistencia.getEstado(), "estado");
        Validacion.texto(asistencia.getRegistradoPor(), "registradoPor");
        Actividad actividad = buscarActividad(asistencia.getActividadId());
        if (actividad.getEstado() == EstadoActividad.CANCELADO) {
            throw new ConflictoException("No se registra asistencia de una actividad cancelada");
        }
        if (actividad.getFechaInicio().isAfter(LocalDateTime.now())) {
            throw new DatosInvalidosException("No se puede registrar asistencia de una actividad que no ha empezado");
        }
        Estudiante estudiante = estudianteRepository.findById(asistencia.getEstudianteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el estudiante con id " + asistencia.getEstudianteId()));
        if (actividad instanceof Entrenamiento entrenamiento && !entrenamiento.getGrupoId().equals(estudiante.getGrupoId())) {
            throw new DatosInvalidosException("El estudiante no pertenece al grupo del entrenamiento");
        }
        if (asistenciaRepository.existsByActividadIdAndEstudianteId(actividad.getId(), estudiante.getId())) {
            throw new ConflictoException("La asistencia de este estudiante ya fue registrada. Use la corrección");
        }
        asistencia.setId(null);
        asistencia.setFechaRegistro(LocalDateTime.now());
        asistencia.setCorrecciones(new ArrayList<>());
        return asistenciaRepository.save(asistencia);
    }

    @Override
    public Asistencia corregir(String id, EstadoAsistencia estado, String motivo, String usuario) {
        Validacion.presente(estado, "estado");
        Validacion.texto(motivo, "motivo");
        Asistencia asistencia = buscarPorId(id);
        if (asistencia.getEstado() == estado) {
            throw new DatosInvalidosException("La asistencia ya tiene el estado " + estado);
        }
        asistencia.corregir(estado, motivo, Validacion.usuarioOSistema(usuario));
        return asistenciaRepository.save(asistencia);
    }

    @Override
    public ResumenAsistenciaDto resumen(String estudianteId) {
        if (!estudianteRepository.existsById(estudianteId)) {
            throw new RecursoNoEncontradoException("No existe el estudiante con id " + estudianteId);
        }
        List<Asistencia> registros = asistenciaRepository.findByEstudianteId(estudianteId);
        long asistidas = registros.stream()
                .filter(a -> a.getEstado() == EstadoAsistencia.PRESENTE || a.getEstado() == EstadoAsistencia.TARDE)
                .count();
        double porcentaje = registros.isEmpty() ? 0 : Math.round(asistidas * 1000.0 / registros.size()) / 10.0;
        return new ResumenAsistenciaDto(estudianteId, registros.size(), asistidas, porcentaje);
    }

    /** Polimorfismo: la asistencia funciona igual para entrenamientos y partidos. */
    private Actividad buscarActividad(String actividadId) {
        Optional<Actividad> entrenamiento = entrenamientoRepository.findById(actividadId).map(e -> (Actividad) e);
        return entrenamiento
                .or(() -> partidoRepository.findById(actividadId).map(p -> (Actividad) p))
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la actividad con id " + actividadId));
    }
}
