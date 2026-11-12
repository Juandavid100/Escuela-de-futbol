package co.edu.soccermanager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.soccermanager.excepcion.ConflictoException;
import co.edu.soccermanager.excepcion.DatosInvalidosException;
import co.edu.soccermanager.excepcion.RecursoNoEncontradoException;
import co.edu.soccermanager.modelo.EstadoMensualidad;
import co.edu.soccermanager.modelo.Mensualidad;
import co.edu.soccermanager.modelo.MetodoPago;
import co.edu.soccermanager.modelo.Pago;
import co.edu.soccermanager.modelo.TipoNotificacion;
import co.edu.soccermanager.repository.EstudianteRepository;
import co.edu.soccermanager.repository.MensualidadRepository;

@Service
public class MensualidadService implements IMensualidadService {

    @Autowired
    private MensualidadRepository mensualidadRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private INotificacionService notificacionService;

    @Override
    public List<Mensualidad> listar(String estudianteId) {
        if (estudianteId == null) {
            return mensualidadRepository.findAll();
        }
        return mensualidadRepository.findByEstudianteId(estudianteId);
    }

    @Override
    public Mensualidad buscarPorId(String id) {
        return mensualidadRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la mensualidad con id " + id));
    }

    @Override
    public Mensualidad crear(Mensualidad mensualidad) {
        Validacion.presente(mensualidad, "cuerpo de la petición");
        Validacion.texto(mensualidad.getEstudianteId(), "estudianteId");
        Validacion.texto(mensualidad.getPeriodo(), "periodo");
        Validacion.presente(mensualidad.getFechaVencimiento(), "fechaVencimiento");
        if (mensualidad.getValor() <= 0) {
            throw new DatosInvalidosException("El valor de la mensualidad debe ser mayor que cero");
        }
        if (mensualidad.getPorcentajeDescuento() < 0 || mensualidad.getPorcentajeDescuento() > 100) {
            throw new DatosInvalidosException("El descuento debe estar entre 0 y 100");
        }
        if (!estudianteRepository.existsById(mensualidad.getEstudianteId())) {
            throw new RecursoNoEncontradoException("No existe el estudiante con id " + mensualidad.getEstudianteId());
        }
        if (mensualidadRepository.existsByEstudianteIdAndPeriodo(mensualidad.getEstudianteId(), mensualidad.getPeriodo())) {
            throw new ConflictoException("Ya existe la mensualidad del periodo " + mensualidad.getPeriodo() + " para este estudiante");
        }
        mensualidad.setId(null);
        mensualidad.setPagos(new ArrayList<>());
        return mensualidadRepository.save(mensualidad);
    }

    @Override
    public Mensualidad registrarPago(String id, double valor, MetodoPago metodo, String comprobante) {
        Validacion.presente(metodo, "metodo");
        if (valor <= 0) {
            throw new DatosInvalidosException("El valor del pago debe ser mayor que cero");
        }
        if (metodo == MetodoPago.TRANSFERENCIA) {
            Validacion.texto(comprobante, "comprobante");
        }
        Mensualidad mensualidad = buscarPorId(id);
        if (mensualidad.getEstado() == EstadoMensualidad.PAGADO) {
            throw new ConflictoException("La mensualidad ya está pagada");
        }
        if (valor > mensualidad.getSaldo()) {
            throw new DatosInvalidosException("El pago supera el saldo pendiente de " + mensualidad.getSaldo());
        }
        mensualidad.registrarPago(valor, metodo, comprobante);
        Mensualidad guardada = mensualidadRepository.save(mensualidad);
        estudianteRepository.findById(guardada.getEstudianteId()).ifPresent(estudiante ->
                notificacionService.notificarEstudianteYAcudientes(estudiante, TipoNotificacion.PAGO,
                        "Se registró un pago de " + valor + " a la mensualidad " + guardada.getPeriodo()
                                + ". Saldo: " + guardada.getSaldo()));
        return guardada;
    }

    @Override
    public Mensualidad anularPago(String id, int indice, String motivo) {
        Validacion.texto(motivo, "motivo");
        Mensualidad mensualidad = buscarPorId(id);
        if (indice < 0 || indice >= mensualidad.getPagos().size()) {
            throw new RecursoNoEncontradoException("No existe el pago número " + indice);
        }
        Pago pago = mensualidad.getPagos().get(indice);
        if (pago.isAnulado()) {
            throw new ConflictoException("El pago ya estaba anulado");
        }
        pago.anular(motivo);
        return mensualidadRepository.save(mensualidad);
    }

    @Override
    public void eliminar(String id) {
        Mensualidad mensualidad = buscarPorId(id);
        if (mensualidad.getTotalPagado() > 0) {
            throw new ConflictoException("No se puede eliminar: la mensualidad tiene pagos. Anúlelos primero");
        }
        mensualidadRepository.deleteById(id);
    }
}
