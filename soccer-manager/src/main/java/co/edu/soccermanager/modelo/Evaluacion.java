package co.edu.soccermanager.modelo;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "evaluaciones")
public class Evaluacion {

    @Id
    private String id;
    private String estudianteId;
    private String entrenadorId;
    private String periodo;
    private int tecnico;
    private int tactico;
    private int fisico;
    private int actitudinal;
    private String observaciones;
    private String meta;
    private boolean visibleFamilia;
    private LocalDate fecha = LocalDate.now();

    public Evaluacion() {
    }

    public Evaluacion(String estudianteId, String entrenadorId, String periodo, int tecnico, int tactico, int fisico, int actitudinal) {
        this.estudianteId = estudianteId;
        this.entrenadorId = entrenadorId;
        this.periodo = periodo;
        this.tecnico = tecnico;
        this.tactico = tactico;
        this.fisico = fisico;
        this.actitudinal = actitudinal;
    }

    public boolean calificacionesValidas() {
        return enRango(tecnico) && enRango(tactico) && enRango(fisico) && enRango(actitudinal);
    }

    private boolean enRango(int valor) {
        return valor >= 1 && valor <= 5;
    }

    public double getPromedio() {
        return (tecnico + tactico + fisico + actitudinal) / 4.0;
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

    public String getEntrenadorId() {
        return entrenadorId;
    }

    public void setEntrenadorId(String entrenadorId) {
        this.entrenadorId = entrenadorId;
    }

    public String getPeriodo() {
        return periodo;
    }

    public void setPeriodo(String periodo) {
        this.periodo = periodo;
    }

    public int getTecnico() {
        return tecnico;
    }

    public void setTecnico(int tecnico) {
        this.tecnico = tecnico;
    }

    public int getTactico() {
        return tactico;
    }

    public void setTactico(int tactico) {
        this.tactico = tactico;
    }

    public int getFisico() {
        return fisico;
    }

    public void setFisico(int fisico) {
        this.fisico = fisico;
    }

    public int getActitudinal() {
        return actitudinal;
    }

    public void setActitudinal(int actitudinal) {
        this.actitudinal = actitudinal;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

    public boolean isVisibleFamilia() {
        return visibleFamilia;
    }

    public void setVisibleFamilia(boolean visibleFamilia) {
        this.visibleFamilia = visibleFamilia;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }
}
