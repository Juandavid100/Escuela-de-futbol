package co.edu.soccermanager.excepcion;

import org.springframework.http.HttpStatus;

/** 403: el usuario no tiene permiso para la acción. */
public class AccesoDenegadoException extends SoccerManagerException {

    public AccesoDenegadoException(String mensaje) {
        super(mensaje);
    }

    @Override
    public HttpStatus getEstadoHttp() {
        return HttpStatus.FORBIDDEN;
    }
}
