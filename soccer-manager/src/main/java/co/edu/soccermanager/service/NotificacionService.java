package co.edu.soccermanager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.soccermanager.excepcion.ConflictoException;
import co.edu.soccermanager.excepcion.RecursoNoEncontradoException;
import co.edu.soccermanager.modelo.EstadoEstudiante;
import co.edu.soccermanager.modelo.Estudiante;
import co.edu.soccermanager.modelo.Notificacion;
import co.edu.soccermanager.modelo.TipoNotificacion;
import co.edu.soccermanager.repository.EstudianteRepository;
import co.edu.soccermanager.repository.NotificacionRepository;

/**
 * Fabricación pura + Indirección: los demás servicios no envían mensajes
 * directamente, se lo piden a este servicio.
 */
@Service
public class NotificacionService implements INotificacionService {

    @Autowired
    private NotificacionRepository notificacionRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Override
    public List<Notificacion> listarPorPersona(String personaId) {
        Validacion.texto(personaId, "personaId");
        return notificacionRepository.findByPersonaIdOrderByFechaEnvioDesc(personaId);
    }

    @Override
    public Notificacion buscarPorId(String id) {
        return notificacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la notificación con id " + id));
    }

    @Override
    public Notificacion crear(Notificacion notificacion) {
        Validacion.presente(notificacion, "cuerpo de la petición");
        Validacion.texto(notificacion.getPersonaId(), "personaId");
        Validacion.presente(notificacion.getTipo(), "tipo");
        Validacion.texto(notificacion.getMensaje(), "mensaje");
        notificacion.setId(null);
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setAnulada(false);
        return notificacionRepository.save(notificacion);
    }

    @Override
    public Notificacion anular(String id) {
        Notificacion notificacion = buscarPorId(id);
        if (notificacion.isAnulada()) {
            throw new ConflictoException("La notificación ya estaba anulada");
        }
        notificacion.setAnulada(true);
        return notificacionRepository.save(notificacion);
    }

    @Override
    public void notificar(String personaId, TipoNotificacion tipo, String mensaje) {
        notificacionRepository.save(new Notificacion(personaId, tipo, mensaje));
    }

    @Override
    public void notificarEstudianteYAcudientes(Estudiante estudiante, TipoNotificacion tipo, String mensaje) {
        notificar(estudiante.getId(), tipo, mensaje);
        for (String acudienteId : estudiante.getAcudienteIds()) {
            notificar(acudienteId, tipo, estudiante.getNombreCompleto() + ": " + mensaje);
        }
    }

    @Override
    public void notificarGrupo(String grupoId, TipoNotificacion tipo, String mensaje) {
        for (Estudiante estudiante : estudianteRepository.findByGrupoIdAndEstado(grupoId, EstadoEstudiante.ACTIVO)) {
            notificarEstudianteYAcudientes(estudiante, tipo, mensaje);
        }
    }
}
