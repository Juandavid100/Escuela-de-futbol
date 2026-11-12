package co.edu.soccermanager.service;

import java.util.List;

import co.edu.soccermanager.modelo.Entrenador;

/** Contrato de la capa de negocio para Entrenador. */
public interface IEntrenadorService {

    List<Entrenador> listar();
    Entrenador buscarPorId(String id);
    Entrenador crear(Entrenador entrenador);
    Entrenador actualizar(String id, Entrenador entrenador);
    Entrenador cambiarPermisos(String id, boolean puedeConvocar, boolean puedeModificarActividades);
    void eliminar(String id);
}
