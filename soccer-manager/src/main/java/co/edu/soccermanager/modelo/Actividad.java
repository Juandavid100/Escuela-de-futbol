package co.edu.soccermanager.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;

public abstract class Actividad {

    @Id
    private String id;
    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Ubicacion ubicacion;
    private EstadoActividad estado = EstadoActividad.PROGRAMADO;
    private List<String> cambios = new ArrayList<>();

    protected Actividad() {
    }

    protected Actividad(LocalDateTime fechaInicio, LocalDateTime fechaFin, Ubicacion ubicacion) {
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.ubicacion = ubicacion;
    }

    public boolean horarioValido() {
        return fechaInicio != null && fechaFin != null && fechaFin.isAfter(fechaInicio);
    }

    public void reprogramar(LocalDateTime nuevoInicio, LocalDateTime nuevoFin, String usuario) {
        cambios.add(usuario + ": " + fechaInicio + " -> " + nuevoInicio);
        fechaInicio = nuevoInicio;
        fechaFin = nuevoFin;
        estado = EstadoActividad.REPROGRAMADO;
    }

    public void cancelar(String motivo, String usuario) {
        cambios.add(usuario + ": cancelada. Motivo: " + motivo);
        estado = EstadoActividad.CANCELADO;
    }

    public abstract String getDescripcion();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDateTime fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDateTime getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDateTime fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public EstadoActividad getEstado() {
        return estado;
    }

    public void setEstado(EstadoActividad estado) {
        this.estado = estado;
    }

    public List<String> getCambios() {
        return cambios;
    }

    public void setCambios(List<String> cambios) {
        this.cambios = cambios;
    }
}
