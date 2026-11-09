package com.contagest.excepcion;

/**
 * Se lanza cuando los datos ingresados no cumplen las reglas del negocio.
 */
public class ValidacionException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public ValidacionException(String mensaje) {
        super(mensaje);
    }
}
