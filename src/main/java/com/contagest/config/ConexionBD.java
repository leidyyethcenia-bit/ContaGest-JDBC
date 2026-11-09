package com.contagest.config;

import com.contagest.excepcion.AccesoDatosException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * Administra la conexion JDBC con la base de datos del sistema.
 * Lee los parametros desde el archivo db.properties.
 */
public final class ConexionBD {

    private static final String ARCHIVO_CONFIGURACION = "db.properties";
    private static final String ARCHIVO_ESQUEMA = "schema.sql";
    private static final Properties CONFIGURACION = cargarConfiguracion();

    private ConexionBD() {
    }

    /**
     * Abre una nueva conexion con la base de datos.
     *
     * @return conexion JDBC lista para usar; quien la solicita debe cerrarla.
     */
    public static Connection obtenerConexion() {
        try {
            return DriverManager.getConnection(
                    CONFIGURACION.getProperty("db.url"),
                    CONFIGURACION.getProperty("db.usuario"),
                    CONFIGURACION.getProperty("db.clave"));
        } catch (SQLException e) {
            throw new AccesoDatosException("No fue posible conectar con la base de datos", e);
        }
    }

    /**
     * Crea las tablas del sistema si todavia no existen.
     */
    public static void inicializarEsquema() {
        String script = leerRecurso(ARCHIVO_ESQUEMA);
        try (Connection conexion = obtenerConexion();
             Statement sentencia = conexion.createStatement()) {
            for (String instruccion : script.split(";")) {
                String sql = quitarComentarios(instruccion).trim();
                if (!sql.isEmpty()) {
                    sentencia.execute(sql);
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("No fue posible crear el esquema de la base de datos", e);
        }
    }

    private static Properties cargarConfiguracion() {
        Properties propiedades = new Properties();
        try (InputStream entrada = ConexionBD.class.getClassLoader()
                .getResourceAsStream(ARCHIVO_CONFIGURACION)) {
            if (entrada == null) {
                throw new IllegalStateException("No se encontro el archivo " + ARCHIVO_CONFIGURACION);
            }
            propiedades.load(entrada);
            Class.forName(propiedades.getProperty("db.driver"));
        } catch (IOException | ClassNotFoundException e) {
            throw new IllegalStateException("No fue posible cargar la configuracion de la base de datos", e);
        }
        return propiedades;
    }

    private static String leerRecurso(String nombre) {
        try (InputStream entrada = ConexionBD.class.getClassLoader().getResourceAsStream(nombre)) {
            if (entrada == null) {
                throw new IllegalStateException("No se encontro el archivo " + nombre);
            }
            return new String(entrada.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new IllegalStateException("No fue posible leer el archivo " + nombre, e);
        }
    }

    private static String quitarComentarios(String texto) {
        StringBuilder resultado = new StringBuilder();
        for (String linea : texto.split("\n")) {
            if (!linea.trim().startsWith("--")) {
                resultado.append(linea).append('\n');
            }
        }
        return resultado.toString();
    }
}
