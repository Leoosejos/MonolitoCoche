package com.example.MonolitoCoche.model.enums;

/**
 * Tipos de combustible admitidos para un coche.
 * Cada valor lleva una etiqueta legible para mostrarla en las vistas.
 */
public enum Combustible {

    GASOLINA("Gasolina"),
    DIESEL("Diésel"),
    HIBRIDO("Híbrido"),
    ELECTRICO("Eléctrico"),
    GLP("GLP");

    private final String etiqueta;

    Combustible(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
