package co.edu.soccermanager.dto;

import java.time.LocalDateTime;

/** Objeto de transferencia (capa de presentación). */
public record ReprogramacionDto(LocalDateTime fechaInicio, LocalDateTime fechaFin, String usuario) {
}
