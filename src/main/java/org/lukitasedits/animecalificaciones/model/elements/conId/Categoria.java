package org.lukitasedits.animecalificaciones.model.elements.conId;

import org.lukitasedits.animecalificaciones.annotations.Descriptible;

import java.util.Objects;

public class Categoria extends ElementosConId {
    @Descriptible(isString = true)
    private String nombre;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }

    public Categoria(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Categoria categoria = (Categoria) o;
        return Objects.equals(nombre, categoria.nombre);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nombre);
    }

    @Override
    public String toString() {
        return id + " | "
                + nombre;
    }
}