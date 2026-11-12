package co.edu.soccermanager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.soccermanager.modelo.Categoria;

/** Capa de datos: acceso a la colección de Categoria en MongoDB. */
@Repository
public interface CategoriaRepository extends MongoRepository<Categoria, String> {

    boolean existsByNombreAndTemporada(String nombre, int temporada);
}
