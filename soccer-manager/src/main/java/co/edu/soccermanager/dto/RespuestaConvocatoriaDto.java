package co.edu.soccermanager.dto;

import co.edu.soccermanager.modelo.EstadoConvocatoria;

/** Objeto de transferencia (capa de presentación). */
public record RespuestaConvocatoriaDto(EstadoConvocatoria estado, String motivo) {
}
