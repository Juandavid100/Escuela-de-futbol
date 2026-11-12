package co.edu.soccermanager.service;

import java.time.LocalDateTime;
import java.util.List;

import co.edu.soccermanager.modelo.Partido;

/** Contrato de la capa de negocio para Partido. */
public interface IPartidoService {

    List<Partido> listar(String campeonatoId);
    Partido buscarPorId(String id);
    Partido crear(Partido partido);
    Partido actualizar(String id, Partido partido);
    Partido reprogramar(String id, LocalDateTime inicio, LocalDateTime fin, String usuario);
    Partido cancelar(String id, String motivo, String usuario);
    Partido registrarResultado(String id, String resultado);
    void eliminar(String id);
}
