package soccermanager.modelo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Partido extends Actividad {

    private final Campeonato campeonato;
    private String rival;
    private String resultado;
    private final List<Convocatoria> convocatorias = new ArrayList<>();

    Partido(Campeonato campeonato, String rival, LocalDateTime fechaInicio, LocalDateTime fechaFin, Ubicacion ubicacion) {
        super(fechaInicio, fechaFin, ubicacion);
        this.campeonato = campeonato;
        this.rival = rival;
    }

    public Convocatoria convocar(Estudiante estudiante) {
        if (!campeonato.getCategoria().equals(estudiante.getCategoria())) {
            throw new IllegalArgumentException("El estudiante no pertenece a la categoría del campeonato");
        }
        if (buscarConvocatoria(estudiante) != null) {
            throw new IllegalStateException("El estudiante ya está convocado");
        }
        Convocatoria convocatoria = new Convocatoria(estudiante, this);
        convocatorias.add(convocatoria);
        return convocatoria;
    }

    public Convocatoria buscarConvocatoria(Estudiante estudiante) {
        for (Convocatoria c : convocatorias) {
            if (c.getEstudiante().equals(estudiante) && c.getEstado() != EstadoConvocatoria.RETIRADO) {
                return c;
            }
        }
        return null;
    }

    @Override
    public String getTipo() {
        return "Partido " + campeonato.getNombre() + " vs. " + rival;
    }

    @Override
    public List<Estudiante> getParticipantes() {
        List<Estudiante> convocados = new ArrayList<>();
        for (Convocatoria c : convocatorias) {
            if (c.getEstado() != EstadoConvocatoria.RETIRADO) {
                convocados.add(c.getEstudiante());
            }
        }
        return convocados;
    }

    public Campeonato getCampeonato() {
        return campeonato;
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

    public List<Convocatoria> getConvocatorias() {
        return Collections.unmodifiableList(convocatorias);
    }
}
