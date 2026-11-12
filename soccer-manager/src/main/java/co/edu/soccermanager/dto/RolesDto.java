package co.edu.soccermanager.dto;

import java.util.Set;

import co.edu.soccermanager.modelo.Rol;

/** Objeto de transferencia (capa de presentación). */
public record RolesDto(Set<Rol> roles) {
}
