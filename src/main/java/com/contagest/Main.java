package com.contagest;

import com.contagest.config.ConexionBD;
import com.contagest.dao.ClienteDAOImpl;
import com.contagest.excepcion.AccesoDatosException;
import com.contagest.servicio.ClienteServicio;
import com.contagest.vista.DemostracionCrud;
import com.contagest.vista.MenuConsola;

import java.util.Scanner;

/**
 * Punto de entrada de ContaGest (modulo de clientes).
 * Sin argumentos abre el menu interactivo; con --demo ejecuta el CRUD automatico.
 */
public final class Main {

    private Main() {
    }

    public static void main(String[] args) {
        try {
            ConexionBD.inicializarEsquema();
            ClienteServicio clienteServicio = new ClienteServicio(new ClienteDAOImpl());

            if (args.length > 0 && "--demo".equals(args[0])) {
                new DemostracionCrud(clienteServicio).ejecutar();
            } else {
                new MenuConsola(clienteServicio, new Scanner(System.in)).iniciar();
            }
        } catch (AccesoDatosException e) {
            System.out.println("Error de base de datos: " + e.getMessage());
            if (e.getCause() != null) {
                System.out.println("Detalle: " + e.getCause().getMessage());
            }
        }
    }
}
