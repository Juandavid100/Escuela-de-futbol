package co.edu.soccermanager.dto;

import co.edu.soccermanager.modelo.MetodoPago;

/** Objeto de transferencia (capa de presentación). */
public record PagoDto(double valor, MetodoPago metodo, String comprobante) {
}
