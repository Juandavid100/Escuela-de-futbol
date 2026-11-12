package co.edu.soccermanager.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.soccermanager.modelo.Evaluacion;

/** Capa de datos: acceso a la colección de Evaluacion en MongoDB. */
@Repository
public interface EvaluacionRepository extends MongoRepository<Evaluacion, String> {

    boolean existsByEstudianteIdAndPeriodo(String estudianteId, String periodo);
    List<Evaluacion> findByEstudianteId(String estudianteId);
    List<Evaluacion> findByEstudianteIdAndVisibleFamiliaTrue(String estudianteId);
}
