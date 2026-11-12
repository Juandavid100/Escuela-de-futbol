package co.edu.soccermanager.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.soccermanager.modelo.Notificacion;

/** Capa de datos: acceso a la colección de Notificacion en MongoDB. */
@Repository
public interface NotificacionRepository extends MongoRepository<Notificacion, String> {

    List<Notificacion> findByPersonaIdOrderByFechaEnvioDesc(String personaId);
}
