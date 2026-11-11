package soccermanager.modelo;

import java.time.LocalDateTime;

public class Convocatoria {

    private final Estudiante estudiante;
    private final Partido partido;
    private final LocalDateTime fecha;
    private EstadoConvocatoria estado;
    private String motivo;

    Convocatoria(Estudiante estudiante, Partido partido) {
        this.estudiante = estudiante;
        this.partido = partido;
        this.fecha = LocalDateTime.now();
        this.estado = EstadoConvocatoria.SIN_RESPUESTA;
    }

    public void confirmar() {
        estado = EstadoConvocatoria.CONFIRMADO;
    }

    public void informarNoAsistencia(String motivo) {
        this.motivo = motivo;
        estado = EstadoConvocatoria.NO_ASISTIRA;
    }

    public void retirar(String motivo) {
        if (motivo == null || motivo.isBlank()) {
            throw new IllegalArgumentException("Debe indicar el motivo del cambio");
        }
        this.motivo = motivo;
        estado = EstadoConvocatoria.RETIRADO;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Partido getPartido() {
        return partido;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public EstadoConvocatoria getEstado() {
        return estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
