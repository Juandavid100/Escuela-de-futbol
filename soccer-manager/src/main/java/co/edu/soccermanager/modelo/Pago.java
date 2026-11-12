package co.edu.soccermanager.modelo;

import java.time.LocalDateTime;

public class Pago {

    private double valor;
    private MetodoPago metodo;
    private String comprobante;
    private LocalDateTime fecha = LocalDateTime.now();
    private boolean validado;
    private boolean anulado;
    private String motivoAnulacion;

    public Pago() {
    }

    public Pago(double valor, MetodoPago metodo, String comprobante) {
        this.valor = valor;
        this.metodo = metodo;
        this.comprobante = comprobante;
    }

    public void anular(String motivo) {
        anulado = true;
        motivoAnulacion = motivo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public MetodoPago getMetodo() {
        return metodo;
    }

    public void setMetodo(MetodoPago metodo) {
        this.metodo = metodo;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public boolean isValidado() {
        return validado;
    }

    public void setValidado(boolean validado) {
        this.validado = validado;
    }

    public boolean isAnulado() {
        return anulado;
    }

    public void setAnulado(boolean anulado) {
        this.anulado = anulado;
    }

    public String getMotivoAnulacion() {
        return motivoAnulacion;
    }

    public void setMotivoAnulacion(String motivoAnulacion) {
        this.motivoAnulacion = motivoAnulacion;
    }
}
