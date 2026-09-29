package com.example.MonolitoCoche.repository;

import java.util.List;

/** Resultado de una consulta paginada: los elementos de la página actual más los datos para pintar el paginador. */
public class Pagina<T> {

    private final List<T> contenido;
    private final long totalElementos;
    private final int pagina;
    private final int tamanioPagina;

    public Pagina(List<T> contenido, long totalElementos, int pagina, int tamanioPagina) {
        this.contenido = contenido;
        this.totalElementos = totalElementos;
        this.pagina = pagina;
        this.tamanioPagina = tamanioPagina;
    }

    public List<T> getContenido() { return contenido; }
    public long getTotalElementos() { return totalElementos; }
    public int getPagina() { return pagina; }
    public int getTamanioPagina() { return tamanioPagina; }

    public int getTotalPaginas() {
        return (int) Math.max(1, Math.ceil(totalElementos / (double) tamanioPagina));
    }

    public boolean isHayAnterior() { return pagina > 0; }
    public boolean isHaySiguiente() { return pagina < getTotalPaginas() - 1; }

    /** Posición (1-based) del primer elemento mostrado, para el texto "Mostrando X-Y de Z". */
    public long getPrimerElemento() {
        return totalElementos == 0 ? 0 : (long) pagina * tamanioPagina + 1;
    }

    public long getUltimoElemento() {
        return Math.min((long) (pagina + 1) * tamanioPagina, totalElementos);
    }
}
