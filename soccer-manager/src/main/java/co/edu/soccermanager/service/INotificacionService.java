package co.edu.soccermanager.service;

import java.util.List;

import co.edu.soccermanager.modelo.Estudiante;
import co.edu.soccermanager.modelo.Notificacion;
import co.edu.soccermanager.modelo.TipoNotificacion;

/** Contrato de la capa de negocio para Notificacion. */
public interface INotificacionService {

    List<Notificacion> listarPorPersona(String personaId);
    Notificacion buscarPorId(String id);
    Notificacion crear(Notificacion notificacion);
    Notificacion anular(String id);
    void notificar(String personaId, TipoNotificacion tipo, String mensaje);
    void notificarEstudianteYAcudientes(Estudiante estudiante, TipoNotificacion tipo, String mensaje);
    void notificarGrupo(String grupoId, TipoNotificacion tipo, String mensaje);
}
