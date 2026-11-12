package co.edu.soccermanager.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.soccermanager.excepcion.AccesoDenegadoException;
import co.edu.soccermanager.excepcion.ConflictoException;
import co.edu.soccermanager.excepcion.CredencialesInvalidasException;
import co.edu.soccermanager.excepcion.DatosInvalidosException;
import co.edu.soccermanager.excepcion.RecursoNoEncontradoException;
import co.edu.soccermanager.modelo.EstadoCuenta;
import co.edu.soccermanager.modelo.Rol;
import co.edu.soccermanager.modelo.Usuario;
import co.edu.soccermanager.repository.AcudienteRepository;
import co.edu.soccermanager.repository.EntrenadorRepository;
import co.edu.soccermanager.repository.EstudianteRepository;
import co.edu.soccermanager.repository.UsuarioRepository;

@Service
public class UsuarioService implements IUsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private AcudienteRepository acudienteRepository;

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

    @Override
    public Usuario buscarPorId(String id) {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el usuario con id " + id));
    }

    @Override
    public Usuario crear(Usuario usuario) {
        Validacion.presente(usuario, "cuerpo de la petición");
        validarCorreo(usuario.getCorreo());
        validarClave(usuario.getClave());
        validarRoles(usuario.getRoles());
        if (usuarioRepository.existsByCorreo(usuario.getCorreo())) {
            throw new ConflictoException("Ya existe un usuario con el correo " + usuario.getCorreo());
        }
        validarPersona(usuario);
        usuario.setId(null);
        usuario.setClave(cifrar(usuario.getClave()));
        usuario.setEstado(EstadoCuenta.ACTIVA);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario actualizar(String id, Usuario datos) {
        Usuario actual = buscarPorId(id);
        Validacion.presente(datos, "cuerpo de la petición");
        validarCorreo(datos.getCorreo());
        if (!actual.getCorreo().equals(datos.getCorreo()) && usuarioRepository.existsByCorreo(datos.getCorreo())) {
            throw new ConflictoException("Ya existe un usuario con el correo " + datos.getCorreo());
        }
        actual.setCorreo(datos.getCorreo());
        if (datos.getClave() != null) {
            validarClave(datos.getClave());
            actual.setClave(cifrar(datos.getClave()));
        }
        return usuarioRepository.save(actual);
    }

    @Override
    public Usuario iniciarSesion(String correo, String clave) {
        Validacion.texto(correo, "correo");
        Validacion.texto(clave, "clave");
        Usuario usuario = usuarioRepository.findByCorreo(correo)
                .orElseThrow(() -> new CredencialesInvalidasException("Correo o contraseña incorrectos"));
        if (!usuario.getClave().equals(cifrar(clave))) {
            throw new CredencialesInvalidasException("Correo o contraseña incorrectos");
        }
        if (!usuario.estaActivo()) {
            throw new AccesoDenegadoException("La cuenta está suspendida. Comuníquese con la escuela");
        }
        return usuario;
    }

    @Override
    public Usuario asignarRoles(String id, Set<Rol> roles) {
        validarRoles(roles);
        Usuario usuario = buscarPorId(id);
        usuario.setRoles(roles);
        validarPersona(usuario);
        return usuarioRepository.save(usuario);
    }

    @Override
    public Usuario cambiarEstado(String id, EstadoCuenta estado) {
        Validacion.presente(estado, "estado");
        Usuario usuario = buscarPorId(id);
        usuario.setEstado(estado);
        return usuarioRepository.save(usuario);
    }

    @Override
    public void eliminar(String id) {
        buscarPorId(id);
        usuarioRepository.deleteById(id);
    }

    private void validarCorreo(String correo) {
        Validacion.texto(correo, "correo");
        if (!correo.contains("@")) {
            throw new DatosInvalidosException("El correo no tiene un formato válido");
        }
    }

    private void validarClave(String clave) {
        Validacion.texto(clave, "clave");
        if (clave.length() < 8) {
            throw new DatosInvalidosException("La contraseña debe tener al menos 8 caracteres");
        }
    }

    private void validarRoles(Set<Rol> roles) {
        if (roles == null || roles.isEmpty()) {
            throw new DatosInvalidosException("El usuario debe tener al menos un rol");
        }
    }

    /** El gerente puede no ser una persona registrada; los demás roles sí deben estarlo. */
    private void validarPersona(Usuario usuario) {
        boolean necesitaPersona = usuario.tieneRol(Rol.ESTUDIANTE) || usuario.tieneRol(Rol.ACUDIENTE)
                || usuario.tieneRol(Rol.ENTRENADOR);
        String personaId = usuario.getPersonaId();
        if (personaId == null || personaId.isBlank()) {
            if (necesitaPersona) {
                throw new DatosInvalidosException("El campo personaId es obligatorio para ese rol");
            }
            return;
        }
        boolean existe = estudianteRepository.existsById(personaId) || acudienteRepository.existsById(personaId)
                || entrenadorRepository.existsById(personaId);
        if (!existe) {
            throw new RecursoNoEncontradoException("No existe la persona con id " + personaId);
        }
    }

    private String cifrar(String clave) {
        try {
            byte[] hash = MessageDigest.getInstance("SHA-256").digest(clave.getBytes(StandardCharsets.UTF_8));
            StringBuilder texto = new StringBuilder();
            for (byte b : hash) {
                texto.append(String.format("%02x", b));
            }
            return texto.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("No se pudo cifrar la contraseña", e);
        }
    }
}
