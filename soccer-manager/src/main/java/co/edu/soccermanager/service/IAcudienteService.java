package co.edu.soccermanager.service;

import java.util.List;

import co.edu.soccermanager.modelo.Acudiente;
import co.edu.soccermanager.modelo.Estudiante;

/** Contrato de la capa de negocio para Acudiente. */
public interface IAcudienteService {

    List<Acudiente> listar();
    Acudiente buscarPorId(String id);
    List<Estudiante> listarEstudiantes(String id);
    Acudiente crear(Acudiente acudiente);
    Acudiente actualizar(String id, Acudiente acudiente);
    void eliminar(String id);
}
