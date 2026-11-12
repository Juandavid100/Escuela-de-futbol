package co.edu.soccermanager.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.soccermanager.excepcion.ConflictoException;
import co.edu.soccermanager.excepcion.DatosInvalidosException;
import co.edu.soccermanager.excepcion.RecursoNoEncontradoException;
import co.edu.soccermanager.modelo.Evaluacion;
import co.edu.soccermanager.modelo.TipoNotificacion;
import co.edu.soccermanager.repository.EntrenadorRepository;
import co.edu.soccermanager.repository.EstudianteRepository;
import co.edu.soccermanager.repository.EvaluacionRepository;

@Service
public class EvaluacionService implements IEvaluacionService {

    @Autowired
    private EvaluacionRepository evaluacionRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @Autowired
    private INotificacionService notificacionService;

    @Override
    public List<Evaluacion> listar(String estudianteId, boolean soloVisibles) {
        if (estudianteId == null) {
            return evaluacionRepository.findAll();
        }
        if (soloVisibles) {
            return evaluacionRepository.findByEstudianteIdAndVisibleFamiliaTrue(estudianteId);
        }
        return evaluacionRepository.findByEstudianteId(estudianteId);
    }

    @Override
    public Evaluacion buscarPorId(String id) {
        return evaluacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la evaluación con id " + id));
    }

    @Override
    public Evaluacion crear(Evaluacion evaluacion) {
        validar(evaluacion);
        if (evaluacionRepository.existsByEstudianteIdAndPeriodo(evaluacion.getEstudianteId(), evaluacion.getPeriodo())) {
            throw new ConflictoException("El estudiante ya tiene una evaluación en el periodo " + evaluacion.getPeriodo());
        }
        evaluacion.setId(null);
        evaluacion.setFecha(LocalDate.now());
        Evaluacion guardada = evaluacionRepository.save(evaluacion);
        if (guardada.isVisibleFamilia()) {
            avisar(guardada);
        }
        return guardada;
    }

    @Override
    public Evaluacion actualizar(String id, Evaluacion datos) {
        Evaluacion actual = buscarPorId(id);
        validar(datos);
        actual.setTecnico(datos.getTecnico());
        actual.setTactico(datos.getTactico());
        actual.setFisico(datos.getFisico());
        actual.setActitudinal(datos.getActitudinal());
        actual.setObservaciones(datos.getObservaciones());
        actual.setMeta(datos.getMeta());
        return evaluacionRepository.save(actual);
    }

    @Override
    public Evaluacion publicar(String id) {
        Evaluacion evaluacion = buscarPorId(id);
        if (evaluacion.isVisibleFamilia()) {
            throw new ConflictoException("La evaluación ya es visible para la familia");
        }
        evaluacion.setVisibleFamilia(true);
        Evaluacion guardada = evaluacionRepository.save(evaluacion);
        avisar(guardada);
        return guardada;
    }

    @Override
    public void eliminar(String id) {
        buscarPorId(id);
        evaluacionRepository.deleteById(id);
    }

    private void validar(Evaluacion evaluacion) {
        Validacion.presente(evaluacion, "cuerpo de la petición");
        Validacion.texto(evaluacion.getEstudianteId(), "estudianteId");
        Validacion.texto(evaluacion.getEntrenadorId(), "entrenadorId");
        Validacion.texto(evaluacion.getPeriodo(), "periodo");
        if (!evaluacion.calificacionesValidas()) {
            throw new DatosInvalidosException("Las calificaciones deben estar entre 1 y 5");
        }
        if (!estudianteRepository.existsById(evaluacion.getEstudianteId())) {
            throw new RecursoNoEncontradoException("No existe el estudiante con id " + evaluacion.getEstudianteId());
        }
        if (!entrenadorRepository.existsById(evaluacion.getEntrenadorId())) {
            throw new RecursoNoEncontradoException("No existe el entrenador con id " + evaluacion.getEntrenadorId());
        }
    }

    private void avisar(Evaluacion evaluacion) {
        estudianteRepository.findById(evaluacion.getEstudianteId()).ifPresent(estudiante ->
                notificacionService.notificarEstudianteYAcudientes(estudiante, TipoNotificacion.GENERAL,
                        "Hay una nueva evaluación del periodo " + evaluacion.getPeriodo()));
    }
}
