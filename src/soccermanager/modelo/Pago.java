package soccermanager.modelo;

import java.time.LocalDateTime;

public class Pago {

    private final Mensualidad mensualidad;
    private final double valor;
    private final MetodoPago metodo;
    private final LocalDateTime fecha;
    private String comprobante;
    private boolean validado;
    private boolean anulado;
    private String motivoAnulacion;

    Pago(Mensualidad mensualidad, double valor, MetodoPago metodo, String comprobante) {
        this.mensualidad = mensualidad;
        this.valor = valor;
        this.metodo = metodo;
        this.comprobante = comprobante;
        this.fecha = LocalDateTime.now();
        this.validado = metodo == MetodoPago.PASARELA;
    }

    public void validar() {
        validado = true;
    }

    public void anular(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el motivo de la anulación");
        }
        anulado = true;
        motivoAnulacion = motivo;
    }

    public Mensualidad getMensualidad() {
        return mensualidad;
    }

    public double getValor() {
        return valor;
    }

    public MetodoPago getMetodo() {
        return metodo;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public boolean isValidado() {
        return validado;
    }

    public boolean isAnulado() {
        return anulado;
    }

    public String getMotivoAnulacion() {
        return motivoAnulacion;
    }
}
