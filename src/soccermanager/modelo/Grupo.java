package soccermanager.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Grupo {

    private final String codigo;
    private Categoria categoria;
    private int cupoMaximo;
    private EstadoGrupo estado;
    private String horario;
    private Ubicacion ubicacion;
    private final List<Entrenador> entrenadores = new ArrayList<>();
    private final List<Estudiante> estudiantes = new ArrayList<>();

    public Grupo(String codigo, Categoria categoria, int cupoMaximo, String horario, Ubicacion ubicacion) {
        this.codigo = codigo;
        this.categoria = categoria;
        this.cupoMaximo = cupoMaximo;
        this.horario = horario;
        this.ubicacion = ubicacion;
        this.estado = EstadoGrupo.ACTIVO;
    }

    public boolean tieneCupo() {
        return estudiantes.size() < cupoMaximo;
    }

    public int cuposDisponibles() {
        return cupoMaximo - estudiantes.size();
    }

    public void agregarEstudiante(Estudiante estudiante) {
        if (!tieneCupo()) {
            throw new IllegalStateException("El grupo alcanzó su capacidad");
        }
        if (estudiante.getGrupo() != null) {
            estudiante.getGrupo().quitarEstudiante(estudiante);
        }
        estudiantes.add(estudiante);
        estudiante.setGrupo(this);
        estudiante.setCategoria(categoria);
        estudiante.setEstado(EstadoEstudiante.ACTIVO);
        estudiante.registrarEnHistorial("Asignado al grupo " + codigo);
    }

    public void quitarEstudiante(Estudiante estudiante) {
        estudiantes.remove(estudiante);
        estudiante.setGrupo(null);
    }

    public void asignarEntrenador(Entrenador entrenador) {
        if (!entrenadores.contains(entrenador)) {
            entrenadores.add(entrenador);
            entrenador.asignarGrupo(this);
        }
    }

    public String getCodigo() {
        return codigo;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
    }

    public EstadoGrupo getEstado() {
        return estado;
    }

    public void setEstado(EstadoGrupo estado) {
        this.estado = estado;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public Ubicacion getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public List<Entrenador> getEntrenadores() {
        return Collections.unmodifiableList(entrenadores);
    }

    public List<Estudiante> getEstudiantes() {
        return Collections.unmodifiableList(estudiantes);
    }

    @Override
    public String toString() {
        return codigo;
    }
}
