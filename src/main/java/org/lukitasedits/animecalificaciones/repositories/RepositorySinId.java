package org.lukitasedits.animecalificaciones.repositories;

import org.lukitasedits.animecalificaciones.annotations.Descriptible;
import org.lukitasedits.animecalificaciones.model.elements.sinId.AnimeCategoria;
import org.lukitasedits.animecalificaciones.model.elements.sinId.UsuariosAnimes;
import org.lukitasedits.animecalificaciones.model.structure.Columna;
import org.lukitasedits.animecalificaciones.model.structure.Tablas;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RepositorySinId<T> extends Repositorio{

    public RepositorySinId(Tablas table, Connection conn) {
        super(table, conn);
    }

    public T almacenar(T t) throws SQLException {
        Field[] atributos = t.getClass().getDeclaredFields();
        String sql = "SELECT * FROM " + table.getNombreTabla() + " WHERE " + relacionalConValor(atributos, t);
        try(Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                actualizar(t);
            } else {
                guardar(t);
            }
        }
        return t;
    }

    public void actualizar(T t) throws SQLException {
        Field[] atributos = t.getClass().getDeclaredFields();
        try(Statement stmt = conn.createStatement()) {
            stmt.executeUpdate("UPDATE " + table.getNombreTabla() + " SET " + Arrays.stream(atributos).filter(
                    f -> f.isAnnotationPresent(Descriptible.class))
                    .filter(f -> !f.getAnnotation(Descriptible.class).isId())
                    .map(f -> {
                        f.setAccessible(true);
                                try {
                                    String palabra = String.valueOf(f.get(t));
                                    if (f.getAnnotation(Descriptible.class).isString()) {
                                        palabra = "'" + palabra + "'";
                                    }
                                    return f.getName() + "=" + palabra;
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                    ).collect(Collectors.joining(", ")) + " WHERE " + relacionalConValor(atributos, t));
            }
    }

    public T extraer(int id1, int id2) throws SQLException {
        T retorno = null;
        String sql = "SELECT * FROM " + table.getNombreTabla() +
                " WHERE " + table.getColumnas().get(0).getNombre() + "=? AND " +
                table.getColumnas().get(1).getNombre() + "=?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id1);
            stmt.setInt(2, id2);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()){
                switch (table.getNombreTabla()){
                    case "animes_calif.`animes-categorias`" -> retorno = (T) new AnimeCategoria(rs.getInt(1),
                            rs.getInt(2));
                    case "animes_calif.`usuarios-animes`" -> retorno = (T) new UsuariosAnimes(rs.getInt(1),
                            rs.getInt(2), rs.getInt(3));
                }
            }
        }
        return retorno;
    }

    public <F> List<T> porCampo(Columna col, F valor) throws SQLException {
        List<T> retorno = new ArrayList<>();
        String sql = "SELECT * FROM " + table.getNombreTabla() + " WHERE " + col.getNombre() + "=" +
                Optional.of(valor).stream().filter(val -> col.getTipo().equals("String")).map(val -> "'" + val + "'").
                        findFirst().orElseGet(() -> String.valueOf(valor));
        try(Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()){
                T elem = null;
                switch (table.getNombreTabla()) {
                    case "animes_calif.`animes-categorias`" -> elem = (T) new AnimeCategoria(rs.getInt(1),
                            rs.getInt(2));
                    case "animes_calif.`usuarios-animes`" -> elem = (T) new UsuariosAnimes(rs.getInt(1),
                            rs.getInt(2), rs.getInt(3));
                }
                retorno.add(elem);
            }
        }
        return retorno;
    }

    private String relacionalConValor(Field[] atributos, T t){
        String retorno = Arrays.stream(atributos).filter(f -> f.isAnnotationPresent(Descriptible.class))
                .filter(f -> f.getAnnotation(Descriptible.class).isRelacional())
                .map(f -> {
                    f.setAccessible(true);
                    try {
                        int valor = (int) f.get(t);
                        return f.getName() + "=" + valor;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    return null;
                }).collect(Collectors.joining(" AND "));
        return retorno;
    }

    public boolean remove(int id1, int id2) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + table.getNombreTabla() +
                " WHERE " + table.getColumnas().get(0).getNombre() + "=? AND " +
                table.getColumnas().get(1).getNombre() + "=?")){
            stmt.setInt(1, id1);
            stmt.setInt(2, id2);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e){
            return false;
        }
    }

    public boolean removePorColumna(Columna colum, int id) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement("DELETE FROM " + table.getNombreTabla() +
                " WHERE " + table.getColumnas().get(0).getNombre() + "=?")){
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e){
            return false;
        }
    }
}
