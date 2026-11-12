package co.edu.soccermanager.modelo;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "convocatorias")
public class Convocatoria {

    @Id
    private String id;
    private String partidoId;
    private String estudianteId;
    private String convocadoPor;
    private LocalDateTime fecha = LocalDateTime.now();
    private EstadoConvocatoria estado = EstadoConvocatoria.SIN_RESPUESTA;
    private String motivo;

    public Convocatoria() {
    }

    public Convocatoria(String partidoId, String estudianteId, String convocadoPor) {
        this.partidoId = partidoId;
        this.estudianteId = estudianteId;
        this.convocadoPor = convocadoPor;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPartidoId() {
        return partidoId;
    }

    public void setPartidoId(String partidoId) {
        this.partidoId = partidoId;
    }

    public String getEstudianteId() {
        return estudianteId;
    }

    public void setEstudianteId(String estudianteId) {
        this.estudianteId = estudianteId;
    }

    public String getConvocadoPor() {
        return convocadoPor;
    }

    public void setConvocadoPor(String convocadoPor) {
        this.convocadoPor = convocadoPor;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public EstadoConvocatoria getEstado() {
        return estado;
    }

    public void setEstado(EstadoConvocatoria estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }
}
