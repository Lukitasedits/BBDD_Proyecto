package org.lukitasedits.animecalificaciones.services;

import org.junit.jupiter.api.*;
import org.lukitasedits.animecalificaciones.Exceptions.*;
import org.lukitasedits.animecalificaciones.model.elements.conId.*;

import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

class ServiceRepositoryTest {

    ServiceRepository serviceRepository;

    @BeforeEach
    void setUp() {
        serviceRepository = new ServiceRepository();
    }

    @AfterEach
    void tearDown() throws SQLException {
        //asumiendo que todas las tablas están vacías...
        assumingThat(serviceRepository.todasLasTablasEstanVacias(), () -> {
            //reiniciá su id con un truncate en cada tabla
            serviceRepository.vaciarTodasLasTablas();
        });
    }

    @Test
    @DisplayName("Manipulación de usuarios")
    void manipulacionUsuarioTest() throws SQLException {
        // registramos el usuario "mailEx1@gmail.com"
        serviceRepository.registrarUser("Nombre1", "Apellido1", "contraseña1",
                    "mailEx1@gmail.com");
        // intentamos registrar un usuario con distinta información pero con mismo mail
        assertThrows(ElementoYaRegistradoException.class, () -> {
            serviceRepository.registrarUser("Nombre2", "Apellido2", "contraseña2",
                    "mailEx1@gmail.com");
        });
        // intentamos eliminar un usuario inexistente
        assertThrows(ElementoInexistenteException.class, () -> {
            serviceRepository.eliminarUser("mailEx2@gmail.com");
        } );
        // extraemos la información del usuario con el mail "mailEx1@gmail.com"
        Usuario lucas =serviceRepository.extraerUser("mailEx1@gmail.com");
        assertEquals("Nombre1", lucas.getNombre());
        assertEquals("Apellido1", lucas.getApellido());
        assertEquals("contraseña1", lucas.getPassword());
        assertEquals("mailEx1@gmail.com", lucas.getMail());
        // eliminamos el registro correspondiente al usuario con el mail "mailEx1@gmail.com"
        serviceRepository.eliminarUser("mailEx1@gmail.com");
        // intentamos volver a extraer la información del usuario con el mail "mailEx1@gmail.com" con el regis-
        // tro ya eliminado para comprobar si salta el error.
        assertThrows(ElementoInexistenteException.class, () -> {
            serviceRepository.extraerUser("mailEx1@gmail.com");
        } );
    }

    @Test
    @DisplayName("Manipulación de categorías")
    void manipulacionCategoria() throws SQLException {
        // agregamos las categorías "categoría1", "categoría2" y "categoría3"
        serviceRepository.agregarCategoria("categoría1");
        serviceRepository.agregarCategoria("categoría2");
        serviceRepository.agregarCategoria("categoría3");
        // intentamos agregar una categoría ya existente y comprobamos si salta el error 'ElementoYaRegistradoException'
        assertThrows(ElementoYaRegistradoException.class, () -> {
            serviceRepository.agregarCategoria("categoría2");
        } );
        // extraemos la categoría "categoría3" de la tabla "categorías"
        Categoria comedia = serviceRepository.extraerCategoria("categoría3");
        // comprobamos que el registro extraído corresponde a los valores esperados
        assertEquals("categoría3", comedia.getNombre());
        // eliminamos la categoría "categoría2"
        serviceRepository.eliminarCategoria("categoría2");
        // intentamos extraer la información de la categoría "categoría2" y comprobamos si tira el error 'ElementoYaRegis-
        // tradoException'
        assertThrows(ElementoInexistenteException.class, () -> {
            serviceRepository.extraerCategoria("categoría2");
        } );
        //reiniciamos la información añadida en el Test.
        serviceRepository.eliminarCategoria("categoría1");
        serviceRepository.eliminarCategoria("categoría3");
    }

    @Test
    @DisplayName("Manipulación de Animes")
    void manipulcionAnimeTest() throws SQLException {
        //agregamos el anime "anime1" con las categorías "categoría1", "categoría2" y "categoría3".
        serviceRepository.agregarAnime(
                "anime1", "categoría1", "categoría2", "categoría3");
        //extraemos la información del registro correspondiente al anime "anime1"
        Anime yourName = serviceRepository.extraerAnime("anime1");
        //comprobamos que el objeto extraído contenga los datos correspondientes
        assertEquals("anime1", yourName.getNombre());
        assertTrue(yourName.getCategorias().contains(new Categoria("categoría1")));
        assertTrue(yourName.getCategorias().contains(new Categoria("categoría2")));
        assertTrue(yourName.getCategorias().contains(new Categoria("categoría3")));
        //Se intenta agregar un animé sin pasarle categorías para comprobar si salta el error 'AnimeSinCategoriasExcep-
        //tion'
        assertThrows(AnimeSinCategoriasException.class, () ->{
           serviceRepository.agregarAnime("Kimetsu no yaiba");
        });
        //eliminamos el registro correspondiente al anime con el nombre "anime1"
        serviceRepository.eliminarAnime("anime1");
        //intentamos extraer la información del registro eliminado anteriormente para comprobar si salta el error 'Ele-
        //mentoInexistenteException'
        assertThrows(ElementoInexistenteException.class, () ->{
            Anime yourName2 = serviceRepository.extraerAnime("anime1");
        } );
        //reiniciamos la información añadida en el Test
        serviceRepository.eliminarCategoria("categoría1");
        serviceRepository.eliminarCategoria("categoría2");
        serviceRepository.eliminarCategoria("categoría3");
    }

    @Test
    @DisplayName("Calificación de animes por usuario")
    void calificarAnimeTest() throws SQLException{
        //se agregan usuarios para poder calificar
        serviceRepository.registrarUser("Nombre1", "Apellido1", "contraseña1",
                "mailEx1@gmail.com");
        serviceRepository.registrarUser("Nombre2", "Apellido2", "contraseña2",
                "mailEx2@gmail.com");
        //se agrega el anime "AnimeEx" para ser calificado
        serviceRepository.agregarAnime("AnimeEx", "categoria1", "categoria2");
        //cada usuario califica "AnimeEx"
        serviceRepository.calificiarAnime("mailEx1@gmail.com", "AnimeEx", 6);
        serviceRepository.calificiarAnime("mailEx2@gmail.com", "AnimeEx", 10);
        //se extrae la información correspondiente al anime "AnimeEx"
        Anime onePiece = serviceRepository.extraerAnime("AnimeEx");
        //se comprueba que se haya almacenado correctamente el promedio dado por las calificaciones de los usuarios.
        assertEquals(8, onePiece.getCalificacion_promedio());
        //intentamos calificar el anime "AnimeEx" con un usuario que NO EXISTE.
        assertThrows(ElementoInexistenteException.class, () -> {
            serviceRepository.calificiarAnime("mailEx3@gmail.com", "AnimeEx", 9);
        });
        //intentamos calificar un anime no registrado con un usuario que sí.
        assertThrows(ElementoInexistenteException.class, () -> {
            serviceRepository.calificiarAnime("mailEx1@gmail.com", "AnimeEx2", 7);
        });
        //eliminamos la información añadida en el test
        serviceRepository.eliminarUser("mailEx1@gmail.com");
        serviceRepository.eliminarUser("mailEx2@gmail.com");
        serviceRepository.eliminarAnime("AnimeEx");
        serviceRepository.eliminarCategoria("categoria1");
        serviceRepository.eliminarCategoria("categoria2");
    }
}