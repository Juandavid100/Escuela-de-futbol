package co.edu.soccermanager.service;

import java.util.List;

import co.edu.soccermanager.modelo.Campeonato;

/** Contrato de la capa de negocio para Campeonato. */
public interface ICampeonatoService {

    List<Campeonato> listar();
    Campeonato buscarPorId(String id);
    Campeonato crear(Campeonato campeonato);
    Campeonato actualizar(String id, Campeonato campeonato);
    Campeonato cancelar(String id, String motivo, String usuario);
    void eliminar(String id);
}
