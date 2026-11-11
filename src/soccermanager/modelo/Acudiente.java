package soccermanager.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Acudiente extends Persona {

    private String correo;
    private final List<Estudiante> estudiantes = new ArrayList<>();

    public Acudiente(String documento, String nombres, String apellidos, String telefono, String correo) {
        super(documento, nombres, apellidos, telefono);
        this.correo = correo;
    }

    public void agregarEstudiante(Estudiante estudiante) {
        if (!estudiantes.contains(estudiante)) {
            estudiantes.add(estudiante);
            estudiante.agregarAcudiente(this);
        }
    }

    public boolean esAcudienteDe(Estudiante estudiante) {
        return estudiantes.contains(estudiante);
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public List<Estudiante> getEstudiantes() {
        return Collections.unmodifiableList(estudiantes);
    }
}
