package org.lukitasedits.animecalificaciones.repositories;

import org.lukitasedits.animecalificaciones.model.elements.conId.Categoria;
import org.lukitasedits.animecalificaciones.annotations.Descriptible;
import org.lukitasedits.animecalificaciones.model.elements.conId.Anime;
import org.lukitasedits.animecalificaciones.model.elements.conId.ElementosConId;
import org.lukitasedits.animecalificaciones.model.elements.conId.Usuario;
import org.lukitasedits.animecalificaciones.model.structure.Columna;
import org.lukitasedits.animecalificaciones.model.structure.Tablas;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RepositoryConId<T extends ElementosConId> extends Repositorio{

    public RepositoryConId(Tablas table, Connection conn) {
        super(table, conn);
    }

    void actualizar(T t) throws SQLException {
        Field[] atributos = t.getClass().getDeclaredFields();
        String sql = "UPDATE " + table.getNombreTabla() + " SET " + Arrays.stream(atributos).filter(
                f-> f.isAnnotationPresent(Descriptible.class))
                .filter(f -> !f.getAnnotation(Descriptible.class).isId())
                .map(f-> { f.setAccessible(true);
                            try {
                                String palabra = String.valueOf(f.get(t));
                                if(f.getAnnotation(Descriptible.class).isString()){palabra = "'" + palabra + "'";}
                                return f.getName() + "=" + palabra;
                            } catch (IllegalAccessException e) {
                                e.printStackTrace();
                            }
                            return null;
                        }
                ).collect(Collectors.joining(", ")) + " WHERE id=?";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1, t.getId());
            stmt.executeUpdate();
        }
    }

    public T almacenar(T t) throws SQLException {
        if(t.getId() == 0){
            super.guardar(t);
            int id = 0;
            try(Statement query = conn.createStatement()){
                ResultSet rs = query.executeQuery("SELECT * FROM " + table.getNombreTabla());
                while (rs.next()){ if(rs.isLast()){ id = rs.getInt(1);}}
            }
            return porId(id);
        } else {
            actualizar(t);
            return t;
        }
    }


    public T porId(int id) throws SQLException {
        T retorno = null;
        try(PreparedStatement stmt = conn.prepareStatement(
                "SELECT * FROM " + table.getNombreTabla() + " WHERE id=?")){
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()) {
                retorno = crearElem(rs);
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
                T elem = crearElem(rs);
                retorno.add(elem);
            }
        }
        return retorno;
    }

    public boolean remove(int id) throws SQLException {
        try(PreparedStatement stmt = conn.prepareStatement(
                "DELETE FROM " + table.getNombreTabla() + " WHERE id=?")){
            stmt.setInt(1, id);
            stmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            return false;
        }
    }

    private T crearElem(ResultSet rs) throws SQLException {
        T retorno = null;
        switch (table.getNombreTabla()) {
            case "animes" -> retorno = (T) new Anime(rs.getInt(1), rs.getString(2), rs.getFloat(3));
            case "categorias" -> retorno = (T) new Categoria(rs.getInt(1), rs.getString(2));
            case "usuarios" -> retorno = (T) new Usuario(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5));
        }
        return retorno;
    }
}
