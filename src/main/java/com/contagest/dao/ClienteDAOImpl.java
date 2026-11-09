package com.contagest.dao;

import com.contagest.config.ConexionBD;
import com.contagest.excepcion.AccesoDatosException;
import com.contagest.modelo.Cliente;
import com.contagest.modelo.TipoPersona;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementacion JDBC del acceso a datos de clientes.
 * Usa PreparedStatement en todas las consultas para evitar inyeccion SQL.
 */
public class ClienteDAOImpl implements ClienteDAO {

    private static final String SQL_INSERTAR =
            "INSERT INTO clientes (nit, razon_social, tipo_persona, correo, telefono, "
            + "es_activo, fecha_registro) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_CONSULTAR_POR_ID =
            "SELECT * FROM clientes WHERE id = ?";
    private static final String SQL_CONSULTAR_TODOS =
            "SELECT * FROM clientes ORDER BY razon_social";
    private static final String SQL_BUSCAR_POR_NIT =
            "SELECT * FROM clientes WHERE nit = ?";
    private static final String SQL_BUSCAR_POR_RAZON_SOCIAL =
            "SELECT * FROM clientes WHERE LOWER(razon_social) LIKE ? ORDER BY razon_social";
    private static final String SQL_ACTUALIZAR =
            "UPDATE clientes SET nit = ?, razon_social = ?, tipo_persona = ?, correo = ?, "
            + "telefono = ?, es_activo = ? WHERE id = ?";
    private static final String SQL_ELIMINAR =
            "DELETE FROM clientes WHERE id = ?";

    @Override
    public Long insertar(Cliente cliente) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(
                     SQL_INSERTAR, Statement.RETURN_GENERATED_KEYS)) {
            sentencia.setString(1, cliente.getNit());
            sentencia.setString(2, cliente.getRazonSocial());
            sentencia.setString(3, cliente.getTipoPersona().getCodigo());
            sentencia.setString(4, cliente.getCorreo());
            asignarTextoOpcional(sentencia, 5, cliente.getTelefono());
            sentencia.setBoolean(6, cliente.isActivo());
            sentencia.setDate(7, Date.valueOf(cliente.getFechaRegistro()));
            sentencia.executeUpdate();

            try (ResultSet llaves = sentencia.getGeneratedKeys()) {
                if (llaves.next()) {
                    cliente.setId(llaves.getLong(1));
                }
            }
            return cliente.getId();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al insertar el cliente", e);
        }
    }

    @Override
    public Optional<Cliente> consultarPorId(Long id) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_CONSULTAR_POR_ID)) {
            sentencia.setLong(1, id);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? Optional.of(mapearCliente(resultado)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al consultar el cliente con id " + id, e);
        }
    }

    @Override
    public List<Cliente> consultarTodos() {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_CONSULTAR_TODOS);
             ResultSet resultado = sentencia.executeQuery()) {
            while (resultado.next()) {
                clientes.add(mapearCliente(resultado));
            }
            return clientes;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al consultar los clientes", e);
        }
    }

    @Override
    public Optional<Cliente> buscarPorNit(String nit) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_BUSCAR_POR_NIT)) {
            sentencia.setString(1, nit);
            try (ResultSet resultado = sentencia.executeQuery()) {
                return resultado.next() ? Optional.of(mapearCliente(resultado)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar el cliente con NIT " + nit, e);
        }
    }

    @Override
    public List<Cliente> buscarPorRazonSocial(String texto) {
        List<Cliente> clientes = new ArrayList<>();
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_BUSCAR_POR_RAZON_SOCIAL)) {
            sentencia.setString(1, "%" + texto.toLowerCase() + "%");
            try (ResultSet resultado = sentencia.executeQuery()) {
                while (resultado.next()) {
                    clientes.add(mapearCliente(resultado));
                }
            }
            return clientes;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar clientes por razon social", e);
        }
    }

    @Override
    public boolean actualizar(Cliente cliente) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ACTUALIZAR)) {
            sentencia.setString(1, cliente.getNit());
            sentencia.setString(2, cliente.getRazonSocial());
            sentencia.setString(3, cliente.getTipoPersona().getCodigo());
            sentencia.setString(4, cliente.getCorreo());
            asignarTextoOpcional(sentencia, 5, cliente.getTelefono());
            sentencia.setBoolean(6, cliente.isActivo());
            sentencia.setLong(7, cliente.getId());
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar el cliente", e);
        }
    }

    @Override
    public boolean eliminar(Long id) {
        try (Connection conexion = ConexionBD.obtenerConexion();
             PreparedStatement sentencia = conexion.prepareStatement(SQL_ELIMINAR)) {
            sentencia.setLong(1, id);
            return sentencia.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al eliminar el cliente con id " + id, e);
        }
    }

    private Cliente mapearCliente(ResultSet resultado) throws SQLException {
        Cliente cliente = new Cliente();
        cliente.setId(resultado.getLong("id"));
        cliente.setNit(resultado.getString("nit"));
        cliente.setRazonSocial(resultado.getString("razon_social"));
        cliente.setTipoPersona(TipoPersona.desdeCodigo(resultado.getString("tipo_persona")));
        cliente.setCorreo(resultado.getString("correo"));
        cliente.setTelefono(resultado.getString("telefono"));
        cliente.setActivo(resultado.getBoolean("es_activo"));
        cliente.setFechaRegistro(resultado.getDate("fecha_registro").toLocalDate());
        return cliente;
    }

    private void asignarTextoOpcional(PreparedStatement sentencia, int posicion, String valor)
            throws SQLException {
        if (valor == null || valor.isBlank()) {
            sentencia.setNull(posicion, Types.VARCHAR);
        } else {
            sentencia.setString(posicion, valor);
        }
    }
}
