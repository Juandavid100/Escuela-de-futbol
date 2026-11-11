package soccermanager.modelo;

public class Ubicacion {

    private String nombre;
    private String direccion;
    private String indicaciones;
    private String enlaceMapa;

    public Ubicacion(String nombre, String direccion, String indicaciones, String enlaceMapa) {
        this.nombre = nombre;
        this.direccion = direccion;
        this.indicaciones = indicaciones;
        this.enlaceMapa = enlaceMapa;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getIndicaciones() {
        return indicaciones;
    }

    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }

    public String getEnlaceMapa() {
        return enlaceMapa;
    }

    public void setEnlaceMapa(String enlaceMapa) {
        this.enlaceMapa = enlaceMapa;
    }

    @Override
    public String toString() {
        return nombre + " (" + direccion + ")";
    }
}
