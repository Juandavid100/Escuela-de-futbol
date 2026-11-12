package co.edu.soccermanager.modelo;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;
    private String correo;
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String clave;
    private String personaId;
    private EstadoCuenta estado = EstadoCuenta.ACTIVA;
    private Set<Rol> roles = new HashSet<>();

    public Usuario() {
    }

    public Usuario(String correo, String clave, String personaId) {
        this.correo = correo;
        this.clave = clave;
        this.personaId = personaId;
    }

    public boolean tieneRol(Rol rol) {
        return roles.contains(rol);
    }

    public boolean estaActivo() {
        return estado == EstadoCuenta.ACTIVA;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getPersonaId() {
        return personaId;
    }

    public void setPersonaId(String personaId) {
        this.personaId = personaId;
    }

    public EstadoCuenta getEstado() {
        return estado;
    }

    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }

    public Set<Rol> getRoles() {
        return roles;
    }

    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}
