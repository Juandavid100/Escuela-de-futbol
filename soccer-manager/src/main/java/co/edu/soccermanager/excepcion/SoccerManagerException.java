package co.edu.soccermanager.excepcion;

import org.springframework.http.HttpStatus;

/**
 * Excepción base del negocio. Cada subclase sabe qué código HTTP le corresponde
 * (Polimorfismo), así el manejador global no necesita un if por cada tipo.
 */
public abstract class SoccerManagerException extends RuntimeException {

    protected SoccerManagerException(String mensaje) {
        super(mensaje);
    }

    public abstract HttpStatus getEstadoHttp();
}
