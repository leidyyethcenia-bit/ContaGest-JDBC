package com.contagest.vista;

import com.contagest.modelo.Cliente;
import com.contagest.modelo.TipoPersona;
import com.contagest.servicio.ClienteServicio;

/**
 * Ejecuta de forma automatica las cuatro operaciones CRUD para demostrar
 * el funcionamiento del modulo (opcion --demo de la clase Main).
 */
public class DemostracionCrud {

    private static final String NIT_DEMOSTRACION = "900123456-7";

    private final ClienteServicio clienteServicio;

    public DemostracionCrud(ClienteServicio clienteServicio) {
        this.clienteServicio = clienteServicio;
    }

    public void ejecutar() {
        limpiarDatosPrevios();

        System.out.println("--- 1. INSERT: registrar cliente ---");
        Cliente cliente = new Cliente(NIT_DEMOSTRACION, "Ferreteria El Tornillo S.A.S.",
                TipoPersona.JURIDICA, "contacto@eltornillo.com", "3101234567");
        clienteServicio.registrar(cliente);
        System.out.println("Registrado: " + cliente);

        System.out.println("\n--- 2. SELECT: consultar ---");
        System.out.println("Por id:  " + clienteServicio.consultarPorId(cliente.getId()));
        System.out.println("Por NIT: " + clienteServicio.consultarPorNit(NIT_DEMOSTRACION));
        System.out.println("Listado completo:");
        clienteServicio.listar().forEach(c -> System.out.println("  " + c));

        System.out.println("\n--- 3. UPDATE: actualizar ---");
        cliente.setRazonSocial("Ferreteria El Tornillo y Cia S.A.S.");
        cliente.setTelefono("3207654321");
        clienteServicio.actualizar(cliente);
        System.out.println("Despues de actualizar: " + clienteServicio.consultarPorId(cliente.getId()));

        System.out.println("\n--- 4. DELETE: eliminar ---");
        clienteServicio.eliminar(cliente.getId());
        System.out.println("Cliente eliminado. Clientes restantes: " + clienteServicio.listar().size());

        System.out.println("\nDemostracion finalizada correctamente.");
    }

    private void limpiarDatosPrevios() {
        try {
            clienteServicio.eliminar(clienteServicio.consultarPorNit(NIT_DEMOSTRACION).getId());
        } catch (RuntimeException e) {
            // No existia un registro previo; no hay nada que limpiar.
        }
    }
}
