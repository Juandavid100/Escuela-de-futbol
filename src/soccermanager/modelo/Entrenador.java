package soccermanager.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Entrenador extends Persona {

    private boolean puedeConvocar;
    private final List<Grupo> grupos = new ArrayList<>();

    public Entrenador(String documento, String nombres, String apellidos, String telefono) {
        super(documento, nombres, apellidos, telefono);
    }

    public void asignarGrupo(Grupo grupo) {
        if (!grupos.contains(grupo)) {
            grupos.add(grupo);
        }
    }

    public boolean entrenaA(Estudiante estudiante) {
        return estudiante.getGrupo() != null && grupos.contains(estudiante.getGrupo());
    }

    public boolean isPuedeConvocar() {
        return puedeConvocar;
    }

    public void setPuedeConvocar(boolean puedeConvocar) {
        this.puedeConvocar = puedeConvocar;
    }

    public List<Grupo> getGrupos() {
        return Collections.unmodifiableList(grupos);
    }
}
