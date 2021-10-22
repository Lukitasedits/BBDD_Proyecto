package org.lukitasedits.animecalificaciones.model.elements.sinId;

import org.lukitasedits.animecalificaciones.annotations.Descriptible;
import org.lukitasedits.animecalificaciones.model.elements.conId.Anime;
import org.lukitasedits.animecalificaciones.model.elements.conId.Usuario;

import java.util.Objects;

public class UsuariosAnimes {
    @Descriptible(isRelacional = true)
    private int id_usuario;
    private Usuario usuario;
    @Descriptible(isRelacional = true)
    private int id_anime;
    private Anime anime;
    @Descriptible
    private int calificacion;

    public UsuariosAnimes(int id_usuario, int id_anime, int calificacion) {
        this.id_usuario = id_usuario;
        this.id_anime = id_anime;
        this.calificacion = calificacion;
    }

    public UsuariosAnimes(Usuario usuario, Anime anime, int calificacion) {
        this.usuario = usuario;
        this.anime = anime;
        this.calificacion = calificacion;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public int getId_anime() {
        return id_anime;
    }

    public void setId_anime(int id_anime) {
        this.id_anime = id_anime;
    }

    public Anime getAnime() {
        return anime;
    }

    public void setAnime(Anime anime) {
        this.anime = anime;
    }

    public int getCalificacion() {
        return calificacion;
    }

    public void setCalificacion(int calificacion) {
        this.calificacion = calificacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UsuariosAnimes that = (UsuariosAnimes) o;
        return id_usuario == that.id_usuario && id_anime == that.id_anime;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_usuario, id_anime);
    }

    @Override
    public String toString() {
        return   usuario.getNombre() + " | " +
                anime.getNombre() + " | " +
                calificacion;
    }
}
