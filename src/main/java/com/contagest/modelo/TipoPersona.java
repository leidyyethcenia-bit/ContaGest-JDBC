package com.contagest.modelo;

/**
 * Tipo de persona ante la DIAN.
 */
public enum TipoPersona {

    NATURAL("N", "Natural"),
    JURIDICA("J", "Juridica");

    private final String codigo;
    private final String descripcion;

    TipoPersona(String codigo, String descripcion) {
        this.codigo = codigo;
        this.descripcion = descripcion;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Obtiene el tipo de persona a partir del codigo guardado en la base de datos.
     */
    public static TipoPersona desdeCodigo(String codigo) {
        for (TipoPersona tipo : values()) {
            if (tipo.codigo.equalsIgnoreCase(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Codigo de tipo de persona desconocido: " + codigo);
    }
}
