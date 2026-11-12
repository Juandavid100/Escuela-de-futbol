package co.edu.soccermanager.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.soccermanager.modelo.Acudiente;

/** Capa de datos: acceso a la colección de Acudiente en MongoDB. */
@Repository
public interface AcudienteRepository extends MongoRepository<Acudiente, String> {

    boolean existsByDocumento(String documento);
}
