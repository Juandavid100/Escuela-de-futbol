package soccermanager.modelo;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Estudiante extends Persona {

    private LocalDate fechaNacimiento;
    private EstadoEstudiante estado;
    private Grupo grupo;
    private Categoria categoria;
    private final List<Acudiente> acudientes = new ArrayList<>();
    private final List<String> historial = new ArrayList<>();

    public Estudiante(String documento, String nombres, String apellidos, String telefono, LocalDate fechaNacimiento) {
        super(documento, nombres, apellidos, telefono);
        if (fechaNacimiento == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria");
        }
        this.fechaNacimiento = fechaNacimiento;
        this.estado = EstadoEstudiante.PENDIENTE;
    }

    public int calcularEdad(LocalDate fecha) {
        return Period.between(fechaNacimiento, fecha).getYears();
    }

    public boolean esMenorDeEdad() {
        return calcularEdad(LocalDate.now()) < 18;
    }

    public void agregarAcudiente(Acudiente acudiente) {
        if (!acudientes.contains(acudiente)) {
            acudientes.add(acudiente);
            acudiente.agregarEstudiante(this);
        }
    }

    public void registrarEnHistorial(String evento) {
        historial.add(LocalDate.now() + " - " + evento);
    }

    public void retirar(String motivo) {
        if (estado == EstadoEstudiante.RETIRADO) {
            throw new IllegalStateException("El estudiante ya está retirado");
        }
        if (grupo != null) {
            grupo.quitarEstudiante(this);
        }
        estado = EstadoEstudiante.RETIRADO;
        registrarEnHistorial("Retirado. Motivo: " + motivo);
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public EstadoEstudiante getEstado() {
        return estado;
    }

    public void setEstado(EstadoEstudiante estado) {
        this.estado = estado;
    }

    public Grupo getGrupo() {
        return grupo;
    }

    public void setGrupo(Grupo grupo) {
        this.grupo = grupo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Acudiente> getAcudientes() {
        return Collections.unmodifiableList(acudientes);
    }

    public List<String> getHistorial() {
        return Collections.unmodifiableList(historial);
    }
}
