package com.contagest.dao;

import com.contagest.modelo.Cliente;

import java.util.List;
import java.util.Optional;

/**
 * Operaciones de acceso a datos para la entidad Cliente.
 */
public interface ClienteDAO extends CrudDAO<Cliente, Long> {

    /** Busca un cliente por su NIT. */
    Optional<Cliente> buscarPorNit(String nit);

    /** Busca clientes cuya razon social contenga el texto indicado. */
    List<Cliente> buscarPorRazonSocial(String texto);
}
