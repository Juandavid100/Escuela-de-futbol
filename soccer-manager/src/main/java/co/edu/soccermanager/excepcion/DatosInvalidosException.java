package co.edu.soccermanager.excepcion;

import org.springframework.http.HttpStatus;

/** 400: los datos enviados no cumplen una regla de negocio. */
public class DatosInvalidosException extends SoccerManagerException {

    public DatosInvalidosException(String mensaje) {
        super(mensaje);
    }

    @Override
    public HttpStatus getEstadoHttp() {
        return HttpStatus.BAD_REQUEST;
    }
}
