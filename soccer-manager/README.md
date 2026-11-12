# Soccer Manager: API REST

API para administrar la escuela de fútbol, hecha con **Spring Boot 3** y **MongoDB**. Se prueba con **Postman**.

## Cómo ejecutarla

Requisitos: Java 17 o superior, Maven y una base MongoDB (local o en MongoDB Atlas).

```bash
cd soccer-manager

# Con MongoDB instalado en el computador (mongodb://localhost:27017/soccermanager)
mvn spring-boot:run

# Con MongoDB Atlas
MONGODB_URI="mongodb+srv://usuario:clave@cluster.mongodb.net/soccermanager" mvn spring-boot:run
```

En Windows (PowerShell) la variable se pone así: `$env:MONGODB_URI="mongodb+srv://..."`, y luego `mvn spring-boot:run`.

La API queda en `http://localhost:8080/api`. Para generar el ejecutable: `mvn package`, que crea `target/soccermanager.jar`. Se corre con `java -jar target/soccermanager.jar`.

## Probarla con Postman

1. En Postman, haga clic en **Import** y elija el archivo [`postman/SoccerManager.postman_collection.json`](postman/SoccerManager.postman_collection.json).
2. Con la API encendida, abra la colección y haga clic en **Run**. Las 52 peticiones se ejecutan en orden.
3. Cada petición guarda los `id` que necesitan las siguientes y verifica el código HTTP esperado. También incluye los casos de error: 400, 401, 403, 404 y 409.

Puede ejecutar la colección todas las veces que quiera, porque genera documentos y correos nuevos en cada corrida.

## Arquitectura por capas

Cada capa solo llama a la de abajo, nunca al revés:

```
controller  (presentación)  →  service  (negocio)  →  repository  (datos)  →  MongoDB
                 dto                excepcion
                         modelo (transversal)
```

| Paquete | Qué contiene |
|---|---|
| `modelo` | Las clases del dominio con sus atributos, constructores, getters y setters. `@Document` indica la colección de MongoDB. |
| `repository` | Una interfaz `XRepository extends MongoRepository` por cada documento. Las consultas se crean a partir del nombre del método, por ejemplo `countByGrupoIdAndEstado`. |
| `service` | Una interfaz `IXService` y su implementación `XService` por entidad. Aquí van **todas las reglas de negocio** de los criterios de aceptación. |
| `controller` | Un `@RestController` por entidad en `/api/{recurso}`, y `ManejadorExcepciones`, que convierte los errores en códigos HTTP. |
| `dto` | Objetos pequeños para las acciones que no son un CRUD: retiro, reprogramación, pago, login, etc. |
| `excepcion` | Excepciones del negocio. Cada una sabe qué código HTTP le corresponde. |

`Ubicacion` y `Pago` no tienen repositorio porque se guardan **embebidos** dentro de `Grupo`/`Actividad` y de `Mensualidad`. En MongoDB no viven solos.

## Modelo de herencia

- `Persona` es abstracta y de ella heredan `Estudiante`, `Acudiente` y `Entrenador`.
- `Usuario` es la cuenta de acceso. Tiene un conjunto de `Rol` (GERENTE, ENTRENADOR, ACUDIENTE, ESTUDIANTE) y apunta a su persona con `personaId`.
- `Actividad` es abstracta y de ella heredan `Entrenamiento` y `Partido`. La asistencia funciona igual para las dos.

## Patrones GRASP en el código

| Patrón | Dónde |
|---|---|
| Controlador | Los `XController` reciben las peticiones y delegan en el servicio, sin lógica de negocio. |
| Experto en información | `Mensualidad.getSaldo()`, `Categoria.aplicaPara()`, `Grupo.tieneCupo()`, `Evaluacion.getPromedio()`: cada cálculo está en la clase que tiene los datos. |
| Creador | `Mensualidad.registrarPago()` crea sus `Pago`, porque los contiene. |
| Polimorfismo | `Actividad` y sus subclases; `SoccerManagerException.getEstadoHttp()`, que cada excepción responde a su manera. |
| Fabricación pura | `NotificacionService` y `Validacion` no son conceptos del dominio; existen para no repetir código. |
| Indirección | Los servicios no se comunican con la persona: se lo piden a `INotificacionService`. |
| Bajo acoplamiento / Variaciones protegidas | Los controladores dependen de interfaces `IXService`, no de las clases concretas. |
| Alta cohesión | Cada servicio se ocupa de una sola entidad. |

## Endpoints

| Recurso | Métodos |
|---|---|
| `/api/categorias` | `GET`, `GET /{id}`, `POST`, `PUT /{id}`, `DELETE /{id}` |
| `/api/acudientes` | CRUD, `GET /{id}/estudiantes` |
| `/api/estudiantes` | CRUD, `PATCH /{id}/grupo`, `PATCH /{id}/retiro` |
| `/api/entrenadores` | CRUD, `PATCH /{id}/permisos` |
| `/api/usuarios` | CRUD, `POST /login`, `PATCH /{id}/roles`, `PATCH /{id}/estado` |
| `/api/grupos` | CRUD, `GET /{id}/estudiantes`, `PATCH` y `DELETE /{id}/entrenadores/{entrenadorId}` |
| `/api/entrenamientos` | CRUD (`?grupoId=`), `PATCH /{id}/reprogramacion`, `PATCH /{id}/cancelacion` |
| `/api/campeonatos` | CRUD, `PATCH /{id}/cancelacion` (también cancela sus partidos) |
| `/api/partidos` | CRUD (`?campeonatoId=`), `PATCH /{id}/reprogramacion`, `/cancelacion`, `/resultado` |
| `/api/convocatorias` | `GET` (`?partidoId=` o `?estudianteId=`), `POST`, `PATCH /{id}/respuesta`, `PATCH /{id}/retiro` |
| `/api/asistencias` | `GET` (`?actividadId=` o `?estudianteId=`), `POST`, `PATCH /{id}/correccion`, `GET /resumen/{estudianteId}` |
| `/api/evaluaciones` | CRUD (`?estudianteId=&soloVisibles=true`), `PATCH /{id}/publicacion` |
| `/api/mensualidades` | `GET`, `POST`, `DELETE`, `POST /{id}/pagos`, `PATCH /{id}/pagos/{indice}/anulacion` |
| `/api/notificaciones` | `GET ?personaId=`, `POST`, `PATCH /{id}/anulacion` |

## Códigos HTTP

| Código | Cuándo |
|---|---|
| 200 OK | Consulta, actualización o acción correcta. |
| 201 Created | Se creó un recurso: matrícula, grupo, pago, convocatoria... |
| 204 No Content | Se eliminó un recurso. |
| 400 Bad Request | Faltan datos o se rompe una regla: menor sin acudiente, edades al revés, pago mayor al saldo, corrección sin motivo. |
| 401 Unauthorized | Correo o contraseña incorrectos. |
| 403 Forbidden | Cuenta suspendida o entrenador sin permiso para convocar. |
| 404 Not Found | El recurso o la ruta no existe. |
| 409 Conflict | Duplicados, grupo lleno, horario cruzado, eliminar algo que tiene historial. |
| 500 Internal Server Error | Error inesperado del servidor. |

Todos los errores responden con el cuerpo `{"error": "mensaje"}`.

## Pruebas

`mvn test` corre las pruebas unitarias con JUnit 5 y Mockito: `EstudianteControllerTest` y `EstudianteServiceTest`. No necesitan MongoDB.
