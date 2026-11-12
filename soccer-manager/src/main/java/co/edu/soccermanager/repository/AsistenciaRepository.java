package co.edu.soccermanager.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.soccermanager.modelo.Asistencia;

/** Capa de datos: acceso a la colección de Asistencia en MongoDB. */
@Repository
public interface AsistenciaRepository extends MongoRepository<Asistencia, String> {

    boolean existsByActividadIdAndEstudianteId(String actividadId, String estudianteId);
    boolean existsByActividadId(String actividadId);
    List<Asistencia> findByActividadId(String actividadId);
    List<Asistencia> findByEstudianteId(String estudianteId);
}
