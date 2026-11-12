package co.edu.soccermanager.excepcion;

import org.springframework.http.HttpStatus;

/** 409: la acción choca con el estado actual (duplicado, cupo lleno...). */
public class ConflictoException extends SoccerManagerException {

    public ConflictoException(String mensaje) {
        super(mensaje);
    }

    @Override
    public HttpStatus getEstadoHttp() {
        return HttpStatus.CONFLICT;
    }
}
