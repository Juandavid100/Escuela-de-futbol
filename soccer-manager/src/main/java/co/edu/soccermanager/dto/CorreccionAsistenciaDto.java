package co.edu.soccermanager.dto;

import co.edu.soccermanager.modelo.EstadoAsistencia;

/** Objeto de transferencia (capa de presentación). */
public record CorreccionAsistenciaDto(EstadoAsistencia estado, String motivo, String usuario) {
}
