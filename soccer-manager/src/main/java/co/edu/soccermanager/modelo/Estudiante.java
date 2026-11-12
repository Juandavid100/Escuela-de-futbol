package co.edu.soccermanager.modelo;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "estudiantes")
public class Estudiante extends Persona {

    private LocalDate fechaNacimiento;
    private EstadoEstudiante estado = EstadoEstudiante.PENDIENTE;
    private String grupoId;
    private String categoriaId;
    private List<String> acudienteIds = new ArrayList<>();
    private List<String> historial = new ArrayList<>();

    public Estudiante() {
    }

    public Estudiante(String documento, String nombres, String apellidos, String telefono, String correo, LocalDate fechaNacimiento) {
        super(documento, nombres, apellidos, telefono, correo);
        this.fechaNacimiento = fechaNacimiento;
    }

    public int calcularEdad(LocalDate fecha) {
        return Period.between(fechaNacimiento, fecha).getYears();
    }

    public boolean esMenorDeEdad() {
        return calcularEdad(LocalDate.now()) < 18;
    }

    public void registrarEnHistorial(String evento) {
        historial.add(LocalDate.now() + " - " + evento);
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

    public String getGrupoId() {
        return grupoId;
    }

    public void setGrupoId(String grupoId) {
        this.grupoId = grupoId;
    }

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
    }

    public List<String> getAcudienteIds() {
        return acudienteIds;
    }

    public void setAcudienteIds(List<String> acudienteIds) {
        this.acudienteIds = acudienteIds;
    }

    public List<String> getHistorial() {
        return historial;
    }

    public void setHistorial(List<String> historial) {
        this.historial = historial;
    }
}
