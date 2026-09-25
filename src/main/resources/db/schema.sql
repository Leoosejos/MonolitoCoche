-- =====================================================================
--  SCRIPT DE INICIALIZACIÓN: base de datos y tablas
--  Proyecto: MonolitoCoche
--  Motor:    MySQL 8+
--
--  Con JDBC la aplicación NO lo ejecuta sola: ejecútalo a mano desde
--  phpMyAdmin (pestaña SQL), MySQL Workbench o consola:
--      mysql -u root -p < schema.sql
--  Es idempotente: se puede ejecutar varias veces sin error.
-- =====================================================================

CREATE DATABASE IF NOT EXISTS monolito_coche
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE monolito_coche;

-- ---------------------------------------------------------------------
--  Tabla: coches  (se corresponde con la entidad model/Coche.java)
-- ---------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS coches (
    id           BIGINT         NOT NULL AUTO_INCREMENT,
    marca        VARCHAR(50)    NOT NULL,
    modelo       VARCHAR(50)    NOT NULL,
    matricula    VARCHAR(15)    NOT NULL,
    anio         INT            NOT NULL,
    color        VARCHAR(30)    NOT NULL,
    precio       DECIMAL(12, 2) NOT NULL,
    kilometraje  INT            NOT NULL,
    combustible  ENUM ('GASOLINA', 'DIESEL', 'HIBRIDO', 'ELECTRICO', 'GLP') NOT NULL,
    transmision  ENUM ('MANUAL', 'AUTOMATICA') NOT NULL,

    CONSTRAINT pk_coches            PRIMARY KEY (id),
    CONSTRAINT uk_coches_matricula  UNIQUE (matricula),
    CONSTRAINT ck_coches_anio       CHECK (anio BETWEEN 1900 AND 2100),
    CONSTRAINT ck_coches_precio     CHECK (precio >= 0),
    CONSTRAINT ck_coches_km         CHECK (kilometraje >= 0)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci;
