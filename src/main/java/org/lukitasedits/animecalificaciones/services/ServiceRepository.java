package org.lukitasedits.animecalificaciones.services;

import org.lukitasedits.animecalificaciones.Exceptions.AnimeSinCategoriasException;
import org.lukitasedits.animecalificaciones.Exceptions.CalificacionFueraDeLimitesException;
import org.lukitasedits.animecalificaciones.Exceptions.ElementoInexistenteException;
import org.lukitasedits.animecalificaciones.Exceptions.ElementoYaRegistradoException;
import org.lukitasedits.animecalificaciones.model.elements.Elementos;
import org.lukitasedits.animecalificaciones.model.elements.conId.Anime;
import org.lukitasedits.animecalificaciones.model.elements.conId.Categoria;
import org.lukitasedits.animecalificaciones.model.elements.conId.Usuario;
import org.lukitasedits.animecalificaciones.model.elements.sinId.AnimeCategoria;
import org.lukitasedits.animecalificaciones.model.elements.sinId.UsuariosAnimes;
import org.lukitasedits.animecalificaciones.model.structure.Tablas;
import org.lukitasedits.animecalificaciones.repositories.Repositorio;
import org.lukitasedits.animecalificaciones.repositories.RepositoryConId;
import org.lukitasedits.animecalificaciones.repositories.RepositorySinId;
import org.lukitasedits.animecalificaciones.util.ConexionBaseDatos;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;

public class ServiceRepository {
    private Connection getConnection() throws SQLException {
        return ConexionBaseDatos.getConnection();
    }

    public boolean todasLasTablasEstanVacias() throws SQLException{
        boolean retorno = true;
        try(Connection conn = getConnection()){
            if(conn.getAutoCommit()){ conn.setAutoCommit(false);}
            try{
                //instanciamos un repositorio genérico y le vinculamos la tabla "usuarios" desde parámetro.
                Repositorio<Elementos> userRepository = new Repositorio<>(Tablas.USUARIOS, conn);
                //instanciamos un repositorio genérico y le vinculamos la tabla "animes" desde parámetro.
                Repositorio<Elementos> animeRepository = new Repositorio<>(Tablas.ANIMES, conn);
                //instanciamos un repositorio genérico y le vinculamos la tabla "categorías" desde parámetro.
                Repositorio<Elementos> categoriasRepository = new Repositorio<>(Tablas.CATEGORIAS, conn);
                //instanciamos un repositorio genérico y le vinculamos la tabla "usuarios-animes" desde parámetro.
                Repositorio<Elementos> usuAniRepository = new Repositorio<>(Tablas.USUARIOS_ANIMES, conn);
                //instanciamos un repositorio genérico y le vinculamos la tabla "animes-categorías" desde parámetro.
                Repositorio<Elementos> aniCatRepository = new Repositorio<>(Tablas.ANIMES_CATEGORIAS, conn);
                if(!userRepository.isEmpty() || !animeRepository.isEmpty() || !categoriasRepository.isEmpty() ||
                        !usuAniRepository.isEmpty() || !aniCatRepository.isEmpty()){
                    retorno = false;
                }
                conn.commit();
            } catch (SQLException e){
                conn.rollback();
                e.printStackTrace();
            }
        }
        return retorno;
    }

    public boolean laTablaEstaVacia(Tablas table) throws SQLException {
        boolean retorno = false;
        try(Connection conn = getConnection()){
            if(conn.getAutoCommit()){ conn.setAutoCommit(false);}
            try{
                //instanciamos un repositorio genérico y le vinculamos la tabla desde parámetro.
                Repositorio<Elementos> repository = new Repositorio<>(table, conn);
                //comprobamos si está vacía
                retorno = repository.isEmpty();
                conn.commit();
            } catch (SQLException e){
                conn.rollback();
                e.printStackTrace();
            }
        }
        return  retorno;
    }

    public void reiniciarTabla(Tablas table) throws SQLException {
        try(Connection conn = getConnection()){
            if(conn.getAutoCommit()){ conn.setAutoCommit(false);}
            try{
                //instanciamos un repositorio genérico y le vinculamos la tabla desde parámetro.
                Repositorio<Elementos> repository = new Repositorio<>(table, conn);
                repository.truncate(); //ejecutamos el TRUNCATE en la tabla asignada
                conn.commit();
            } catch (SQLException e){
                conn.rollback();
                e.printStackTrace();
            }
        }
    }

    public void vaciarTodasLasTablas() throws SQLException{
        try(Connection conn = getConnection()){
            if(conn.getAutoCommit()){ conn.setAutoCommit(false);}
            try{
                //instanciamos un repositorio genérico y le vinculamos la tabla "usuarios" desde parámetro.
                Repositorio<Elementos> userRepository = new Repositorio<>(Tablas.USUARIOS, conn);
                userRepository.truncate(); //ejecutamos el TRUNCATE en la tabla asignada
                //instanciamos un repositorio genérico y le vinculamos la tabla "animes" desde parámetro.
                Repositorio<Elementos> animeRepository = new Repositorio<>(Tablas.ANIMES, conn);
                animeRepository.truncate(); //ejecutamos el TRUNCATE en la tabla asignada
                //instanciamos un repositorio genérico y le vinculamos la tabla "categorías" desde parámetro.
                Repositorio<Elementos> categoriasRepository = new Repositorio<>(Tablas.CATEGORIAS, conn);
                categoriasRepository.truncate(); //ejecutamos el TRUNCATE en la tabla asignada
                //instanciamos un repositorio genérico y le vinculamos la tabla "usuarios-animes" desde parámetro.
                Repositorio<Elementos> usuAniRepository = new Repositorio<>(Tablas.USUARIOS_ANIMES, conn);
                usuAniRepository.truncate(); //ejecutamos el TRUNCATE en la tabla asignada
                //instanciamos un repositorio genérico y le vinculamos la tabla "animes-categorías" desde parámetro.
                Repositorio<Elementos> aniCatRepository = new Repositorio<>(Tablas.ANIMES_CATEGORIAS, conn);
                aniCatRepository.truncate(); //ejecutamos el TRUNCATE en la tabla asignada
                conn.commit();
            } catch (SQLException e){
                conn.rollback();
                e.printStackTrace();
            }
        }
    }

    public void registrarUser(String nombre, String apellido, String password, String mail) throws SQLException {
        try(Connection conn = getConnection()){
            if(conn.getAutoCommit()){ conn.setAutoCommit(false);}
            try{
                //instanciamos el repositorio para la tabla "Usuario".
                RepositoryConId<Usuario> userRepository = new RepositoryConId<>(Tablas.USUARIOS, conn);
                //Creamos un nuevo Usuario a partir de la información dada por parámetro.
                Usuario newUsuario = new Usuario(nombre, apellido, password, mail);
                //si no encuentra un usuario en la tabla con el mismo mail que el solicitado...
                if(userRepository.porCampo(Tablas.USUARIOS.getColumna("mail"), mail).size() == 0){
                    userRepository.almacenar(newUsuario); //se almacena en la tabla el usuario
                } else { //si se encuentra un usuario con ese mail...
                    conn.rollback();
                    //lanza error aclarando que el mail ya está registrado.
                    throw new ElementoYaRegistradoException("Ese mail ya está registrado");
                }
                conn.commit();
            } catch (SQLException e){
                conn.rollback();
                e.printStackTrace();
            }
        }
    }

    public Usuario extraerUser(String mail) throws SQLException {
        Usuario user = null; //creamos e inicializamos un objeto Usuario para retornarlo fuera del contexto try
        try(Connection conn = getConnection()){
            if(conn.getAutoCommit()){ conn.setAutoCommit(false);}
            try{
                //instanciamos el repositorio correspondiente a la tabla "usuarios".
                RepositoryConId<Usuario> userRepository = new RepositoryConId<>(Tablas.USUARIOS, conn);
                user = userRepository.porCampo(Tablas.USUARIOS.getColumna("mail"), mail).stream()
                        .findFirst().get(); //si el mail está registrado retorna el usuario, sino tira error.
            } catch (SQLException e){
                conn.rollback();
                e.printStackTrace();
            } catch (NoSuchElementException e){ //si se tiró error por no haber encontrado el mail...
                conn.rollback();
                //lanzamos un nuevo error aclarando la situación.
                throw new ElementoInexistenteException("Ese mail no está registrado.");
            }
        }
        return user;
    }

    public void eliminarUser(String mail) throws SQLException {
        try(Connection conn = getConnection()){
            if(conn.getAutoCommit()){ conn.setAutoCommit(false);}
            try{
                //se instancias los repositorios.
                RepositoryConId<Usuario> userRepository = new RepositoryConId<>(Tablas.USUARIOS, conn);
                RepositorySinId<UsuariosAnimes> userAniRepository = new RepositorySinId<>(Tablas.USUARIOS_ANIMES, conn);
                //si encuentra un usuario correspondiente al nombre en la tabla otorgado devuelve su id, sino 0.
                int id = userRepository.porCampo(Tablas.USUARIOS.getColumna("mail"), mail).stream()
                        .findFirst().map(Usuario::getId).orElse(0);
                if(id == 0) { //si el id es 0 (no se encontró el mail en la tabla)
                    conn.rollback();
                    //tira error aclarando que el mail no está registrado.
                    throw new ElementoInexistenteException("Ese mail no está registrado.");
                } else { //si el id es mayor a 0 (se encontró el usuario en la tabla)...
                    userRepository.remove(id); //elimina el registro de la tabla "usuarios".
                    //también se eliminan todos los registros correspondientes al id del usuario en la tabla "usuarios-animes".
                    userAniRepository.removePorColumna(Tablas.USUARIOS_ANIMES.getColumna("id_usuario"), id);
                }
                conn.commit();
            } catch (SQLException e){
                conn.rollback();
                e.printStackTrace();
            }
        }
    }

    public void agregarCategoria(String nombre) throws SQLException {
        try(Connection conn = getConnection()){
            if(conn.getAutoCommit()){ conn.setAutoCommit(false);}
            try{
                //instanciamos los repositorios.
                RepositoryConId<Categoria> categoriaRepository = new RepositoryConId<>(Tablas.CATEGORIAS, conn);
                //Creamos un objeto Categoría a partir del nombre pasado por parámetro.
                Categoria newCategoria =new Categoria(nombre.toLowerCase());
                //si no se encuentra ninguna categoría  con el nombre dado en la tabla "categorias"...
                if(categoriaRepository.porCampo(Tablas.CATEGORIAS.getColumna("nombre"), nombre).size() == 0){
                    categoriaRepository.almacenar(newCategoria); //se agrega la nueva categoría.
                } else { //si se encuentra en la tabla...
                    conn.rollback();
                    //tira error aclarando que la categoría ya está registrada.
                    throw new ElementoYaRegistradoException("Esa categoría ya está registrado");
                }
                conn.commit();
            } catch (SQLException e){
                conn.rollback();
                e.printStackTrace();
            }
        }
    }

    public void eliminarCategoria(String nombre) throws SQLException {
        try(Connection conn = getConnection()){
            if(conn.getAutoCommit()){ conn.setAutoCommit(false);}
            try{
                //se instancian los repositorios
                RepositoryConId<Categoria> categoriaRepository = new RepositoryConId<>(Tablas.CATEGORIAS, conn);
                RepositorySinId<AnimeCategoria> animeCategoriaRepository = new RepositorySinId<>(
                        Tablas.ANIMES_CATEGORIAS, conn);
                //si encuentra en la tabla una categoría correspondiente a dicho nombre almacena el id, sino retorna 0.
                int id = categoriaRepository.porCampo(Tablas.CATEGORIAS.getColumna("nombre"), nombre).stream()
                        .findFirst().map(Categoria::getId).orElse(0);
                if(id == 0) { //si el id es 0 (no se encontró la categoría en la tabla)...
                    conn.rollback();
                    //tira el error aclarando que no se encontró dicha categoría.
                    throw new ElementoInexistenteException("No se encontró la categoría\"" + nombre + "\"");
                } else { //si el id es mayor a 0 (se encontró la categoría)...
                    if(animeCategoriaRepository.porCampo(Tablas.ANIMES_CATEGORIAS.getColumna
                            ("id_categoria"), id).size() != 0){
                        throw new ElementoYaRegistradoException("No puedes eliminar una categoría vinculada a un animé.");
                    }
                    categoriaRepository.remove(id); //elimina la categoría de la tabla "categorías" a través del id.
                }
                conn.commit();
            } catch (SQLException e){
                conn.rollback();
                e.printStackTrace();
            }
        }
    }

    public Categoria extraerCategoria(String nombre) throws SQLException {
        Categoria cat = null;
        try(Connection conn = getConnection()){
            if(conn.getAutoCommit()){ conn.setAutoCommit(false);}
            try{
                //instanciamos el repositorio con la tabla "categorias".
                RepositoryConId<Categoria> categoriaRepository = new RepositoryConId<>(Tablas.CATEGORIAS, conn);
                cat = categoriaRepository.porCampo(Tablas.CATEGORIAS.getColumna("nombre"), nombre).stream()
                        .findFirst().get(); //se busca a través del nombre en la tabla "categorias", si no se encuentra
                //tira el error NoSuchElementException.
            } catch (SQLException e){
                conn.rollback();
                e.printStackTrace();
            } catch (NoSuchElementException e){ //si tira el error (no se encontró en la tabla la categoría).
                conn.rollback();
                //tira un error aclarando que no se encontró dicha categoría en la tabla.
                throw new ElementoInexistenteException("No se encontró la categoría\"" + nombre + "\"");
            }
        }
        return cat;
    }

    public void agregarAnime(String nombreAnime, String... nombreCategorias) throws SQLException {
        nombreAnime = nombreAnime.toLowerCase();
        try(Connection conn = getConnection()){
            if(conn.getAutoCommit()){ conn.setAutoCommit(false);}
            try{
                Categoria newCategoria; //creo un objeto categoría que pueda almacenar el nombre y el id en caso de que
                                        //el parámetro "categoría" no almacene el id.
                //inicializo los repositorios...
                RepositoryConId<Anime> animeRepository = new RepositoryConId<>(Tablas.ANIMES, conn);
                RepositoryConId<Categoria> categoriaRepository = new RepositoryConId<>(Tablas.CATEGORIAS, conn);
                RepositorySinId<AnimeCategoria> animeCategoriaRepository = new RepositorySinId<>
                        (Tablas.ANIMES_CATEGORIAS, conn);

                //creamos una lista a partir del vargars correspondiente a las categorías.
                List<Categoria> categorias = Arrays.stream(nombreCategorias).map(Categoria::new).toList();
                //si no está el animé registrado aún...
                if(animeRepository.porCampo(Tablas.ANIMES.getColumna("nombre"),
                        nombreAnime).size() == 0){
                    if(categorias.size() == 0) {
                        conn.rollback();
                        throw new AnimeSinCategoriasException("Este anime no tiene categorías vinculadas");
                    }

                //almacenamos y guardamos la información en un objeto nuevo con el id incluido.
                    Anime newAnime = animeRepository.almacenar(new Anime(nombreAnime));
                    //recorremos la lista de categorías.
                    for(int i = 0; i < categorias.size(); i++) {
                    //si la categoría actual no está registrada en la tabla "categorías"...
                    if (categoriaRepository.porCampo(Tablas.CATEGORIAS.getColumna("nombre"),
                            categorias.get(i).getNombre()).size() == 0) {
                        agregarCategoria(categorias.get(i).getNombre()); //se agrega automáticamente.
                    }
                    //almacenamos en el objeto newCategoría la información completa del registro de la tabla
                    newCategoria = extraerCategoria(categorias.get(i).getNombre());
                    //almacenamos en la tabla "anime-categorías" el anime y las categorías correspondientes.
                    animeCategoriaRepository.almacenar(new AnimeCategoria(newAnime.getId(), newCategoria.getId()));
                    }
                }else { //si ya está registrado el nombre del animé...
                    conn.rollback();
                    throw new ElementoYaRegistradoException("Ese animé ya está registrado.");
                }
                conn.commit();
            } catch (SQLException e){
                conn.rollback();
                e.printStackTrace();
            }
        }
    }

    public void eliminarAnime(String nombre) throws SQLException {
        nombre = nombre.toLowerCase();
        try(Connection conn = getConnection()){
            if(conn.getAutoCommit()){ conn.setAutoCommit(false);}
            try{
                //Instanciamos los repositorios
                RepositoryConId<Anime> animeRepository = new RepositoryConId<>(Tablas.ANIMES, conn);
                RepositorySinId<AnimeCategoria> animeCategoriaRepository = new RepositorySinId<>(
                        Tablas.ANIMES_CATEGORIAS, conn);
                //si encuentra un anime en la tabla correspondiente al nombre otorgado devuelve su id, sino 0.
                int id = animeRepository.porCampo(Tablas.ANIMES.getColumna("nombre"), nombre).stream()
                        .findFirst().map(Anime::getId).orElse(0);
                if(id == 0) { //si el id es 0 (no se encontró el animé en la tabla).
                    conn.rollback();
                    throw new ElementoInexistenteException("No se encontró el animé \"" + nombre + "\"");
                } else { //si el id es mayor a 0 (el animé se encontró)..
                    animeRepository.remove(id); //elimina el anime de la tabla "animes".
                    animeCategoriaRepository.removePorColumna(Tablas.ANIMES_CATEGORIAS.getColumna
                            ("id_anime"), id); //elimina todos los registros de la tabla "anime-categorías"
                    //  que correspondan al id del anime en la columna id_anime.
                }
                conn.commit();
            } catch (SQLException e){
                conn.rollback();
                e.printStackTrace();
            }
        }
    }

    public Anime extraerAnime(String nombre) throws SQLException {
        Anime anime = null;
        nombre = nombre.toLowerCase();
        try(Connection conn = getConnection()){
            if(conn.getAutoCommit()){ conn.setAutoCommit(false);}
            try{
                //instanciamos los repositorios con la tabla "animes".
                RepositoryConId<Anime> animeRepository = new RepositoryConId<>(Tablas.ANIMES, conn);
                RepositorySinId<AnimeCategoria> animeCategoriaRepository = new RepositorySinId<>
                        (Tablas.ANIMES_CATEGORIAS, conn);
                RepositoryConId<Categoria> categoriaRepository = new RepositoryConId<>(Tablas.CATEGORIAS, conn);
                //se busca a través del nombre en la tabla "animes",
                // si no se encuentra tira el error NoSuchElementException.
                anime =animeRepository.porCampo(Tablas.ANIMES.getColumna("nombre"), nombre).stream()
                        .findFirst().get();
                //le agregamos al objeto "anime" sus categorías correspondientes.
                animeCategoriaRepository.porCampo(Tablas.ANIMES_CATEGORIAS.getColumna("id_anime"),
                        anime.getId())
                        .stream().map(ac -> {
                            Categoria c = null;
                            try {
                            c = categoriaRepository.porId(ac.getId_categoria());
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            } return c;
                }).forEach(anime::addCategoria);
            } catch (SQLException e){
                conn.rollback();
                e.printStackTrace();
            } catch (NoSuchElementException e){ //si tira el error (no se encontró en la tabla la animes).
                conn.rollback();
                //tira un error aclarando que no se encontró el animé en la tabla.
                throw new ElementoInexistenteException("No se encontró el animé \"" + nombre + "\"");
            }
        }
        return  anime;
    }

    public void calificiarAnime(String mail, String nombreAnime, int calificacion) throws SQLException {
        if(calificacion > 10 || calificacion < 0){
            throw new CalificacionFueraDeLimitesException("La calificación permitida es entre 0 y 10 (ambos incluyentes).");
        }

        try(Connection conn = getConnection()){
            if(conn.getAutoCommit()){ conn.setAutoCommit(false);}
            try{
                RepositoryConId<Usuario> userRepository = new RepositoryConId<>(Tablas.USUARIOS, conn);
                RepositoryConId<Anime> animeRepository = new RepositoryConId<>(Tablas.ANIMES, conn);
                RepositorySinId<UsuariosAnimes> usuariosAnimesRepository = new RepositorySinId<>
                        (Tablas.USUARIOS_ANIMES, conn);
                Usuario user = extraerUser(mail);
                Anime anime = extraerAnime(nombreAnime);
                //almacenamos el registro que relaciona el usuario y el anime mediante sus respectivos id's.
                usuariosAnimesRepository.almacenar(new UsuariosAnimes(user.getId(), anime.getId(), calificacion));
                //le indicamos al objeto "anime" el número de calificaciones que se le hicieron a través de un conteo
                // de la cantidad de veces que aparece un registro con su id en la tabla usuarios-animes.
                int nCalificaciones = usuariosAnimesRepository.porCampo(
                        Tablas.USUARIOS_ANIMES.getColumna("id_anime"), anime.getId()).size();
                float resultado = ((anime.getCalificacion_promedio()
                        * (nCalificaciones-1)) + (float)calificacion)  /  nCalificaciones;
                anime.setCalificacion_promedio(resultado);
                animeRepository.almacenar(anime);
                conn.commit();
            } catch (SQLException e){
                conn.rollback();
                e.printStackTrace();
            }
        }
    }

}
