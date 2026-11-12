package co.edu.soccermanager.service;

import java.util.List;

import co.edu.soccermanager.modelo.Categoria;

/** Contrato de la capa de negocio para Categoria. */
public interface ICategoriaService {

    List<Categoria> listar();
    Categoria buscarPorId(String id);
    Categoria crear(Categoria categoria);
    Categoria actualizar(String id, Categoria categoria);
    void eliminar(String id);
}
