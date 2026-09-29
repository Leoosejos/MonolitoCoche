package com.example.MonolitoCoche.service;

import com.example.MonolitoCoche.model.Coche;
import com.example.MonolitoCoche.repository.FiltroCoche;

import java.util.List;

/**
 * CAPA SERVICIO (Contrato)
 * Define las operaciones de negocio del CRUD.
 * El controlador depende de esta interfaz, no de la implementación.
 */
public interface CocheService {

    /** Coches de la página pedida, ya filtrados y ordenados. */
    List<Coche> buscar(FiltroCoche filtro);

    /** Total de coches que cumplen el filtro (sin paginar), para calcular el número de páginas. */
    long contar(FiltroCoche filtro);

    List<String> listarMarcas();

    Coche buscarPorId(Long id);

    Coche guardar(Coche coche);

    Coche actualizar(Long id, Coche coche);

    void eliminar(Long id);

    boolean existeMatricula(String matricula, Long idExcluido);
}
