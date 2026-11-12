package co.edu.soccermanager.service;

import java.util.List;

import co.edu.soccermanager.modelo.Mensualidad;
import co.edu.soccermanager.modelo.MetodoPago;

/** Contrato de la capa de negocio para Mensualidad. */
public interface IMensualidadService {

    List<Mensualidad> listar(String estudianteId);
    Mensualidad buscarPorId(String id);
    Mensualidad crear(Mensualidad mensualidad);
    Mensualidad registrarPago(String id, double valor, MetodoPago metodo, String comprobante);
    Mensualidad anularPago(String id, int indice, String motivo);
    void eliminar(String id);
}
