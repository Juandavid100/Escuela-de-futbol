package co.edu.soccermanager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.soccermanager.modelo.Entrenador;

/** Capa de datos: acceso a la colección de Entrenador en MongoDB. */
@Repository
public interface EntrenadorRepository extends MongoRepository<Entrenador, String> {

    boolean existsByDocumento(String documento);
}
