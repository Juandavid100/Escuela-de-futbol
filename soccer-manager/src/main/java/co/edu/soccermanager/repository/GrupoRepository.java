package co.edu.soccermanager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.soccermanager.modelo.Grupo;

/** Capa de datos: acceso a la colección de Grupo en MongoDB. */
@Repository
public interface GrupoRepository extends MongoRepository<Grupo, String> {

    boolean existsByCodigo(String codigo);
    boolean existsByCategoriaId(String categoriaId);
    boolean existsByEntrenadorIdsContaining(String entrenadorId);
}
