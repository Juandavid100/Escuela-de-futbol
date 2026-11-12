package co.edu.soccermanager.excepcion;

import org.springframework.http.HttpStatus;

/** 404: el recurso pedido no existe. */
public class RecursoNoEncontradoException extends SoccerManagerException {

    public RecursoNoEncontradoException(String mensaje) {
        super(mensaje);
    }

    @Override
    public HttpStatus getEstadoHttp() {
        return HttpStatus.NOT_FOUND;
    }
}
