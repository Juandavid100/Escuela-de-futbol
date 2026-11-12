package co.edu.soccermanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.soccermanager.excepcion.ConflictoException;
import co.edu.soccermanager.excepcion.DatosInvalidosException;
import co.edu.soccermanager.excepcion.RecursoNoEncontradoException;
import co.edu.soccermanager.modelo.Entrenamiento;
import co.edu.soccermanager.modelo.EstadoActividad;
import co.edu.soccermanager.modelo.Grupo;
import co.edu.soccermanager.modelo.TipoNotificacion;
import co.edu.soccermanager.repository.AsistenciaRepository;
import co.edu.soccermanager.repository.EntrenamientoRepository;
import co.edu.soccermanager.repository.GrupoRepository;

@Service
public class EntrenamientoService implements IEntrenamientoService {

    @Autowired
    private EntrenamientoRepository entrenamientoRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private INotificacionService notificacionService;

    @Override
    public List<Entrenamiento> listar(String grupoId) {
        if (grupoId == null) {
            return entrenamientoRepository.findAll();
        }
        return entrenamientoRepository.findByGrupoId(grupoId);
    }

    @Override
    public Entrenamiento buscarPorId(String id) {
        return entrenamientoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el entrenamiento con id " + id));
    }

    @Override
    public Entrenamiento crear(Entrenamiento entrenamiento) {
        Validacion.presente(entrenamiento, "cuerpo de la petición");
        Validacion.texto(entrenamiento.getGrupoId(), "grupoId");
        Grupo grupo = grupoRepository.findById(entrenamiento.getGrupoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el grupo con id " + entrenamiento.getGrupoId()));
        validarHorario(entrenamiento, null, entrenamiento.getFechaInicio(), entrenamiento.getFechaFin());
        if (entrenamiento.getUbicacion() == null) {
            entrenamiento.setUbicacion(grupo.getUbicacion());
        }
        entrenamiento.setId(null);
        entrenamiento.setEstado(EstadoActividad.PROGRAMADO);
        entrenamiento.setCambios(new ArrayList<>());
        Entrenamiento guardado = entrenamientoRepository.save(entrenamiento);
        notificacionService.notificarGrupo(grupo.getId(), TipoNotificacion.RECORDATORIO,
                "Nuevo entrenamiento el " + guardado.getFechaInicio());
        return guardado;
    }

    @Override
    public Entrenamiento actualizar(String id, Entrenamiento datos) {
        Entrenamiento actual = buscarPorId(id);
        Validacion.presente(datos, "cuerpo de la petición");
        actual.setInstrucciones(datos.getInstrucciones());
        if (datos.getUbicacion() != null) {
            actual.setUbicacion(datos.getUbicacion());
        }
        return entrenamientoRepository.save(actual);
    }

    @Override
    public Entrenamiento reprogramar(String id, LocalDateTime inicio, LocalDateTime fin, String usuario) {
        Entrenamiento entrenamiento = buscarPorId(id);
        validarModificable(entrenamiento);
        validarHorario(entrenamiento, id, inicio, fin);
        entrenamiento.reprogramar(inicio, fin, Validacion.usuarioOSistema(usuario));
        Entrenamiento guardado = entrenamientoRepository.save(entrenamiento);
        notificacionService.notificarGrupo(guardado.getGrupoId(), TipoNotificacion.CAMBIO_ACTIVIDAD,
                "El entrenamiento fue reprogramado para el " + inicio);
        return guardado;
    }

    @Override
    public Entrenamiento cancelar(String id, String motivo, String usuario) {
        Validacion.texto(motivo, "motivo");
        Entrenamiento entrenamiento = buscarPorId(id);
        validarModificable(entrenamiento);
        entrenamiento.cancelar(motivo, Validacion.usuarioOSistema(usuario));
        Entrenamiento guardado = entrenamientoRepository.save(entrenamiento);
        notificacionService.notificarGrupo(guardado.getGrupoId(), TipoNotificacion.CAMBIO_ACTIVIDAD,
                "El entrenamiento del " + guardado.getFechaInicio() + " fue cancelado. Motivo: " + motivo);
        return guardado;
    }

    @Override
    public void eliminar(String id) {
        buscarPorId(id);
        if (asistenciaRepository.existsByActividadId(id)) {
            throw new ConflictoException("No se puede eliminar: el entrenamiento ya tiene asistencia registrada. Cancélelo");
        }
        entrenamientoRepository.deleteById(id);
    }

    private void validarModificable(Entrenamiento entrenamiento) {
        if (entrenamiento.getEstado() == EstadoActividad.CANCELADO || entrenamiento.getEstado() == EstadoActividad.REALIZADO) {
            throw new ConflictoException("El entrenamiento ya está " + entrenamiento.getEstado());
        }
    }

    private void validarHorario(Entrenamiento entrenamiento, String idPropio, LocalDateTime inicio, LocalDateTime fin) {
        Validacion.presente(inicio, "fechaInicio");
        Validacion.presente(fin, "fechaFin");
        if (!fin.isAfter(inicio)) {
            throw new DatosInvalidosException("La hora de fin debe ser posterior a la de inicio");
        }
        boolean cruce = entrenamientoRepository
                .findByGrupoIdAndFechaInicioLessThanAndFechaFinGreaterThan(entrenamiento.getGrupoId(), fin, inicio)
                .stream()
                .anyMatch(otro -> !otro.getId().equals(idPropio) && otro.getEstado() != EstadoActividad.CANCELADO);
        if (cruce) {
            throw new ConflictoException("El grupo ya tiene un entrenamiento en ese horario");
        }
    }
}
