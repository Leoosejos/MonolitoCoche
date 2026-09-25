package com.example.MonolitoCoche.repository;

import com.example.MonolitoCoche.exception.AccesoDatosException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * PATRÓN SINGLETON
 * Solo existe una instancia de Conexion en toda la aplicación (constructor privado +
 * getInstancia()). Guarda una única Connection JDBC que reutilizan todos los DAO.
 * URL, usuario y contraseña se leen de application.properties.
 */
public class Conexion {

    private static Conexion instancia;

    private final String url;
    private final String usuario;
    private final String password;
    private Connection connection;

    private Conexion() {
        Properties props = new Properties();
        try (InputStream in = Conexion.class.getResourceAsStream("/application.properties")) {
            if (in == null) {
                throw new IOException("No se encuentra application.properties en el classpath");
            }
            props.load(new InputStreamReader(in, StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new AccesoDatosException("No se pudo leer la configuración de la base de datos", e);
        }
        this.url = props.getProperty("spring.datasource.url");
        this.usuario = props.getProperty("spring.datasource.username");
        this.password = props.getProperty("spring.datasource.password", "");
    }

    public static synchronized Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    /** Devuelve la conexión; si no existe o dejó de ser válida (MySQL cierra las inactivas), abre otra. */
    public synchronized Connection getConnection() {
        try {
            if (connection == null || !connection.isValid(2)) {
                connection = DriverManager.getConnection(url, usuario, password);
            }
            return connection;
        } catch (SQLException e) {
            throw new AccesoDatosException("No se pudo conectar a la base de datos", e);
        }
    }

    public synchronized void cerrarConexion() {
        if (connection == null) {
            return;
        }
        try {
            connection.close();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al cerrar la conexión", e);
        } finally {
            connection = null;
        }
    }
}
