package co.edu.soccermanager.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.edu.soccermanager.excepcion.ConflictoException;
import co.edu.soccermanager.excepcion.DatosInvalidosException;
import co.edu.soccermanager.excepcion.RecursoNoEncontradoException;
import co.edu.soccermanager.modelo.Categoria;
import co.edu.soccermanager.repository.CategoriaRepository;
import co.edu.soccermanager.repository.GrupoRepository;

@Service
public class CategoriaService implements ICategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    @Override
    public List<Categoria> listar() {
        return categoriaRepository.findAll();
    }

    @Override
    public Categoria buscarPorId(String id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("No existe la categoría con id " + id));
    }

    @Override
    public Categoria crear(Categoria categoria) {
        validar(categoria);
        if (categoriaRepository.existsByNombreAndTemporada(categoria.getNombre(), categoria.getTemporada())) {
            throw new ConflictoException("Ya existe la categoría " + categoria.getNombre() + " en la temporada " + categoria.getTemporada());
        }
        categoria.setId(null);
        return categoriaRepository.save(categoria);
    }

    @Override
    public Categoria actualizar(String id, Categoria datos) {
        Categoria actual = buscarPorId(id);
        validar(datos);
        boolean cambiaClave = !actual.getNombre().equals(datos.getNombre()) || actual.getTemporada() != datos.getTemporada();
        if (cambiaClave && categoriaRepository.existsByNombreAndTemporada(datos.getNombre(), datos.getTemporada())) {
            throw new ConflictoException("Ya existe la categoría " + datos.getNombre() + " en la temporada " + datos.getTemporada());
        }
        actual.setNombre(datos.getNombre());
        actual.setEdadMinima(datos.getEdadMinima());
        actual.setEdadMaxima(datos.getEdadMaxima());
        actual.setTemporada(datos.getTemporada());
        actual.setFechaCorte(datos.getFechaCorte());
        return categoriaRepository.save(actual);
    }

    @Override
    public void eliminar(String id) {
        buscarPorId(id);
        if (grupoRepository.existsByCategoriaId(id)) {
            throw new ConflictoException("No se puede eliminar: la categoría tiene grupos asociados");
        }
        categoriaRepository.deleteById(id);
    }

    private void validar(Categoria categoria) {
        Validacion.presente(categoria, "cuerpo de la petición");
        Validacion.texto(categoria.getNombre(), "nombre");
        Validacion.presente(categoria.getFechaCorte(), "fechaCorte");
        if (categoria.getEdadMinima() < 0) {
            throw new DatosInvalidosException("La edad mínima no puede ser negativa");
        }
        if (!categoria.rangoValido()) {
            throw new DatosInvalidosException("La edad mínima no puede ser mayor que la edad máxima");
        }
    }
}
