package org.lukitasedits.animecalificaciones.model.elements.conId;

import org.lukitasedits.animecalificaciones.annotations.Descriptible;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Anime extends ElementosConId {
    @Descriptible (isString = true)
    private String nombre;
    @Descriptible
    private float calificacion_promedio;
    private List<Categoria> categorias;

    public Anime(String nombre) {
        categorias = new ArrayList<>();
        this.nombre = nombre;
        this.calificacion_promedio = 0.0f;
    }

    public Anime(String nombre, List<Categoria> categorias) {
        this.nombre = nombre;
        this.categorias = categorias;
        this.calificacion_promedio = 0.0f;
    }

    public Anime(int id, String nombre, List<Categoria> categorias) {
        this(nombre);
        this.id = id;
        this.categorias = categorias;
    }

    public Anime(int id, String nombre, float calificacion_promedio) {
        this.id = id;
        this.nombre = nombre;
        this.calificacion_promedio = calificacion_promedio;
        categorias = new ArrayList<>();
    }

    public Anime(int id, String nombre, List<Categoria> categorias, float calificiacion_promedio) {
        this(id, nombre, categorias);
        this.calificacion_promedio = calificiacion_promedio;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public float getCalificacion_promedio() {
        return calificacion_promedio;
    }

    public void setCalificacion_promedio(float calificacion_promedio) {
        this.calificacion_promedio = calificacion_promedio;
    }

    public List<Categoria> getCategorias() {
        return categorias;
    }

    public void setCategorias(List<Categoria> categorias) {
        this.categorias = categorias;
    }

    public List<Categoria> addCategoria(String categoria){
        Categoria c = new Categoria(categoria);
        this.categorias.add(c);
        return this.categorias;
    }

    public List<Categoria> addCategoria(Categoria c){
        this.categorias.add(c);
        return this.categorias;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Anime anime = (Anime) o;
        return id == anime.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return   id + " |  "
                + nombre + " | " +
                + calificacion_promedio ;
    }
}
