package org.lukitasedits.animecalificaciones.util;

import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.SQLException;

public class ConexionBaseDatos {
    private static String url = "jdbc:mysql://localhost:3306/animes_calif?serverTimezone=America/Argentina/Buenos_Aires";
    private static String user = "root";
    private static String password = "escueladetorpes";
    private static BasicDataSource pool;

    public static BasicDataSource getInstance() throws SQLException {
        if(pool == null){
            pool = new BasicDataSource();
            pool.setUrl(url);
            pool.setUsername(user);
            pool.setPassword(password);
            pool.setInitialSize(3);
            pool.setInitialSize(3);
            pool.setMinIdle(3);
            pool.setMaxIdle(10);
            pool.setMaxTotal(10);
        }
        return pool;
    }

    public static Connection getConnection() throws  SQLException{
        return getInstance().getConnection();
    }
}
