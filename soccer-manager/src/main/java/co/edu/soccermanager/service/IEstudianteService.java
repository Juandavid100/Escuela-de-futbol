package co.edu.soccermanager.service;

import java.util.List;

import co.edu.soccermanager.modelo.Estudiante;

/** Contrato de la capa de negocio para Estudiante. */
public interface IEstudianteService {

    List<Estudiante> listar();
    Estudiante buscarPorId(String id);
    Estudiante crear(Estudiante estudiante);
    Estudiante actualizar(String id, Estudiante estudiante);
    Estudiante asignarGrupo(String id, String grupoId);
    Estudiante retirar(String id, String motivo, String usuario);
    void eliminar(String id);
}
