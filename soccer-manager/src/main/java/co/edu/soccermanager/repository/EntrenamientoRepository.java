package co.edu.soccermanager.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.soccermanager.modelo.Entrenamiento;

/** Capa de datos: acceso a la colección de Entrenamiento en MongoDB. */
@Repository
public interface EntrenamientoRepository extends MongoRepository<Entrenamiento, String> {

    List<Entrenamiento> findByGrupoId(String grupoId);
    List<Entrenamiento> findByGrupoIdAndFechaInicioLessThanAndFechaFinGreaterThan(String grupoId, LocalDateTime fin, LocalDateTime inicio);
}
