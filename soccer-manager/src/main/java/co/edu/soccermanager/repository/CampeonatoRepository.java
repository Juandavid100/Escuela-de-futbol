package co.edu.soccermanager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.soccermanager.modelo.Campeonato;

/** Capa de datos: acceso a la colección de Campeonato en MongoDB. */
@Repository
public interface CampeonatoRepository extends MongoRepository<Campeonato, String> {

    boolean existsByEntrenadorId(String entrenadorId);
}
