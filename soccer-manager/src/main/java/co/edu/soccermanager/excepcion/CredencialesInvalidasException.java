package co.edu.soccermanager.excepcion;

import org.springframework.http.HttpStatus;

/** 401: correo o contraseña incorrectos. */
public class CredencialesInvalidasException extends SoccerManagerException {

    public CredencialesInvalidasException(String mensaje) {
        super(mensaje);
    }

    @Override
    public HttpStatus getEstadoHttp() {
        return HttpStatus.UNAUTHORIZED;
    }
}
