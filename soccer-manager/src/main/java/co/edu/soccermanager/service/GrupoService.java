package co.edu.soccermanager.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.soccermanager.excepcion.ConflictoException;
import co.edu.soccermanager.excepcion.DatosInvalidosException;
import co.edu.soccermanager.excepcion.RecursoNoEncontradoException;
import co.edu.soccermanager.modelo.EstadoEstudiante;
import co.edu.soccermanager.modelo.EstadoGrupo;
import co.edu.soccermanager.modelo.Estudiante;
import co.edu.soccermanager.modelo.Grupo;
import co.edu.soccermanager.repository.CategoriaRepository;
import co.edu.soccermanager.repository.EntrenadorRepository;
import co.edu.soccermanager.repository.EstudianteRepository;
import co.edu.soccermanager.repository.GrupoRepository;

@Service
public class GrupoService implements IGrupoService {

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Override
    public List<Grupo> listar() {
        return grupoRepository.findAll();
    }

    @Override
    public Grupo buscarPorId(String id) {
        return grupoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el grupo con id " + id));
    }

    @Override
    public List<Estudiante> listarEstudiantes(String id) {
        buscarPorId(id);
        return estudianteRepository.findByGrupoIdAndEstado(id, EstadoEstudiante.ACTIVO);
    }

    @Override
    public Grupo crear(Grupo grupo) {
        validar(grupo);
        if (grupoRepository.existsByCodigo(grupo.getCodigo())) {
            throw new ConflictoException("Ya existe un grupo con el código " + grupo.getCodigo());
        }
        if (grupo.getEntrenadorIds() == null) {
            grupo.setEntrenadorIds(new ArrayList<>());
        }
        for (String entrenadorId : grupo.getEntrenadorIds()) {
            validarEntrenador(entrenadorId);
        }
        grupo.setId(null);
        grupo.setEstado(EstadoGrupo.ACTIVO);
        return grupoRepository.save(grupo);
    }

    @Override
    public Grupo actualizar(String id, Grupo datos) {
        Grupo actual = buscarPorId(id);
        validar(datos);
        if (!actual.getCodigo().equals(datos.getCodigo()) && grupoRepository.existsByCodigo(datos.getCodigo())) {
            throw new ConflictoException("Ya existe un grupo con el código " + datos.getCodigo());
        }
        long activos = estudianteRepository.countByGrupoIdAndEstado(id, EstadoEstudiante.ACTIVO);
        if (datos.getCupoMaximo() < activos) {
            throw new ConflictoException("El cupo no puede ser menor que los " + activos + " estudiantes activos del grupo");
        }
        actual.setCodigo(datos.getCodigo());
        actual.setCategoriaId(datos.getCategoriaId());
        actual.setCupoMaximo(datos.getCupoMaximo());
        actual.setHorario(datos.getHorario());
        actual.setUbicacion(datos.getUbicacion());
        if (datos.getEstado() != null) {
            actual.setEstado(datos.getEstado());
        }
        return grupoRepository.save(actual);
    }

    @Override
    public Grupo asignarEntrenador(String id, String entrenadorId) {
        Grupo grupo = buscarPorId(id);
        validarEntrenador(entrenadorId);
        if (grupo.getEntrenadorIds().contains(entrenadorId)) {
            throw new ConflictoException("El entrenador ya está asignado a este grupo");
        }
        grupo.getEntrenadorIds().add(entrenadorId);
        return grupoRepository.save(grupo);
    }

    @Override
    public Grupo quitarEntrenador(String id, String entrenadorId) {
        Grupo grupo = buscarPorId(id);
        if (!grupo.getEntrenadorIds().remove(entrenadorId)) {
            throw new RecursoNoEncontradoException("El entrenador no está asignado a este grupo");
        }
        return grupoRepository.save(grupo);
    }

    @Override
    public void eliminar(String id) {
        buscarPorId(id);
        if (estudianteRepository.countByGrupoIdAndEstado(id, EstadoEstudiante.ACTIVO) > 0) {
            throw new ConflictoException("No se puede eliminar: el grupo tiene estudiantes activos");
        }
        grupoRepository.deleteById(id);
    }

    private void validar(Grupo grupo) {
        Validacion.presente(grupo, "cuerpo de la petición");
        Validacion.texto(grupo.getCodigo(), "codigo");
        Validacion.texto(grupo.getCategoriaId(), "categoriaId");
        Validacion.texto(grupo.getHorario(), "horario");
        if (grupo.getCupoMaximo() <= 0) {
            throw new DatosInvalidosException("El cupo máximo debe ser mayor que cero");
        }
        if (!categoriaRepository.existsById(grupo.getCategoriaId())) {
            throw new RecursoNoEncontradoException("No existe la categoría con id " + grupo.getCategoriaId());
        }
    }

    private void validarEntrenador(String entrenadorId) {
        if (!entrenadorRepository.existsById(entrenadorId)) {
            throw new RecursoNoEncontradoException("No existe el entrenador con id " + entrenadorId);
        }
    }
}
