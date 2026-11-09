package com.contagest.vista;

import com.contagest.excepcion.AccesoDatosException;
import com.contagest.excepcion.ValidacionException;
import com.contagest.modelo.Cliente;
import com.contagest.modelo.TipoPersona;
import com.contagest.servicio.ClienteServicio;

import java.util.List;
import java.util.Scanner;

/**
 * Interfaz de consola del modulo de clientes.
 */
public class MenuConsola {

    private final ClienteServicio clienteServicio;
    private final Scanner lector;

    public MenuConsola(ClienteServicio clienteServicio, Scanner lector) {
        this.clienteServicio = clienteServicio;
        this.lector = lector;
    }

    public void iniciar() {
        boolean continuar = true;
        while (continuar) {
            mostrarMenu();
            String opcion = leerTexto("Seleccione una opcion");
            if (opcion == null) {
                break;
            }
            try {
                continuar = ejecutarOpcion(opcion.trim());
            } catch (ValidacionException e) {
                System.out.println("Aviso: " + e.getMessage());
            } catch (AccesoDatosException e) {
                System.out.println("Error de base de datos: " + e.getMessage());
            }
        }
        System.out.println("Hasta pronto.");
    }

    private void mostrarMenu() {
        System.out.println();
        System.out.println("===== CONTAGEST - MODULO DE CLIENTES =====");
        System.out.println("1. Registrar cliente (INSERT)");
        System.out.println("2. Listar clientes (SELECT)");
        System.out.println("3. Consultar cliente por id (SELECT)");
        System.out.println("4. Buscar cliente por NIT (SELECT)");
        System.out.println("5. Buscar clientes por razon social (SELECT)");
        System.out.println("6. Actualizar cliente (UPDATE)");
        System.out.println("7. Eliminar cliente (DELETE)");
        System.out.println("0. Salir");
    }

    private boolean ejecutarOpcion(String opcion) {
        switch (opcion) {
            case "1":
                registrarCliente();
                break;
            case "2":
                imprimirLista(clienteServicio.listar());
                break;
            case "3":
                System.out.println(clienteServicio.consultarPorId(leerNumero("Id del cliente")));
                break;
            case "4":
                System.out.println(clienteServicio.consultarPorNit(leerTexto("NIT")));
                break;
            case "5":
                imprimirLista(clienteServicio.buscarPorRazonSocial(leerTexto("Texto a buscar")));
                break;
            case "6":
                actualizarCliente();
                break;
            case "7":
                eliminarCliente();
                break;
            case "0":
                return false;
            default:
                System.out.println("Opcion no valida.");
        }
        return true;
    }

    private void registrarCliente() {
        Cliente cliente = new Cliente();
        cliente.setNit(leerTexto("NIT (ej: 900123456-7)"));
        cliente.setRazonSocial(leerTexto("Razon social"));
        cliente.setTipoPersona(leerTipoPersona());
        cliente.setCorreo(leerTexto("Correo electronico"));
        cliente.setTelefono(leerTexto("Telefono (opcional)"));
        clienteServicio.registrar(cliente);
        System.out.println("Cliente registrado con id " + cliente.getId());
    }

    private void actualizarCliente() {
        Cliente cliente = clienteServicio.consultarPorId(leerNumero("Id del cliente a actualizar"));
        System.out.println("Datos actuales: " + cliente);
        System.out.println("(Deje vacio un campo para conservar el valor actual)");

        String nit = leerTexto("Nuevo NIT");
        if (!nit.isBlank()) {
            cliente.setNit(nit);
        }
        String razonSocial = leerTexto("Nueva razon social");
        if (!razonSocial.isBlank()) {
            cliente.setRazonSocial(razonSocial);
        }
        String correo = leerTexto("Nuevo correo");
        if (!correo.isBlank()) {
            cliente.setCorreo(correo);
        }
        String telefono = leerTexto("Nuevo telefono");
        if (!telefono.isBlank()) {
            cliente.setTelefono(telefono);
        }
        String activo = leerTexto("Esta activo? (S/N)");
        if (!activo.isBlank()) {
            cliente.setActivo(activo.equalsIgnoreCase("S"));
        }
        clienteServicio.actualizar(cliente);
        System.out.println("Cliente actualizado: " + cliente);
    }

    private void eliminarCliente() {
        long id = leerNumero("Id del cliente a eliminar");
        Cliente cliente = clienteServicio.consultarPorId(id);
        String confirmacion = leerTexto("Eliminar a " + cliente.getRazonSocial() + "? (S/N)");
        if (confirmacion.equalsIgnoreCase("S")) {
            clienteServicio.eliminar(id);
            System.out.println("Cliente eliminado.");
        } else {
            System.out.println("Operacion cancelada.");
        }
    }

    private void imprimirLista(List<Cliente> clientes) {
        if (clientes.isEmpty()) {
            System.out.println("No hay clientes para mostrar.");
            return;
        }
        clientes.forEach(System.out::println);
        System.out.println("Total: " + clientes.size());
    }

    private TipoPersona leerTipoPersona() {
        String respuesta = leerTexto("Tipo de persona (N = Natural, J = Juridica)");
        try {
            return TipoPersona.desdeCodigo(respuesta);
        } catch (IllegalArgumentException e) {
            throw new ValidacionException("Tipo de persona invalido. Use N o J");
        }
    }

    private long leerNumero(String mensaje) {
        String texto = leerTexto(mensaje);
        try {
            return Long.parseLong(texto.trim());
        } catch (NumberFormatException e) {
            throw new ValidacionException("Debe ingresar un numero valido");
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje + ": ");
        return lector.hasNextLine() ? lector.nextLine() : null;
    }
}
