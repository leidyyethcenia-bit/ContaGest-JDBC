-- Esquema del modulo de clientes del despacho contable
CREATE TABLE IF NOT EXISTS clientes (
    id             BIGINT AUTO_INCREMENT PRIMARY KEY,
    nit            VARCHAR(15)  NOT NULL UNIQUE,
    razon_social   VARCHAR(150) NOT NULL,
    tipo_persona   CHAR(1)      NOT NULL,
    correo         VARCHAR(100) NOT NULL,
    telefono       VARCHAR(15),
    es_activo      BOOLEAN      NOT NULL DEFAULT TRUE,
    fecha_registro DATE         NOT NULL
);
