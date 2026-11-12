package co.edu.soccermanager.service;

import java.util.List;
import java.util.Set;

import co.edu.soccermanager.modelo.EstadoCuenta;
import co.edu.soccermanager.modelo.Rol;
import co.edu.soccermanager.modelo.Usuario;

/** Contrato de la capa de negocio para Usuario. */
public interface IUsuarioService {

    List<Usuario> listar();
    Usuario buscarPorId(String id);
    Usuario crear(Usuario usuario);
    Usuario actualizar(String id, Usuario usuario);
    Usuario iniciarSesion(String correo, String clave);
    Usuario asignarRoles(String id, Set<Rol> roles);
    Usuario cambiarEstado(String id, EstadoCuenta estado);
    void eliminar(String id);
}
