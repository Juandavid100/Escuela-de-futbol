package co.edu.soccermanager.modelo;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "campeonatos")
public class Campeonato {

    @Id
    private String id;
    private String nombre;
    private String categoriaId;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String entrenadorId;
    private EstadoCampeonato estado = EstadoCampeonato.PLANIFICADO;

    public Campeonato() {
    }

    public Campeonato(String nombre, String categoriaId, LocalDate fechaInicio, LocalDate fechaFin, String entrenadorId) {
        this.nombre = nombre;
        this.categoriaId = categoriaId;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.entrenadorId = entrenadorId;
    }

    public boolean fechasValidas() {
        return fechaInicio != null && fechaFin != null && !fechaFin.isBefore(fechaInicio);
    }

    public boolean incluyeFecha(LocalDate fecha) {
        return !fecha.isBefore(fechaInicio) && !fecha.isAfter(fechaFin);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
    }

    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public String getEntrenadorId() {
        return entrenadorId;
    }

    public void setEntrenadorId(String entrenadorId) {
        this.entrenadorId = entrenadorId;
    }

    public EstadoCampeonato getEstado() {
        return estado;
    }

    public void setEstado(EstadoCampeonato estado) {
        this.estado = estado;
    }
}
