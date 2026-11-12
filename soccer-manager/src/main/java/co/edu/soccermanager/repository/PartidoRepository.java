package co.edu.soccermanager.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.soccermanager.modelo.Partido;

/** Capa de datos: acceso a la colección de Partido en MongoDB. */
@Repository
public interface PartidoRepository extends MongoRepository<Partido, String> {

    List<Partido> findByCampeonatoId(String campeonatoId);
}
