package co.edu.soccermanager.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.soccermanager.excepcion.ConflictoException;
import co.edu.soccermanager.excepcion.DatosInvalidosException;
import co.edu.soccermanager.excepcion.RecursoNoEncontradoException;
import co.edu.soccermanager.modelo.Categoria;
import co.edu.soccermanager.modelo.EstadoEstudiante;
import co.edu.soccermanager.modelo.EstadoGrupo;
import co.edu.soccermanager.modelo.Estudiante;
import co.edu.soccermanager.modelo.Grupo;
import co.edu.soccermanager.modelo.TipoNotificacion;
import co.edu.soccermanager.repository.AcudienteRepository;
import co.edu.soccermanager.repository.CategoriaRepository;
import co.edu.soccermanager.repository.EstudianteRepository;
import co.edu.soccermanager.repository.GrupoRepository;

@Service
public class EstudianteService implements IEstudianteService {

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private AcudienteRepository acudienteRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private INotificacionService notificacionService;

    @Override
    public List<Estudiante> listar() {
        return estudianteRepository.findAll();
    }

    @Override
    public Estudiante buscarPorId(String id) {
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el estudiante con id " + id));
    }

    @Override
    public Estudiante crear(Estudiante estudiante) {
        validarDatos(estudiante);
        if (estudianteRepository.existsByDocumento(estudiante.getDocumento())) {
            throw new ConflictoException("Ya existe un estudiante con el documento " + estudiante.getDocumento());
        }
        validarAcudientes(estudiante);
        if (estudiante.getCategoriaId() != null) {
            validarCategoria(estudiante, estudiante.getCategoriaId());
        }
        estudiante.setId(null);
        estudiante.setEstado(EstadoEstudiante.PENDIENTE);
        estudiante.setGrupoId(null);
        estudiante.setHistorial(new ArrayList<>());
        estudiante.registrarEnHistorial("Matrícula registrada");
        return estudianteRepository.save(estudiante);
    }

    @Override
    public Estudiante actualizar(String id, Estudiante datos) {
        Estudiante actual = buscarPorId(id);
        validarDatos(datos);
        if (!actual.getDocumento().equals(datos.getDocumento())
                && estudianteRepository.existsByDocumento(datos.getDocumento())) {
            throw new ConflictoException("Ya existe un estudiante con el documento " + datos.getDocumento());
        }
        actual.setDocumento(datos.getDocumento());
        actual.setNombres(datos.getNombres());
        actual.setApellidos(datos.getApellidos());
        actual.setTelefono(datos.getTelefono());
        actual.setCorreo(datos.getCorreo());
        actual.setFechaNacimiento(datos.getFechaNacimiento());
        actual.setAcudienteIds(datos.getAcudienteIds());
        validarAcudientes(actual);
        if (datos.getCategoriaId() != null && !datos.getCategoriaId().equals(actual.getCategoriaId())) {
            validarCategoria(actual, datos.getCategoriaId());
            actual.setCategoriaId(datos.getCategoriaId());
            actual.registrarEnHistorial("Cambio de categoría");
            notificacionService.notificarEstudianteYAcudientes(actual, TipoNotificacion.CAMBIO_CATEGORIA,
                    "La categoría del estudiante fue actualizada");
        }
        actual.registrarEnHistorial("Datos actualizados");
        return estudianteRepository.save(actual);
    }

    @Override
    public Estudiante asignarGrupo(String id, String grupoId) {
        Validacion.texto(grupoId, "grupoId");
        Estudiante estudiante = buscarPorId(id);
        if (estudiante.getEstado() == EstadoEstudiante.RETIRADO) {
            throw new ConflictoException("El estudiante está retirado");
        }
        if (grupoId.equals(estudiante.getGrupoId())) {
            throw new ConflictoException("El estudiante ya pertenece a este grupo");
        }
        Grupo grupo = grupoRepository.findById(grupoId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el grupo con id " + grupoId));
        if (grupo.getEstado() != EstadoGrupo.ACTIVO) {
            throw new ConflictoException("El grupo no está activo");
        }
        if (!grupo.getCategoriaId().equals(estudiante.getCategoriaId())) {
            validarCategoria(estudiante, grupo.getCategoriaId());
            estudiante.setCategoriaId(grupo.getCategoriaId());
        }
        long activos = estudianteRepository.countByGrupoIdAndEstado(grupoId, EstadoEstudiante.ACTIVO);
        if (!grupo.tieneCupo(activos)) {
            throw new ConflictoException("El grupo alcanzó su capacidad");
        }
        estudiante.setGrupoId(grupoId);
        estudiante.setEstado(EstadoEstudiante.ACTIVO);
        estudiante.registrarEnHistorial("Asignado al grupo " + grupo.getCodigo());
        Estudiante guardado = estudianteRepository.save(estudiante);
        notificacionService.notificarEstudianteYAcudientes(guardado, TipoNotificacion.GENERAL,
                "Fue asignado al grupo " + grupo.getCodigo() + ", horario: " + grupo.getHorario());
        return guardado;
    }

    @Override
    public Estudiante retirar(String id, String motivo, String usuario) {
        Validacion.texto(motivo, "motivo");
        Estudiante estudiante = buscarPorId(id);
        if (estudiante.getEstado() == EstadoEstudiante.RETIRADO) {
            throw new ConflictoException("El estudiante ya está retirado");
        }
        estudiante.setEstado(EstadoEstudiante.RETIRADO);
        estudiante.setGrupoId(null);
        estudiante.registrarEnHistorial("Retirado por " + Validacion.usuarioOSistema(usuario) + ". Motivo: " + motivo);
        return estudianteRepository.save(estudiante);
    }

    @Override
    public void eliminar(String id) {
        buscarPorId(id);
        throw new ConflictoException("Un estudiante no se elimina: use el retiro para conservar su historial");
    }

    private void validarDatos(Estudiante estudiante) {
        Validacion.persona(estudiante);
        Validacion.presente(estudiante.getFechaNacimiento(), "fechaNacimiento");
        if (estudiante.getFechaNacimiento().isAfter(LocalDate.now())) {
            throw new DatosInvalidosException("La fecha de nacimiento no puede ser futura");
        }
        if (estudiante.getAcudienteIds() == null) {
            estudiante.setAcudienteIds(new ArrayList<>());
        }
    }

    private void validarAcudientes(Estudiante estudiante) {
        if (estudiante.esMenorDeEdad() && estudiante.getAcudienteIds().isEmpty()) {
            throw new DatosInvalidosException("Un estudiante menor de edad debe tener al menos un acudiente");
        }
        for (String acudienteId : estudiante.getAcudienteIds()) {
            if (!acudienteRepository.existsById(acudienteId)) {
                throw new RecursoNoEncontradoException("No existe el acudiente con id " + acudienteId);
            }
        }
    }

    private void validarCategoria(Estudiante estudiante, String categoriaId) {
        Categoria categoria = categoriaRepository.findById(categoriaId)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la categoría con id " + categoriaId));
        if (!categoria.aplicaPara(estudiante)) {
            throw new DatosInvalidosException("La edad del estudiante no corresponde a la categoría " + categoria.getNombre());
        }
    }
}
