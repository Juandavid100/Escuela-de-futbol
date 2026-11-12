package co.edu.soccermanager.modelo;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "entrenamientos")
public class Entrenamiento extends Actividad {

    private String grupoId;
    private String instrucciones;

    public Entrenamiento() {
    }

    public Entrenamiento(LocalDateTime fechaInicio, LocalDateTime fechaFin, Ubicacion ubicacion, String grupoId, String instrucciones) {
        super(fechaInicio, fechaFin, ubicacion);
        this.grupoId = grupoId;
        this.instrucciones = instrucciones;
    }

    @Override
    public String getDescripcion() {
        return "Entrenamiento del grupo " + grupoId;
    }

    public String getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(String grupoId) {
        this.grupoId = grupoId;
    }

    public String getInstrucciones() {
        return instrucciones;
    }

    public void setInstrucciones(String instrucciones) {
        this.instrucciones = instrucciones;
    }
}
