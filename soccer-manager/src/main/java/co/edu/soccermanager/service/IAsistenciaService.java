package co.edu.soccermanager.service;

import java.util.List;

import co.edu.soccermanager.dto.ResumenAsistenciaDto;
import co.edu.soccermanager.modelo.Asistencia;
import co.edu.soccermanager.modelo.EstadoAsistencia;

/** Contrato de la capa de negocio para Asistencia. */
public interface IAsistenciaService {

    List<Asistencia> listar(String actividadId, String estudianteId);
    Asistencia buscarPorId(String id);
    Asistencia registrar(Asistencia asistencia);
    Asistencia corregir(String id, EstadoAsistencia estado, String motivo, String usuario);
    ResumenAsistenciaDto resumen(String estudianteId);
}
