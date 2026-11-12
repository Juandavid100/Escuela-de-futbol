package co.edu.soccermanager.modelo;

import java.time.LocalDateTime;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "partidos")
public class Partido extends Actividad {

    private String campeonatoId;
    private String rival;
    private String resultado;

    public Partido() {
    }

    public Partido(LocalDateTime fechaInicio, LocalDateTime fechaFin, Ubicacion ubicacion, String campeonatoId, String rival) {
        super(fechaInicio, fechaFin, ubicacion);
        this.campeonatoId = campeonatoId;
        this.rival = rival;
    }

    @Override
    public String getDescripcion() {
        return "Partido contra " + rival;
    }

    public String getCampeonatoId() {
        return campeonatoId;
    }

    public void setCampeonatoId(String campeonatoId) {
        this.campeonatoId = campeonatoId;
    }

    public String getRival() {
        return rival;
    }

    public void setRival(String rival) {
        this.rival = rival;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }
}
