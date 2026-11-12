package co.edu.soccermanager.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.soccermanager.excepcion.ConflictoException;
import co.edu.soccermanager.excepcion.DatosInvalidosException;
import co.edu.soccermanager.excepcion.RecursoNoEncontradoException;
import co.edu.soccermanager.modelo.Campeonato;
import co.edu.soccermanager.modelo.Convocatoria;
import co.edu.soccermanager.modelo.EstadoActividad;
import co.edu.soccermanager.modelo.EstadoCampeonato;
import co.edu.soccermanager.modelo.Partido;
import co.edu.soccermanager.modelo.TipoNotificacion;
import co.edu.soccermanager.repository.AsistenciaRepository;
import co.edu.soccermanager.repository.CampeonatoRepository;
import co.edu.soccermanager.repository.ConvocatoriaRepository;
import co.edu.soccermanager.repository.EstudianteRepository;
import co.edu.soccermanager.repository.PartidoRepository;

@Service
public class PartidoService implements IPartidoService {

    @Autowired
    private PartidoRepository partidoRepository;

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Autowired
    private ConvocatoriaRepository convocatoriaRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private INotificacionService notificacionService;

    @Override
    public List<Partido> listar(String campeonatoId) {
        if (campeonatoId == null) {
            return partidoRepository.findAll();
        }
        return partidoRepository.findByCampeonatoId(campeonatoId);
    }

    @Override
    public Partido buscarPorId(String id) {
        return partidoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el partido con id " + id));
    }

    @Override
    public Partido crear(Partido partido) {
        Validacion.presente(partido, "cuerpo de la petición");
        Validacion.texto(partido.getCampeonatoId(), "campeonatoId");
        Validacion.texto(partido.getRival(), "rival");
        Campeonato campeonato = campeonatoRepository.findById(partido.getCampeonatoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el campeonato con id " + partido.getCampeonatoId()));
        if (campeonato.getEstado() == EstadoCampeonato.CANCELADO || campeonato.getEstado() == EstadoCampeonato.FINALIZADO) {
            throw new ConflictoException("No se pueden agregar partidos a un campeonato " + campeonato.getEstado());
        }
        validarHorario(campeonato, partido.getFechaInicio(), partido.getFechaFin());
        partido.setId(null);
        partido.setEstado(EstadoActividad.PROGRAMADO);
        partido.setResultado(null);
        partido.setCambios(new ArrayList<>());
        return partidoRepository.save(partido);
    }

    @Override
    public Partido actualizar(String id, Partido datos) {
        Partido actual = buscarPorId(id);
        Validacion.presente(datos, "cuerpo de la petición");
        Validacion.texto(datos.getRival(), "rival");
        actual.setRival(datos.getRival());
        if (datos.getUbicacion() != null) {
            actual.setUbicacion(datos.getUbicacion());
        }
        return partidoRepository.save(actual);
    }

    @Override
    public Partido reprogramar(String id, LocalDateTime inicio, LocalDateTime fin, String usuario) {
        Partido partido = buscarPorId(id);
        validarModificable(partido);
        Campeonato campeonato = campeonatoRepository.findById(partido.getCampeonatoId())
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el campeonato del partido"));
        validarHorario(campeonato, inicio, fin);
        partido.reprogramar(inicio, fin, Validacion.usuarioOSistema(usuario));
        Partido guardado = partidoRepository.save(partido);
        avisarConvocados(guardado, "El partido contra " + guardado.getRival() + " fue reprogramado para el " + inicio);
        return guardado;
    }

    @Override
    public Partido cancelar(String id, String motivo, String usuario) {
        Validacion.texto(motivo, "motivo");
        Partido partido = buscarPorId(id);
        validarModificable(partido);
        partido.cancelar(motivo, Validacion.usuarioOSistema(usuario));
        Partido guardado = partidoRepository.save(partido);
        avisarConvocados(guardado, "El partido contra " + guardado.getRival() + " fue cancelado. Motivo: " + motivo);
        return guardado;
    }

    @Override
    public Partido registrarResultado(String id, String resultado) {
        Validacion.texto(resultado, "resultado");
        Partido partido = buscarPorId(id);
        if (partido.getEstado() == EstadoActividad.CANCELADO) {
            throw new ConflictoException("El partido está cancelado");
        }
        if (partido.getFechaInicio().isAfter(LocalDateTime.now())) {
            throw new DatosInvalidosException("El partido aún no se ha jugado");
        }
        partido.setResultado(resultado);
        partido.setEstado(EstadoActividad.REALIZADO);
        return partidoRepository.save(partido);
    }

    @Override
    public void eliminar(String id) {
        buscarPorId(id);
        if (asistenciaRepository.existsByActividadId(id)) {
            throw new ConflictoException("No se puede eliminar: el partido ya tiene asistencia registrada. Cancélelo");
        }
        convocatoriaRepository.deleteAll(convocatoriaRepository.findByPartidoId(id));
        partidoRepository.deleteById(id);
    }

    private void validarModificable(Partido partido) {
        if (partido.getEstado() == EstadoActividad.CANCELADO || partido.getEstado() == EstadoActividad.REALIZADO) {
            throw new ConflictoException("El partido ya está " + partido.getEstado());
        }
    }

    private void validarHorario(Campeonato campeonato, LocalDateTime inicio, LocalDateTime fin) {
        Validacion.presente(inicio, "fechaInicio");
        Validacion.presente(fin, "fechaFin");
        if (!fin.isAfter(inicio)) {
            throw new DatosInvalidosException("La hora de fin debe ser posterior a la de inicio");
        }
        if (!campeonato.incluyeFecha(inicio.toLocalDate())) {
            throw new DatosInvalidosException("La fecha del partido está fuera de las fechas del campeonato");
        }
    }

    private void avisarConvocados(Partido partido, String mensaje) {
        for (Convocatoria convocatoria : convocatoriaRepository.findByPartidoId(partido.getId())) {
            estudianteRepository.findById(convocatoria.getEstudianteId()).ifPresent(estudiante ->
                    notificacionService.notificarEstudianteYAcudientes(estudiante, TipoNotificacion.CAMBIO_ACTIVIDAD, mensaje));
        }
    }
}
