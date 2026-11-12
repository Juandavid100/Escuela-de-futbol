package co.edu.soccermanager;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import co.edu.soccermanager.controller.EstudianteController;
import co.edu.soccermanager.modelo.Estudiante;
import co.edu.soccermanager.service.IEstudianteService;

/** Prueba del controlador aislado: el servicio se reemplaza por un simulacro (mock). */
public class EstudianteControllerTest {

    @Mock
    private IEstudianteService estudianteServiceMock;

    @InjectMocks
    private EstudianteController estudianteController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCrearEstudianteResponde201() {
        Estudiante estudiante = new Estudiante("200", "Juan", "Pérez", null, null, LocalDate.of(2017, 3, 1));
        when(estudianteServiceMock.crear(any(Estudiante.class))).thenReturn(estudiante);

        ResponseEntity<Estudiante> respuesta = estudianteController.crear(estudiante);

        assertEquals(HttpStatus.CREATED, respuesta.getStatusCode());
        assertEquals(estudiante, respuesta.getBody());
    }

    @Test
    public void testListarEstudiantesResponde200() {
        List<Estudiante> estudiantes = new ArrayList<>();
        when(estudianteServiceMock.listar()).thenReturn(estudiantes);

        ResponseEntity<List<Estudiante>> respuesta = estudianteController.listar();

        assertEquals(HttpStatus.OK, respuesta.getStatusCode());
        assertEquals(estudiantes, respuesta.getBody());
    }

    @Test
    public void testEliminarRespondeSinContenido() {
        ResponseEntity<Void> respuesta = estudianteController.eliminar("id1");

        assertEquals(HttpStatus.NO_CONTENT, respuesta.getStatusCode());
    }
}
