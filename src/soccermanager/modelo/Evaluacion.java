package soccermanager.modelo;

import java.time.LocalDate;

public class Evaluacion {

    private final Estudiante estudiante;
    private final Entrenador entrenador;
    private final LocalDate fecha;
    private String periodo;
    private int tecnico;
    private int tactico;
    private int fisico;
    private int actitudinal;
    private String observaciones;
    private String meta;
    private boolean visibleFamilia;

    public Evaluacion(Estudiante estudiante, Entrenador entrenador, String periodo,
                      int tecnico, int tactico, int fisico, int actitudinal) {
        this.estudiante = estudiante;
        this.entrenador = entrenador;
        this.fecha = LocalDate.now();
        this.periodo = periodo;
        setTecnico(tecnico);
        setTactico(tactico);
        setFisico(fisico);
        setActitudinal(actitudinal);
    }

    public double promedio() {
        return (tecnico + tactico + fisico + actitudinal) / 4.0;
    }

    private int validar(int valor) {
        if (valor < 1 || valor > 5) {
            throw new IllegalArgumentException("La calificación debe estar entre 1 y 5");
        }
        return valor;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public Entrenador getEntrenador() {
        return entrenador;
    }

    public LocalDate getFecha() {
        return fecha;
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
        this.tecnico = validar(tecnico);
    }

    public int getTactico() {
        return tactico;
    }

    public void setTactico(int tactico) {
        this.tactico = validar(tactico);
    }

    public int getFisico() {
        return fisico;
    }

    public void setFisico(int fisico) {
        this.fisico = validar(fisico);
    }

    public int getActitudinal() {
        return actitudinal;
    }

    public void setActitudinal(int actitudinal) {
        this.actitudinal = validar(actitudinal);
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
}
