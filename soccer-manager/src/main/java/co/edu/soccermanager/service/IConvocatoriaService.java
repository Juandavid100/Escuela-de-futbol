package co.edu.soccermanager.service;

import java.util.List;

import co.edu.soccermanager.modelo.Convocatoria;
import co.edu.soccermanager.modelo.EstadoConvocatoria;

/** Contrato de la capa de negocio para Convocatoria. */
public interface IConvocatoriaService {

    List<Convocatoria> listar(String partidoId, String estudianteId);
    Convocatoria buscarPorId(String id);
    Convocatoria convocar(Convocatoria convocatoria);
    Convocatoria responder(String id, EstadoConvocatoria respuesta, String motivo);
    Convocatoria retirar(String id, String motivo);
}
