package co.edu.soccermanager.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import co.edu.soccermanager.excepcion.SoccerManagerException;

/**
 * Convierte las excepciones del negocio en respuestas HTTP con el código correcto
 * y un cuerpo {"error": "mensaje"}. Así nunca se responde 200 cuando algo falla.
 */
@RestControllerAdvice
public class ManejadorExcepciones {

    @ExceptionHandler(SoccerManagerException.class)
    public ResponseEntity<Map<String, String>> manejarNegocio(SoccerManagerException e) {
        return ResponseEntity.status(e.getEstadoHttp()).body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> manejarCuerpoInvalido(HttpMessageNotReadableException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "El cuerpo de la petición no es un JSON válido o tiene un valor no permitido"));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> manejarInesperado(Exception e) {
        if (e instanceof ErrorResponse errorSpring) {
            // Errores propios de Spring: ruta inexistente (404), método no permitido (405), parámetro faltante (400)...
            return ResponseEntity.status(errorSpring.getStatusCode()).body(Map.of("error", e.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Error interno del servidor"));
    }
}
