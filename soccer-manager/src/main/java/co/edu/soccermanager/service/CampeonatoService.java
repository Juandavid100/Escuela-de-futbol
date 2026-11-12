package co.edu.soccermanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.soccermanager.excepcion.ConflictoException;
import co.edu.soccermanager.excepcion.DatosInvalidosException;
import co.edu.soccermanager.excepcion.RecursoNoEncontradoException;
import co.edu.soccermanager.modelo.Campeonato;
import co.edu.soccermanager.modelo.EstadoActividad;
import co.edu.soccermanager.modelo.EstadoCampeonato;
import co.edu.soccermanager.modelo.Partido;
import co.edu.soccermanager.repository.AsistenciaRepository;
import co.edu.soccermanager.repository.CampeonatoRepository;
import co.edu.soccermanager.repository.CategoriaRepository;
import co.edu.soccermanager.repository.ConvocatoriaRepository;
import co.edu.soccermanager.repository.EntrenadorRepository;
import co.edu.soccermanager.repository.PartidoRepository;

/**
 * Un campeonato se compone de sus partidos (composición): si se cancela o se
 * elimina, sus partidos corren la misma suerte.
 */
@Service
public class CampeonatoService implements ICampeonatoService {

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @Autowired
    private PartidoRepository partidoRepository;

    @Autowired
    private ConvocatoriaRepository convocatoriaRepository;

    @Autowired
    private AsistenciaRepository asistenciaRepository;

    @Autowired
    private IPartidoService partidoService;

    @Override
    public List<Campeonato> listar() {
        return campeonatoRepository.findAll();
    }

    @Override
    public Campeonato buscarPorId(String id) {
        return campeonatoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el campeonato con id " + id));
    }

    @Override
    public Campeonato crear(Campeonato campeonato) {
        validar(campeonato);
        campeonato.setId(null);
        campeonato.setEstado(EstadoCampeonato.PLANIFICADO);
        return campeonatoRepository.save(campeonato);
    }

    @Override
    public Campeonato actualizar(String id, Campeonato datos) {
        Campeonato actual = buscarPorId(id);
        validar(datos);
        if (actual.getEstado() == EstadoCampeonato.CANCELADO || actual.getEstado() == EstadoCampeonato.FINALIZADO) {
            throw new ConflictoException("No se puede modificar un campeonato " + actual.getEstado());
        }
        actual.setNombre(datos.getNombre());
        actual.setCategoriaId(datos.getCategoriaId());
        actual.setFechaInicio(datos.getFechaInicio());
        actual.setFechaFin(datos.getFechaFin());
        actual.setEntrenadorId(datos.getEntrenadorId());
        if (datos.getEstado() != null && datos.getEstado() != EstadoCampeonato.CANCELADO) {
            actual.setEstado(datos.getEstado());
        }
        return campeonatoRepository.save(actual);
    }

    @Override
    public Campeonato cancelar(String id, String motivo, String usuario) {
        Validacion.texto(motivo, "motivo");
        Campeonato campeonato = buscarPorId(id);
        if (campeonato.getEstado() == EstadoCampeonato.CANCELADO || campeonato.getEstado() == EstadoCampeonato.FINALIZADO) {
            throw new ConflictoException("El campeonato ya está " + campeonato.getEstado());
        }
        campeonato.setEstado(EstadoCampeonato.CANCELADO);
        for (Partido partido : partidoRepository.findByCampeonatoId(id)) {
            if (partido.getEstado() != EstadoActividad.CANCELADO && partido.getEstado() != EstadoActividad.REALIZADO) {
                partidoService.cancelar(partido.getId(), "Campeonato cancelado: " + motivo, usuario);
            }
        }
        return campeonatoRepository.save(campeonato);
    }

    @Override
    public void eliminar(String id) {
        buscarPorId(id);
        List<Partido> partidos = partidoRepository.findByCampeonatoId(id);
        for (Partido partido : partidos) {
            if (asistenciaRepository.existsByActividadId(partido.getId())) {
                throw new ConflictoException("No se puede eliminar: hay partidos con asistencia registrada. Cancélelo");
            }
        }
        for (Partido partido : partidos) {
            convocatoriaRepository.deleteAll(convocatoriaRepository.findByPartidoId(partido.getId()));
        }
        partidoRepository.deleteAll(partidos);
        campeonatoRepository.deleteById(id);
    }

    private void validar(Campeonato campeonato) {
        Validacion.presente(campeonato, "cuerpo de la petición");
        Validacion.texto(campeonato.getNombre(), "nombre");
        Validacion.texto(campeonato.getCategoriaId(), "categoriaId");
        Validacion.texto(campeonato.getEntrenadorId(), "entrenadorId");
        if (!campeonato.fechasValidas()) {
            throw new DatosInvalidosException("La fecha de fin no puede ser anterior a la de inicio");
        }
        if (!categoriaRepository.existsById(campeonato.getCategoriaId())) {
            throw new RecursoNoEncontradoException("No existe la categoría con id " + campeonato.getCategoriaId());
        }
        if (!entrenadorRepository.existsById(campeonato.getEntrenadorId())) {
            throw new RecursoNoEncontradoException("No existe el entrenador con id " + campeonato.getEntrenadorId());
        }
    }
}
