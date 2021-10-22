package org.lukitasedits.animecalificaciones.model.elements.sinId;

import org.lukitasedits.animecalificaciones.annotations.Descriptible;
import org.lukitasedits.animecalificaciones.model.elements.conId.Anime;
import org.lukitasedits.animecalificaciones.model.elements.conId.Categoria;

import java.util.Objects;

public class AnimeCategoria {
    @Descriptible(isRelacional = true)
    private int id_anime;
    @Descriptible(isRelacional = true)
    private int id_categoria;
    private Anime anime;
    private Categoria categoria;

    public AnimeCategoria() {
    }

    public AnimeCategoria(int id_anime, int id_categoria) {
        this.id_anime = id_anime;
        this.id_categoria = id_categoria;
    }

    public int getId_anime() {
        return id_anime;
    }

    public void setId_anime(int id_anime) {
        this.id_anime = id_anime;
    }

    public int getId_categoria() {
        return id_categoria;
    }

    public void setId_categoria(int id_categoria) {
        this.id_categoria = id_categoria;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnimeCategoria that = (AnimeCategoria) o;
        return id_anime == that.id_anime && id_categoria == that.id_categoria;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id_anime, id_categoria);
    }


}
