package co.edu.soccermanager.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.soccermanager.modelo.Mensualidad;

/** Capa de datos: acceso a la colección de Mensualidad en MongoDB. */
@Repository
public interface MensualidadRepository extends MongoRepository<Mensualidad, String> {

    boolean existsByEstudianteIdAndPeriodo(String estudianteId, String periodo);
    List<Mensualidad> findByEstudianteId(String estudianteId);
}
