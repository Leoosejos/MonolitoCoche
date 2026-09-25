package com.example.MonolitoCoche.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CocheNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String manejarCocheNoEncontrado(CocheNotFoundException ex, Model model) {
        model.addAttribute("mensaje", ex.getMessage());
        return "error/404";
    }
}
