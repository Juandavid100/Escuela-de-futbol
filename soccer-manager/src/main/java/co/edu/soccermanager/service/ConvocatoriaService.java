package co.edu.soccermanager.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.soccermanager.excepcion.AccesoDenegadoException;
import co.edu.soccermanager.excepcion.ConflictoException;
import co.edu.soccermanager.excepcion.DatosInvalidosException;
import co.edu.soccermanager.excepcion.RecursoNoEncontradoException;
import co.edu.soccermanager.modelo.Campeonato;
import co.edu.soccermanager.modelo.Convocatoria;
import co.edu.soccermanager.modelo.Entrenador;
import co.edu.soccermanager.modelo.EstadoActividad;
import co.edu.soccermanager.modelo.EstadoConvocatoria;
import co.edu.soccermanager.modelo.EstadoEstudiante;
import co.edu.soccermanager.modelo.Estudiante;
import co.edu.soccermanager.modelo.Partido;
import co.edu.soccermanager.modelo.TipoNotificacion;
import co.edu.soccermanager.repository.CampeonatoRepository;
import co.edu.soccermanager.repository.ConvocatoriaRepository;
import co.edu.soccermanager.repository.EntrenadorRepository;
import co.edu.soccermanager.repository.EstudianteRepository;
import co.edu.soccermanager.repository.PartidoRepository;

@Service
public class ConvocatoriaService implements IConvocatoriaService {

    @Autowired
    private ConvocatoriaRepository convocatoriaRepository;

    @Autowired
    private PartidoRepository partidoRepository;

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @Autowired
    private INotificacionService notificacionService;

    @Override
    public List<Convocatoria> listar(String partidoId, String estudianteId) {
        if (partidoId != null) {
            return convocatoriaRepository.findByPartidoId(partidoId);
        }
        if (estudianteId != null) {
            return convocatoriaRepository.findByEstudianteId(estudianteId);
        }
        return convocatoriaRepository.findAll();
    }

    @Override
    public Convocatoria buscarPorId(String id) {
        return convocatoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la convocatoria con id " + id));
    }

    @Override
    public Convocatoria convocar(Convocatoria convocatoria) {
        Validacion.presente(convocatoria, "cuerpo de la petición");
        Validacion.texto(convocatoria.getPartidoId(), "partidoId");
        Validacion.texto(convocatoria.getEstudianteId(), "estudianteId");
        Validacion.texto(convocatoria.getConvocadoPor(), "convocadoPor");
        Entrenador entrenador = entrenadorRepository.findById(convocatoria.getConvocadoPor())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el entrenador con id " + convocatoria.getConvocadoPor()));
        if (!entrenador.isPuedeConvocar()) {
            throw new AccesoDenegadoException("El entrenador no tiene permiso para convocar");
        }
        Partido partido = partidoRepository.findById(convocatoria.getPartidoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el partido con id " + convocatoria.getPartidoId()));
        if (partido.getEstado() == EstadoActividad.CANCELADO || partido.getEstado() == EstadoActividad.REALIZADO) {
            throw new ConflictoException("No se puede convocar a un partido " + partido.getEstado());
        }
        Estudiante estudiante = estudianteRepository.findById(convocatoria.getEstudianteId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el estudiante con id " + convocatoria.getEstudianteId()));
        if (estudiante.getEstado() != EstadoEstudiante.ACTIVO) {
            throw new DatosInvalidosException("Solo se pueden convocar estudiantes activos");
        }
        Campeonato campeonato = campeonatoRepository.findById(partido.getCampeonatoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el campeonato del partido"));
        if (!campeonato.getCategoriaId().equals(estudiante.getCategoriaId())) {
            throw new DatosInvalidosException("El estudiante no pertenece a la categoría del campeonato");
        }
        if (convocatoriaRepository.existsByPartidoIdAndEstudianteId(partido.getId(), estudiante.getId())) {
            throw new ConflictoException("El estudiante ya está convocado a este partido");
        }
        convocatoria.setId(null);
        convocatoria.setFecha(LocalDateTime.now());
        convocatoria.setEstado(EstadoConvocatoria.SIN_RESPUESTA);
        convocatoria.setMotivo(null);
        Convocatoria guardada = convocatoriaRepository.save(convocatoria);
        notificacionService.notificarEstudianteYAcudientes(estudiante, TipoNotificacion.CONVOCATORIA,
                "Convocado al partido contra " + partido.getRival() + " el " + partido.getFechaInicio());
        return guardada;
    }

    @Override
    public Convocatoria responder(String id, EstadoConvocatoria respuesta, String motivo) {
        Validacion.presente(respuesta, "estado");
        if (respuesta != EstadoConvocatoria.CONFIRMADO && respuesta != EstadoConvocatoria.NO_ASISTIRA) {
            throw new DatosInvalidosException("La respuesta debe ser CONFIRMADO o NO_ASISTIRA");
        }
        if (respuesta == EstadoConvocatoria.NO_ASISTIRA) {
            Validacion.texto(motivo, "motivo");
        }
        Convocatoria convocatoria = buscarPorId(id);
        if (convocatoria.getEstado() == EstadoConvocatoria.RETIRADO) {
            throw new ConflictoException("La convocatoria fue retirada");
        }
        convocatoria.setEstado(respuesta);
        convocatoria.setMotivo(motivo);
        return convocatoriaRepository.save(convocatoria);
    }

    @Override
    public Convocatoria retirar(String id, String motivo) {
        Validacion.texto(motivo, "motivo");
        Convocatoria convocatoria = buscarPorId(id);
        if (convocatoria.getEstado() == EstadoConvocatoria.RETIRADO) {
            throw new ConflictoException("La convocatoria ya estaba retirada");
        }
        convocatoria.setEstado(EstadoConvocatoria.RETIRADO);
        convocatoria.setMotivo(motivo);
        Convocatoria guardada = convocatoriaRepository.save(convocatoria);
        estudianteRepository.findById(convocatoria.getEstudianteId()).ifPresent(estudiante ->
                notificacionService.notificarEstudianteYAcudientes(estudiante, TipoNotificacion.CONVOCATORIA,
                        "Se retiró la convocatoria. Motivo: " + motivo));
        return guardada;
    }
}
