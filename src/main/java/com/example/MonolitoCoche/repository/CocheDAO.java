package com.example.MonolitoCoche.repository;

import com.example.MonolitoCoche.model.Coche;

import java.util.List;
import java.util.Optional;

/**
 * CAPA DAO (Acceso a datos): contrato de las operaciones SQL sobre la tabla "coches".
 */
public interface CocheDAO {

    List<Coche> listarTodos();

    Optional<Coche> obtenerPorId(Long id);

    /** Inserta el coche y devuelve el mismo objeto con el id generado por MySQL. */
    Coche guardar(Coche coche);

    void actualizar(Coche coche);

    /** @return true si existía y se borró; false si no había ningún coche con ese id */
    boolean eliminar(Long id);

    boolean existeMatricula(String matricula, Long idExcluido);
}
