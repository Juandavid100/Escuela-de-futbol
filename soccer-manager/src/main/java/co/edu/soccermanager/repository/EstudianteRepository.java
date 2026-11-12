package co.edu.soccermanager.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import co.edu.soccermanager.modelo.EstadoEstudiante;
import co.edu.soccermanager.modelo.Estudiante;

/** Capa de datos: acceso a la colección de Estudiante en MongoDB. */
@Repository
public interface EstudianteRepository extends MongoRepository<Estudiante, String> {

    boolean existsByDocumento(String documento);
    List<Estudiante> findByGrupoIdAndEstado(String grupoId, EstadoEstudiante estado);
    long countByGrupoIdAndEstado(String grupoId, EstadoEstudiante estado);
    List<Estudiante> findByAcudienteIdsContaining(String acudienteId);
    boolean existsByAcudienteIdsContaining(String acudienteId);
}
