package co.edu.soccermanager.service;

import java.util.List;

import co.edu.soccermanager.modelo.Evaluacion;

/** Contrato de la capa de negocio para Evaluacion. */
public interface IEvaluacionService {

    List<Evaluacion> listar(String estudianteId, boolean soloVisibles);
    Evaluacion buscarPorId(String id);
    Evaluacion crear(Evaluacion evaluacion);
    Evaluacion actualizar(String id, Evaluacion evaluacion);
    Evaluacion publicar(String id);
    void eliminar(String id);
}
