package org.lukitasedits.animecalificaciones.model.elements.conId;

import org.lukitasedits.animecalificaciones.annotations.Descriptible;

import java.util.Objects;

public class Usuario extends ElementosConId {
    @Descriptible(isString = true)
    private String nombre;
    @Descriptible(isString = true)
    private String apellido;
    @Descriptible(isString = true)
    private String password;
    @Descriptible(isString = true)
    private String mail;

    public Usuario(int id, String nombre, String apellido, String password, String mail) {
        this.id = id;
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
        this.mail = mail;
    }

    public Usuario(String nombre, String apellido, String password, String mail) {
        this.nombre = nombre;
        this.apellido = apellido;
        this.password = password;
        this.mail = mail;
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

    public String getApellido() {
        return apellido;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return id == usuario.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + " | " +
                nombre + " | " +
                apellido + " | " +
                password + " | " +
                mail;
    }
}
