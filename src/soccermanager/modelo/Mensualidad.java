package soccermanager.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mensualidad {

    private final Estudiante estudiante;
    private final String periodo;
    private double valor;
    private double porcentajeDescuento;
    private LocalDate fechaVencimiento;
    private final List<Pago> pagos = new ArrayList<>();

    public Mensualidad(Estudiante estudiante, String periodo, double valor, double porcentajeDescuento, LocalDate fechaVencimiento) {
        this.estudiante = estudiante;
        this.periodo = periodo;
        this.valor = valor;
        this.porcentajeDescuento = porcentajeDescuento;
        this.fechaVencimiento = fechaVencimiento;
    }

    public double valorAPagar() {
        return valor - valor * porcentajeDescuento / 100;
    }

    public double totalPagado() {
        double total = 0;
        for (Pago p : pagos) {
            if (!p.isAnulado()) {
                total = total + p.getValor();
            }
        }
        return total;
    }

    public double calcularSaldo() {
        return Math.max(0, valorAPagar() - totalPagado());
    }

    public EstadoMensualidad calcularEstado(LocalDate hoy) {
        if (calcularSaldo() == 0) {
            return EstadoMensualidad.PAGADO;
        }
        if (hoy.isAfter(fechaVencimiento)) {
            return EstadoMensualidad.VENCIDO;
        }
        if (totalPagado() > 0) {
            return EstadoMensualidad.PARCIAL;
        }
        return EstadoMensualidad.PENDIENTE;
    }

    public Pago registrarPago(double monto, MetodoPago metodo, String comprobante) {
        if (monto <= 0) {
            throw new IllegalArgumentException("El valor del pago debe ser mayor que cero");
        }
        if (monto > calcularSaldo()) {
            throw new IllegalArgumentException("El pago supera el saldo pendiente");
        }
        Pago pago = new Pago(this, monto, metodo, comprobante);
        pagos.add(pago);
        return pago;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public String getPeriodo() {
        return periodo;
    }

    public double getValor() {
        return valor;
    }

    public void setValor(double valor) {
        this.valor = valor;
    }

    public double getPorcentajeDescuento() {
        return porcentajeDescuento;
    }

    public void setPorcentajeDescuento(double porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public List<Pago> getPagos() {
        return Collections.unmodifiableList(pagos);
    }
}
