package co.edu.soccermanager.modelo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "mensualidades")
public class Mensualidad {

    @Id
    private String id;
    private String estudianteId;
    private String periodo;
    private double valor;
    private double porcentajeDescuento;
    private LocalDate fechaVencimiento;
    private List<Pago> pagos = new ArrayList<>();

    public Mensualidad() {
    }

    public Mensualidad(String estudianteId, String periodo, double valor, double porcentajeDescuento, LocalDate fechaVencimiento) {
        this.estudianteId = estudianteId;
        this.periodo = periodo;
        this.valor = valor;
        this.porcentajeDescuento = porcentajeDescuento;
        this.fechaVencimiento = fechaVencimiento;
    }

    public double getValorAPagar() {
        return valor - valor * porcentajeDescuento / 100;
    }

    public double getTotalPagado() {
        double total = 0;
        for (Pago pago : pagos) {
            if (!pago.isAnulado()) {
                total = total + pago.getValor();
            }
        }
        return total;
    }

    public double getSaldo() {
        return Math.max(0, getValorAPagar() - getTotalPagado());
    }

    public EstadoMensualidad getEstado() {
        if (getSaldo() == 0) {
            return EstadoMensualidad.PAGADO;
        }
        if (LocalDate.now().isAfter(fechaVencimiento)) {
            return EstadoMensualidad.VENCIDO;
        }
        if (getTotalPagado() > 0) {
            return EstadoMensualidad.PARCIAL;
        }
        return EstadoMensualidad.PENDIENTE;
    }

    public Pago registrarPago(double monto, MetodoPago metodo, String comprobante) {
        Pago pago = new Pago(monto, metodo, comprobante);
        pago.setValidado(metodo == MetodoPago.PASARELA);
        pagos.add(pago);
        return pago;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(String estudianteId) {
        this.estudianteId = estudianteId;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
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
        return pagos;
    }

    public void setPagos(List<Pago> pagos) {
        this.pagos = pagos;
    }
}
