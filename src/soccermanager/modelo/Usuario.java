package soccermanager.modelo;

import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

public class Usuario {

    private final String correo;
    private String claveCifrada;
    private EstadoCuenta estado;
    private Persona persona;
    private final Set<Rol> roles = EnumSet.noneOf(Rol.class);

    public Usuario(String correo, String claveCifrada, Persona persona, Rol rolInicial) {
        this.correo = correo;
        this.claveCifrada = claveCifrada;
        this.persona = persona;
        this.estado = EstadoCuenta.ACTIVA;
        roles.add(rolInicial);
    }

    public boolean tieneRol(Rol rol) {
        return roles.contains(rol);
    }

    public void agregarRol(Rol rol) {
        roles.add(rol);
    }

    public void quitarRol(Rol rol) {
        if (roles.size() == 1 && roles.contains(rol)) {
            throw new IllegalStateException("Un usuario debe tener al menos un rol");
        }
        roles.remove(rol);
    }

    public boolean estaActivo() {
        return estado == EstadoCuenta.ACTIVA;
    }

    public void suspender() {
        estado = EstadoCuenta.SUSPENDIDA;
    }

    public void reactivar() {
        estado = EstadoCuenta.ACTIVA;
    }

    public String getCorreo() {
        return correo;
    }

    public String getClaveCifrada() {
        return claveCifrada;
    }

    public void setClaveCifrada(String claveCifrada) {
        this.claveCifrada = claveCifrada;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Set<Rol> getRoles() {
        return Collections.unmodifiableSet(roles);
    }
}
