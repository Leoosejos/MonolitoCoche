package com.example.MonolitoCoche.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Cualquier URL que no coincida con ningún controlador (ej. /cualquiercosa) lanza
 * NoResourceFoundException. En vez de dejar que se vea el 404 por defecto, se
 * reutiliza la misma vista de PingController.
 */
@ControllerAdvice
public class ErrorController {

    @ExceptionHandler(NoResourceFoundException.class)
    public String manejarRutaNoEncontrada() {
        return "ping";
    }
}
