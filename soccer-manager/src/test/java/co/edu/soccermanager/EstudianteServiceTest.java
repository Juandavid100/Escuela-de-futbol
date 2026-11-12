package co.edu.soccermanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import co.edu.soccermanager.excepcion.ConflictoException;
import co.edu.soccermanager.excepcion.DatosInvalidosException;
import co.edu.soccermanager.modelo.Categoria;
import co.edu.soccermanager.modelo.EstadoEstudiante;
import co.edu.soccermanager.modelo.Estudiante;
import co.edu.soccermanager.modelo.Grupo;
import co.edu.soccermanager.repository.AcudienteRepository;
import co.edu.soccermanager.repository.CategoriaRepository;
import co.edu.soccermanager.repository.EstudianteRepository;
import co.edu.soccermanager.repository.GrupoRepository;
import co.edu.soccermanager.service.EstudianteService;
import co.edu.soccermanager.service.INotificacionService;

/** Prueba de las reglas de negocio del estudiante con los repositorios simulados. */
public class EstudianteServiceTest {

    @Mock
    private EstudianteRepository estudianteRepository;

    @Mock
    private AcudienteRepository acudienteRepository;

    @Mock
    private GrupoRepository grupoRepository;

    @Mock
    private CategoriaRepository categoriaRepository;

    @Mock
    private INotificacionService notificacionService;

    @InjectMocks
    private EstudianteService estudianteService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    private Estudiante nino() {
        return new Estudiante("200", "Juan", "Pérez", null, null, LocalDate.now().minusYears(9));
    }

    @Test
    public void testMenorSinAcudienteEsRechazado() {
        assertThrows(DatosInvalidosException.class, () -> estudianteService.crear(nino()));
    }

    @Test
    public void testMenorConAcudienteQuedaPendiente() {
        Estudiante estudiante = nino();
        estudiante.getAcudienteIds().add("acu1");
        when(acudienteRepository.existsById("acu1")).thenReturn(true);
        when(estudianteRepository.save(any(Estudiante.class))).thenAnswer(i -> i.getArgument(0));

        Estudiante guardado = estudianteService.crear(estudiante);

        assertEquals(EstadoEstudiante.PENDIENTE, guardado.getEstado());
    }

    @Test
    public void testDocumentoRepetidoEsConflicto() {
        Estudiante estudiante = nino();
        estudiante.getAcudienteIds().add("acu1");
        when(estudianteRepository.existsByDocumento("200")).thenReturn(true);

        assertThrows(ConflictoException.class, () -> estudianteService.crear(estudiante));
    }

    @Test
    public void testGrupoLlenoEsConflicto() {
        Estudiante estudiante = nino();
        estudiante.setId("est1");
        estudiante.setCategoriaId("cat1");
        Grupo grupo = new Grupo("S10-A", "cat1", 20, "L-M-V 4 p. m.", null);
        grupo.setId("g1");
        when(estudianteRepository.findById("est1")).thenReturn(Optional.of(estudiante));
        when(grupoRepository.findById("g1")).thenReturn(Optional.of(grupo));
        when(categoriaRepository.findById("cat1"))
                .thenReturn(Optional.of(new Categoria("Sub-10", 8, 10, 2026, LocalDate.now())));
        when(estudianteRepository.countByGrupoIdAndEstado("g1", EstadoEstudiante.ACTIVO)).thenReturn(20L);

        ConflictoException error = assertThrows(ConflictoException.class,
                () -> estudianteService.asignarGrupo("est1", "g1"));

        assertEquals("El grupo alcanzó su capacidad", error.getMessage());
    }
}
