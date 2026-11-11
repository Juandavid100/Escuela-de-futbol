package soccermanager.modelo;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Campeonato {

    private String nombre;
    private Categoria categoria;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private Entrenador entrenadorResponsable;
    private EstadoCampeonato estado;
    private final List<Partido> partidos = new ArrayList<>();

    public Campeonato(String nombre, Categoria categoria, LocalDate fechaInicio, LocalDate fechaFin, Entrenador entrenadorResponsable) {
        if (fechaFin.isBefore(fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin debe ser posterior a la de inicio");
        }
        this.nombre = nombre;
        this.categoria = categoria;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.entrenadorResponsable = entrenadorResponsable;
        this.estado = EstadoCampeonato.PLANIFICADO;
    }

    public Partido agregarPartido(String rival, LocalDateTime inicio, LocalDateTime fin, Ubicacion ubicacion) {
        LocalDate dia = inicio.toLocalDate();
        if (dia.isBefore(fechaInicio) || dia.isAfter(fechaFin)) {
            throw new IllegalArgumentException("El partido debe estar dentro de las fechas del campeonato");
        }
        Partido partido = new Partido(this, rival, inicio, fin, ubicacion);
        partidos.add(partido);
        return partido;
    }

    public void cancelar(String motivo, String usuario) {
        estado = EstadoCampeonato.CANCELADO;
        for (Partido p : partidos) {
            p.cancelar(motivo, usuario);
        }
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
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

    public Entrenador getEntrenadorResponsable() {
        return entrenadorResponsable;
    }

    public void setEntrenadorResponsable(Entrenador entrenadorResponsable) {
        this.entrenadorResponsable = entrenadorResponsable;
    }

    public EstadoCampeonato getEstado() {
        return estado;
    }

    public void setEstado(EstadoCampeonato estado) {
        this.estado = estado;
    }

    public List<Partido> getPartidos() {
        return Collections.unmodifiableList(partidos);
    }
}
