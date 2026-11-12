package co.edu.soccermanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.soccermanager.excepcion.ConflictoException;
import co.edu.soccermanager.excepcion.RecursoNoEncontradoException;
import co.edu.soccermanager.modelo.Acudiente;
import co.edu.soccermanager.modelo.Estudiante;
import co.edu.soccermanager.repository.AcudienteRepository;
import co.edu.soccermanager.repository.EstudianteRepository;

@Service
public class AcudienteService implements IAcudienteService {

    @Autowired
    private AcudienteRepository acudienteRepository;

    @Autowired
    private EstudianteRepository estudianteRepository;

    @Override
    public List<Acudiente> listar() {
        return acudienteRepository.findAll();
    }

    @Override
    public Acudiente buscarPorId(String id) {
        return acudienteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe el acudiente con id " + id));
    }

    @Override
    public List<Estudiante> listarEstudiantes(String id) {
        buscarPorId(id);
        return estudianteRepository.findByAcudienteIdsContaining(id);
    }

    @Override
    public Acudiente crear(Acudiente acudiente) {
        Validacion.persona(acudiente);
        Validacion.texto(acudiente.getTelefono(), "telefono");
        if (acudienteRepository.existsByDocumento(acudiente.getDocumento())) {
            throw new ConflictoException("Ya existe un acudiente con el documento " + acudiente.getDocumento());
        }
        acudiente.setId(null);
        return acudienteRepository.save(acudiente);
    }

    @Override
    public Acudiente actualizar(String id, Acudiente datos) {
        Acudiente actual = buscarPorId(id);
        Validacion.persona(datos);
        Validacion.texto(datos.getTelefono(), "telefono");
        if (!actual.getDocumento().equals(datos.getDocumento())
                && acudienteRepository.existsByDocumento(datos.getDocumento())) {
            throw new ConflictoException("Ya existe un acudiente con el documento " + datos.getDocumento());
        }
        actual.setDocumento(datos.getDocumento());
        actual.setNombres(datos.getNombres());
        actual.setApellidos(datos.getApellidos());
        actual.setTelefono(datos.getTelefono());
        actual.setCorreo(datos.getCorreo());
        actual.setParentesco(datos.getParentesco());
        return acudienteRepository.save(actual);
    }

    @Override
    public void eliminar(String id) {
        buscarPorId(id);
        if (estudianteRepository.existsByAcudienteIdsContaining(id)) {
            throw new ConflictoException("No se puede eliminar: el acudiente tiene estudiantes a cargo");
        }
        acudienteRepository.deleteById(id);
    }
}
