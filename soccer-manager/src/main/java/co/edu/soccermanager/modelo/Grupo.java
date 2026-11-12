package co.edu.soccermanager.modelo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "grupos")
public class Grupo {

    @Id
    private String id;
    private String codigo;
    private String categoriaId;
    private int cupoMaximo;
    private String horario;
    private Ubicacion ubicacion;
    private EstadoGrupo estado = EstadoGrupo.ACTIVO;
    private List<String> entrenadorIds = new ArrayList<>();

    public Grupo() {
    }

    public Grupo(String codigo, String categoriaId, int cupoMaximo, String horario, Ubicacion ubicacion) {
        this.codigo = codigo;
        this.categoriaId = categoriaId;
        this.cupoMaximo = cupoMaximo;
        this.horario = horario;
        this.ubicacion = ubicacion;
    }

    public boolean tieneCupo(long estudiantesActivos) {
        return estudiantesActivos < cupoMaximo;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getCategoriaId() {
        return categoriaId;
    }

    public void setCategoriaId(String categoriaId) {
        this.categoriaId = categoriaId;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public void setCupoMaximo(int cupoMaximo) {
        this.cupoMaximo = cupoMaximo;
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

    public EstadoGrupo getEstado() {
        return estado;
    }

    public void setEstado(EstadoGrupo estado) {
        this.estado = estado;
    }

    public List<String> getEntrenadorIds() {
        return entrenadorIds;
    }

    public void setEntrenadorIds(List<String> entrenadorIds) {
        this.entrenadorIds = entrenadorIds;
    }
}
