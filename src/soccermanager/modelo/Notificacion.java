package soccermanager.modelo;

import java.time.LocalDateTime;

public class Notificacion {

    private final Persona destinatario;
    private final TipoNotificacion tipo;
    private final String mensaje;
    private final LocalDateTime fechaEnvio;
    private boolean anulada;

    public Notificacion(Persona destinatario, TipoNotificacion tipo, String mensaje) {
        this.destinatario = destinatario;
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.fechaEnvio = LocalDateTime.now();
    }

    public void anular() {
        anulada = true;
    }

    public Persona getDestinatario() {
        return destinatario;
    }

    public TipoNotificacion getTipo() {
        return tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public LocalDateTime getFechaEnvio() {
        return fechaEnvio;
    }

    public boolean isAnulada() {
        return anulada;
    }
}
