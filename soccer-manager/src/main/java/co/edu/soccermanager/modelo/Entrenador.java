package co.edu.soccermanager.modelo;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "entrenadores")
public class Entrenador extends Persona {

    private boolean puedeConvocar;
    private boolean puedeModificarActividades;

    public Entrenador() {
    }

    public Entrenador(String documento, String nombres, String apellidos, String telefono, String correo) {
        super(documento, nombres, apellidos, telefono, correo);
    }

    public boolean isPuedeConvocar() {
        return puedeConvocar;
    }

    public void setPuedeConvocar(boolean puedeConvocar) {
        this.puedeConvocar = puedeConvocar;
    }

    public boolean isPuedeModificarActividades() {
        return puedeModificarActividades;
    }

    public void setPuedeModificarActividades(boolean puedeModificarActividades) {
        this.puedeModificarActividades = puedeModificarActividades;
    }
}
