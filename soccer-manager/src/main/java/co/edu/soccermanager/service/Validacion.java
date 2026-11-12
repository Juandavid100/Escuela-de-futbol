package co.edu.soccermanager.service;

import co.edu.soccermanager.excepcion.DatosInvalidosException;
import co.edu.soccermanager.modelo.Persona;

/**
 * Fabricación pura: reúne las validaciones repetidas para que cada servicio
 * no tenga que escribirlas otra vez.
 */
public final class Validacion {

    private Validacion() {
    }

    public static void texto(String valor, String campo) {
        if (valor == null || valor.isBlank()) {
            throw new DatosInvalidosException("El campo " + campo + " es obligatorio");
        }
    }

    public static void presente(Object valor, String campo) {
        if (valor == null) {
            throw new DatosInvalidosException("El campo " + campo + " es obligatorio");
        }
    }

    public static void persona(Persona persona) {
        presente(persona, "cuerpo de la petición");
        texto(persona.getDocumento(), "documento");
        texto(persona.getNombres(), "nombres");
        texto(persona.getApellidos(), "apellidos");
        if (persona.getCorreo() != null && !persona.getCorreo().isBlank() && !persona.getCorreo().contains("@")) {
            throw new DatosInvalidosException("El correo no tiene un formato válido");
        }
    }

    public static String usuarioOSistema(String usuario) {
        return usuario == null || usuario.isBlank() ? "sistema" : usuario;
    }
}
