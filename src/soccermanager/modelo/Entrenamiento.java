package soccermanager.modelo;

import java.time.LocalDateTime;
import java.util.List;

public class Entrenamiento extends Actividad {

    private Grupo grupo;
    private String instrucciones;

    public Entrenamiento(Grupo grupo, LocalDateTime fechaInicio, LocalDateTime fechaFin, Ubicacion ubicacion, String instrucciones) {
        super(fechaInicio, fechaFin, ubicacion);
        this.grupo = grupo;
        this.instrucciones = instrucciones;
    }

    @Override
    public String getTipo() {
        return "Entrenamiento " + grupo.getCodigo();
    }

    @Override
    public List<Estudiante> getParticipantes() {
        return grupo.getEstudiantes();
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }
}
