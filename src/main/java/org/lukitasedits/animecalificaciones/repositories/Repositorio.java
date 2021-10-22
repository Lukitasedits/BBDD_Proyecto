package org.lukitasedits.animecalificaciones.repositories;

import org.lukitasedits.animecalificaciones.annotations.Descriptible;
import org.lukitasedits.animecalificaciones.model.structure.Tablas;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.stream.Collectors;

public class Repositorio<T> {
    Tablas table;
    Connection conn;

    public Repositorio(Tablas table, Connection conn) {
        this.table = table;
        this.conn = conn;
    }

    public void truncate() throws SQLException {
        try(Statement stmt = conn.createStatement()){
            stmt.executeUpdate("TRUNCATE TABLE " + table.getNombreTabla());
        }
    }

    public boolean isEmpty() throws SQLException{
        try (Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery("SELECT * FROM " + table.getNombreTabla());
            if(rs.next()){
                return true;
            }
        }
        return false;
    }

    protected void guardar(T t) throws SQLException {
        Field[] atributos = t.getClass().getDeclaredFields();
        try(Statement stmt = conn.createStatement()){
            stmt.executeUpdate("INSERT INTO " + table.getNombreTabla() + " (" + table.nombreColumnas() +
                    ") VALUES (" + Arrays.stream(atributos).filter(f-> f.isAnnotationPresent(Descriptible.class))
                    .filter(f -> !f.getAnnotation(Descriptible.class).isId())
                    .map(f-> { f.setAccessible(true);
                                try {
                                    String palabra = String.valueOf(f.get(t));
                                    if(f.getAnnotation(Descriptible.class).isString()){palabra = "'" + palabra + "'";}
                                    return palabra;
                                } catch (IllegalAccessException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                    ).collect(Collectors.joining(", ")) + ")");
        }
    }



}
