package soccermanager.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Actividad {

    private LocalDateTime fechaInicio;
    private LocalDateTime fechaFin;
    private Ubicacion ubicacion;
    private EstadoActividad estado;
    private final List<String> cambios = new ArrayList<>();

    protected Actividad(LocalDateTime fechaInicio, LocalDateTime fechaFin, Ubicacion ubicacion) {
        if (!fechaFin.isAfter(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio");
        }
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.ubicacion = ubicacion;
        this.estado = EstadoActividad.PROGRAMADO;
    }

    public abstract String getTipo();

    public abstract List<Estudiante> getParticipantes();

    public void reprogramar(LocalDateTime nuevoInicio, LocalDateTime nuevoFin, String usuario) {
        if (estado == EstadoActividad.CANCELADO) {
            throw new IllegalStateException("No se puede reprogramar una actividad cancelada");
        }
        cambios.add(usuario + ": " + fechaInicio + " -> " + nuevoInicio);
        fechaInicio = nuevoInicio;
        fechaFin = nuevoFin;
        estado = EstadoActividad.REPROGRAMADO;
    }

    public void cambiarUbicacion(Ubicacion nueva, String usuario) {
        cambios.add(usuario + ": " + ubicacion.getNombre() + " -> " + nueva.getNombre());
        ubicacion = nueva;
    }

    public void cancelar(String motivo, String usuario) {
        cambios.add(usuario + ": cancelada. Motivo: " + motivo);
        estado = EstadoActividad.CANCELADO;
    }

    public void marcarRealizada() {
        estado = EstadoActividad.REALIZADO;
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
        return Collections.unmodifiableList(cambios);
    }
}
