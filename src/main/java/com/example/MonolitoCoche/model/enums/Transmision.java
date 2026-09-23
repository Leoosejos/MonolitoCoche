package com.example.MonolitoCoche.model.enums;

/**
 * Tipos de transmisión (caja de cambios) admitidos para un coche.
 */
public enum Transmision {

    MANUAL("Manual"),
    AUTOMATICA("Automática");

    private final String etiqueta;

    Transmision(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
