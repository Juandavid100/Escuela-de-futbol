package co.edu.soccermanager.service;

import java.time.LocalDateTime;
import java.util.List;

import co.edu.soccermanager.modelo.Entrenamiento;

/** Contrato de la capa de negocio para Entrenamiento. */
public interface IEntrenamientoService {

    List<Entrenamiento> listar(String grupoId);
    Entrenamiento buscarPorId(String id);
    Entrenamiento crear(Entrenamiento entrenamiento);
    Entrenamiento actualizar(String id, Entrenamiento entrenamiento);
    Entrenamiento reprogramar(String id, LocalDateTime inicio, LocalDateTime fin, String usuario);
    Entrenamiento cancelar(String id, String motivo, String usuario);
    void eliminar(String id);
}
