package co.edu.soccermanager.modelo;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "acudientes")
public class Acudiente extends Persona {

    private String parentesco;

    public Acudiente() {
    }

    public Acudiente(String documento, String nombres, String apellidos, String telefono, String correo, String parentesco) {
        super(documento, nombres, apellidos, telefono, correo);
        this.parentesco = parentesco;
    }

    public String getParentesco() {
        return parentesco;
    }

    public void setParentesco(String parentesco) {
        this.parentesco = parentesco;
    }
}
