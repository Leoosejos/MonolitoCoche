package com.example.MonolitoCoche.exception;

/**
 * Excepción de negocio: se lanza cuando no existe un coche con el id indicado.
 */
public class CocheNotFoundException extends RuntimeException {

    public CocheNotFoundException(Long id) {
        super("No se encontró el coche con id " + id);
    }
}
