package co.edu.soccermanager.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "asistencias")
public class Asistencia {

    @Id
    private String id;
    private String actividadId;
    private String estudianteId;
    private EstadoAsistencia estado;
    private String registradoPor;
    private String observacion;
    private LocalDateTime fechaRegistro = LocalDateTime.now();
    private List<String> correcciones = new ArrayList<>();

    public Asistencia() {
    }

    public Asistencia(String actividadId, String estudianteId, EstadoAsistencia estado, String registradoPor) {
        this.actividadId = actividadId;
        this.estudianteId = estudianteId;
        this.estado = estado;
        this.registradoPor = registradoPor;
    }

    public void corregir(EstadoAsistencia nuevoEstado, String motivo, String usuario) {
        correcciones.add(LocalDateTime.now() + " " + usuario + ": " + estado + " -> " + nuevoEstado + ". Motivo: " + motivo);
        estado = nuevoEstado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getActividadId() {
        return actividadId;
    }

    public void setActividadId(String actividadId) {
        this.actividadId = actividadId;
    }

    public String getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(String estudianteId) {
        this.estudianteId = estudianteId;
    }

    public EstadoAsistencia getEstado() {
        return estado;
    }

    public void setEstado(EstadoAsistencia estado) {
        this.estado = estado;
    }

    public String getRegistradoPor() {
        return registradoPor;
    }

    public void setRegistradoPor(String registradoPor) {
        this.registradoPor = registradoPor;
    }

    public String getObservacion() {
        return observacion;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public List<String> getCorrecciones() {
        return correcciones;
    }

    public void setCorrecciones(List<String> correcciones) {
        this.correcciones = correcciones;
    }
}
