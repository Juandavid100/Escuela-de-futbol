package co.edu.soccermanager.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.soccermanager.modelo.Convocatoria;

/** Capa de datos: acceso a la colección de Convocatoria en MongoDB. */
@Repository
public interface ConvocatoriaRepository extends MongoRepository<Convocatoria, String> {

    boolean existsByPartidoIdAndEstudianteId(String partidoId, String estudianteId);
    List<Convocatoria> findByPartidoId(String partidoId);
    List<Convocatoria> findByEstudianteId(String estudianteId);
}
