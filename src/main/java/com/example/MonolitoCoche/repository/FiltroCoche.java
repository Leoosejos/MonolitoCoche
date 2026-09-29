package com.example.MonolitoCoche.repository;

import java.util.List;

/**
 * Criterios de búsqueda, orden y paginación para el listado de coches.
 * Las listas vacías significan "sin filtrar por ese campo".
 */
public class FiltroCoche {

    private List<String> combustibles = List.of();
    private List<String> transmisiones = List.of();
    private List<String> marcas = List.of();
    private String ordenarPor = "marca";
    private String direccion = "asc";
    private int pagina = 0;
    private int tamanioPagina = 5;

    public List<String> getCombustibles() { return combustibles; }
    public void setCombustibles(List<String> combustibles) { this.combustibles = combustibles; }

    public List<String> getTransmisiones() { return transmisiones; }
    public void setTransmisiones(List<String> transmisiones) { this.transmisiones = transmisiones; }

    public List<String> getMarcas() { return marcas; }
    public void setMarcas(List<String> marcas) { this.marcas = marcas; }

    public String getOrdenarPor() { return ordenarPor; }
    public void setOrdenarPor(String ordenarPor) { this.ordenarPor = ordenarPor; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public int getPagina() { return pagina; }
    public void setPagina(int pagina) { this.pagina = pagina; }

    public int getTamanioPagina() { return tamanioPagina; }
    public void setTamanioPagina(int tamanioPagina) { this.tamanioPagina = tamanioPagina; }

    public boolean tieneFiltrosActivos() {
        return !combustibles.isEmpty() || !transmisiones.isEmpty() || !marcas.isEmpty();
    }

    public int totalFiltrosActivos() {
        return combustibles.size() + transmisiones.size() + marcas.size();
    }
}
