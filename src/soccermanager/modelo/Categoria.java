package soccermanager.modelo;

import java.time.LocalDate;

public class Categoria {

    private String nombre;
    private int edadMinima;
    private int edadMaxima;
    private int temporada;
    private LocalDate fechaCorte;

    public Categoria(String nombre, int edadMinima, int edadMaxima, int temporada, LocalDate fechaCorte) {
        if (edadMinima > edadMaxima) {
            throw new IllegalArgumentException("La edad mínima no puede ser mayor que la máxima");
        }
        this.nombre = nombre;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
        this.temporada = temporada;
        this.fechaCorte = fechaCorte;
    }

    public boolean aplicaPara(Estudiante estudiante) {
        int edad = estudiante.calcularEdad(fechaCorte);
        return edad >= edadMinima && edad <= edadMaxima;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEdadMinima() {
        return edadMinima;
    }

    public void setEdadMinima(int edadMinima) {
        this.edadMinima = edadMinima;
    }

    public int getEdadMaxima() {
        return edadMaxima;
    }

    public void setEdadMaxima(int edadMaxima) {
        this.edadMaxima = edadMaxima;
    }

    public int getTemporada() {
        return temporada;
    }

    public void setTemporada(int temporada) {
        this.temporada = temporada;
    }

    public LocalDate getFechaCorte() {
        return fechaCorte;
    }

    public void setFechaCorte(LocalDate fechaCorte) {
        this.fechaCorte = fechaCorte;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
