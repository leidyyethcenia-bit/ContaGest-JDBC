package com.contagest.modelo;

import java.time.LocalDate;

/**
 * Persona natural o juridica atendida por el despacho contable.
 */
public class Cliente {

    private Long id;
    private String nit;
    private String razonSocial;
    private TipoPersona tipoPersona;
    private String correo;
    private String telefono;
    private boolean activo;
    private LocalDate fechaRegistro;

    public Cliente() {
        this.activo = true;
        this.fechaRegistro = LocalDate.now();
    }

    public Cliente(String nit, String razonSocial, TipoPersona tipoPersona,
                   String correo, String telefono) {
        this();
        this.nit = nit;
        this.razonSocial = razonSocial;
        this.tipoPersona = tipoPersona;
        this.correo = correo;
        this.telefono = telefono;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getRazonSocial() {
        return razonSocial;
    }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    @Override
    public String toString() {
        return String.format("[%d] NIT %s | %s | %s | %s | %s | %s | registrado %s",
                id, nit, razonSocial, tipoPersona.getDescripcion(), correo,
                telefono == null ? "sin telefono" : telefono,
                activo ? "activo" : "inactivo", fechaRegistro);
    }
}
