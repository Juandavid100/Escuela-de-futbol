package co.edu.soccermanager.dto;

/** Objeto de transferencia (capa de presentación). */
public record ResumenAsistenciaDto(String estudianteId, long totalActividades, long asistidas, double porcentaje) {
}
