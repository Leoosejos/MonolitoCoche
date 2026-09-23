package com.example.MonolitoCoche.service;

import com.example.MonolitoCoche.model.Coche;

import java.util.List;

/**
 * CAPA SERVICIO (Contrato)
 * Define las operaciones de negocio del CRUD.
 * El controlador depende de esta interfaz, no de la implementación.
 */
public interface CocheService {

    List<Coche> listarTodos();

    Coche buscarPorId(Long id);

    Coche guardar(Coche coche);

    Coche actualizar(Long id, Coche coche);

    void eliminar(Long id);

    boolean existeMatricula(String matricula, Long idExcluido);
}
