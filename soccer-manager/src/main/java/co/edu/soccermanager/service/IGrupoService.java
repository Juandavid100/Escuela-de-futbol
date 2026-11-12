package co.edu.soccermanager.service;

import java.util.List;

import co.edu.soccermanager.modelo.Estudiante;
import co.edu.soccermanager.modelo.Grupo;

/** Contrato de la capa de negocio para Grupo. */
public interface IGrupoService {

    List<Grupo> listar();
    Grupo buscarPorId(String id);
    List<Estudiante> listarEstudiantes(String id);
    Grupo crear(Grupo grupo);
    Grupo actualizar(String id, Grupo grupo);
    Grupo asignarEntrenador(String id, String entrenadorId);
    Grupo quitarEntrenador(String id, String entrenadorId);
    void eliminar(String id);
}
