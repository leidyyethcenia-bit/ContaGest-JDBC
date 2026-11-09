package com.contagest.servicio;

import com.contagest.dao.ClienteDAO;
import com.contagest.excepcion.ValidacionException;
import com.contagest.modelo.Cliente;

import java.util.List;
import java.util.regex.Pattern;

/**
 * Logica de negocio del modulo de clientes: valida los datos antes de
 * delegar en el DAO.
 */
public class ClienteServicio {

    private static final Pattern PATRON_NIT = Pattern.compile("^\\d{5,12}(-\\d)?$");
    private static final Pattern PATRON_CORREO =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    private static final Pattern PATRON_TELEFONO = Pattern.compile("^\\d{7,10}$");

    private final ClienteDAO clienteDAO;

    public ClienteServicio(ClienteDAO clienteDAO) {
        this.clienteDAO = clienteDAO;
    }

    /** Registra un cliente nuevo (INSERT). */
    public Cliente registrar(Cliente cliente) {
        validar(cliente);
        if (clienteDAO.buscarPorNit(cliente.getNit()).isPresent()) {
            throw new ValidacionException("Ya existe un cliente con el NIT " + cliente.getNit());
        }
        clienteDAO.insertar(cliente);
        return cliente;
    }

    /** Lista todos los clientes (SELECT). */
    public List<Cliente> listar() {
        return clienteDAO.consultarTodos();
    }

    /** Consulta un cliente por id (SELECT). */
    public Cliente consultarPorId(long id) {
        return clienteDAO.consultarPorId(id)
                .orElseThrow(() -> new ValidacionException("No existe un cliente con id " + id));
    }

    /** Consulta un cliente por NIT (SELECT). */
    public Cliente consultarPorNit(String nit) {
        return clienteDAO.buscarPorNit(nit)
                .orElseThrow(() -> new ValidacionException("No existe un cliente con NIT " + nit));
    }

    /** Busca clientes por parte de la razon social (SELECT). */
    public List<Cliente> buscarPorRazonSocial(String texto) {
        if (texto == null || texto.isBlank()) {
            throw new ValidacionException("Ingrese un texto para buscar");
        }
        return clienteDAO.buscarPorRazonSocial(texto.trim());
    }

    /** Actualiza los datos de un cliente existente (UPDATE). */
    public Cliente actualizar(Cliente cliente) {
        if (cliente.getId() == null) {
            throw new ValidacionException("El cliente debe tener un id para actualizarse");
        }
        validar(cliente);
        clienteDAO.buscarPorNit(cliente.getNit()).ifPresent(existente -> {
            if (!existente.getId().equals(cliente.getId())) {
                throw new ValidacionException("El NIT " + cliente.getNit()
                        + " pertenece a otro cliente");
            }
        });
        if (!clienteDAO.actualizar(cliente)) {
            throw new ValidacionException("No existe un cliente con id " + cliente.getId());
        }
        return cliente;
    }

    /** Elimina un cliente (DELETE). */
    public void eliminar(long id) {
        if (!clienteDAO.eliminar(id)) {
            throw new ValidacionException("No existe un cliente con id " + id);
        }
    }

    private void validar(Cliente cliente) {
        if (cliente.getNit() == null || !PATRON_NIT.matcher(cliente.getNit()).matches()) {
            throw new ValidacionException("El NIT debe tener entre 5 y 12 digitos, "
                    + "con digito de verificacion opcional (ej: 900123456-7)");
        }
        if (cliente.getRazonSocial() == null || cliente.getRazonSocial().isBlank()) {
            throw new ValidacionException("La razon social es obligatoria");
        }
        if (cliente.getTipoPersona() == null) {
            throw new ValidacionException("El tipo de persona es obligatorio");
        }
        if (cliente.getCorreo() == null || !PATRON_CORREO.matcher(cliente.getCorreo()).matches()) {
            throw new ValidacionException("El correo electronico no es valido");
        }
        String telefono = cliente.getTelefono();
        if (telefono != null && !telefono.isBlank()
                && !PATRON_TELEFONO.matcher(telefono).matches()) {
            throw new ValidacionException("El telefono debe tener entre 7 y 10 digitos");
        }
    }
}
