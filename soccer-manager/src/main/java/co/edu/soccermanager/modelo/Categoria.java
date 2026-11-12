package co.edu.soccermanager.modelo;

import java.time.LocalDate;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "categorias")
public class Categoria {

    @Id
    private String id;
    private String nombre;
    private int edadMinima;
    private int edadMaxima;
    private int temporada;
    private LocalDate fechaCorte;

    public Categoria() {
    }

    public Categoria(String nombre, int edadMinima, int edadMaxima, int temporada, LocalDate fechaCorte) {
        this.nombre = nombre;
        this.edadMinima = edadMinima;
        this.edadMaxima = edadMaxima;
        this.temporada = temporada;
        this.fechaCorte = fechaCorte;
    }

    public boolean rangoValido() {
        return edadMinima <= edadMaxima;
    }

    public boolean aplicaPara(Estudiante estudiante) {
        int edad = estudiante.calcularEdad(fechaCorte);
        return edad >= edadMinima && edad <= edadMaxima;
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
}
