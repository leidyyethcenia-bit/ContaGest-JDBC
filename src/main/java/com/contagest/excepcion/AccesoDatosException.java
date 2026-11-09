package com.contagest.excepcion;

/**
 * Se lanza cuando ocurre un error al comunicarse con la base de datos.
 */
public class AccesoDatosException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public AccesoDatosException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
