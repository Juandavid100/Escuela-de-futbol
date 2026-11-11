package soccermanager.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Asistencia {

    private final Estudiante estudiante;
    private final Actividad actividad;
    private EstadoAsistencia estado;
    private String observacion;
    private final String registradoPor;
    private final LocalDateTime fechaRegistro;
    private final List<String> correcciones = new ArrayList<>();

    public Asistencia(Estudiante estudiante, Actividad actividad, EstadoAsistencia estado, String registradoPor) {
        this.estudiante = estudiante;
        this.actividad = actividad;
        this.estado = estado;
        this.registradoPor = registradoPor;
        this.fechaRegistro = LocalDateTime.now();
    }

    public void corregir(EstadoAsistencia nuevoEstado, String motivo, String usuario) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("El motivo es obligatorio para corregir");
        }
        correcciones.add(LocalDateTime.now() + " " + usuario + ": " + estado + " -> " + nuevoEstado + ". Motivo: " + motivo);
        estado = nuevoEstado;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Actividad getActividad() {
        return actividad;
    }

    public EstadoAsistencia getEstado() {
        return estado;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getRegistradoPor() {
        return registradoPor;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public List<String> getCorrecciones() {
        return Collections.unmodifiableList(correcciones);
    }
}
