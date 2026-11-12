package co.edu.soccermanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.soccermanager.excepcion.ConflictoException;
import co.edu.soccermanager.excepcion.RecursoNoEncontradoException;
import co.edu.soccermanager.modelo.Entrenador;
import co.edu.soccermanager.repository.CampeonatoRepository;
import co.edu.soccermanager.repository.EntrenadorRepository;
import co.edu.soccermanager.repository.GrupoRepository;

@Service
public class EntrenadorService implements IEntrenadorService {

    @Autowired
    private EntrenadorRepository entrenadorRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Autowired
    private CampeonatoRepository campeonatoRepository;

    @Override
    public List<Entrenador> listar() {
        return entrenadorRepository.findAll();
    }

    @Override
    public Entrenador buscarPorId(String id) {
        return entrenadorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el entrenador con id " + id));
    }

    @Override
    public Entrenador crear(Entrenador entrenador) {
        Validacion.persona(entrenador);
        if (entrenadorRepository.existsByDocumento(entrenador.getDocumento())) {
            throw new ConflictoException("Ya existe un entrenador con el documento " + entrenador.getDocumento());
        }
        entrenador.setId(null);
        return entrenadorRepository.save(entrenador);
    }

    @Override
    public Entrenador actualizar(String id, Entrenador datos) {
        Entrenador actual = buscarPorId(id);
        Validacion.persona(datos);
        if (!actual.getDocumento().equals(datos.getDocumento())
                && entrenadorRepository.existsByDocumento(datos.getDocumento())) {
            throw new ConflictoException("Ya existe un entrenador con el documento " + datos.getDocumento());
        }
        actual.setDocumento(datos.getDocumento());
        actual.setNombres(datos.getNombres());
        actual.setApellidos(datos.getApellidos());
        actual.setTelefono(datos.getTelefono());
        actual.setCorreo(datos.getCorreo());
        return entrenadorRepository.save(actual);
    }

    @Override
    public Entrenador cambiarPermisos(String id, boolean puedeConvocar, boolean puedeModificarActividades) {
        Entrenador entrenador = buscarPorId(id);
        entrenador.setPuedeConvocar(puedeConvocar);
        entrenador.setPuedeModificarActividades(puedeModificarActividades);
        return entrenadorRepository.save(entrenador);
    }

    @Override
    public void eliminar(String id) {
        buscarPorId(id);
        if (grupoRepository.existsByEntrenadorIdsContaining(id) || campeonatoRepository.existsByEntrenadorId(id)) {
            throw new ConflictoException("No se puede eliminar: el entrenador tiene grupos o campeonatos asignados");
        }
        entrenadorRepository.deleteById(id);
    }
}
