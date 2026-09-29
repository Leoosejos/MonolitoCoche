package com.example.MonolitoCoche.repository;

import com.example.MonolitoCoche.model.Coche;

import java.util.List;
import java.util.Optional;

/**
 * CAPA DAO (Acceso a datos): contrato de las operaciones SQL sobre la tabla "coches".
 */
public interface CocheDAO {

    /** Trae solo la página pedida, ya filtrada y ordenada en la propia consulta SQL (LIMIT/OFFSET). */
    List<Coche> buscar(FiltroCoche filtro);

    /** Cuántos coches cumplen el filtro en total (para pintar el paginador), sin aplicar LIMIT/OFFSET. */
    long contar(FiltroCoche filtro);

    /** Marcas distintas que existen en la tabla, para pintar el checklist de filtros. */
    List<String> listarMarcas();

    Optional<Coche> obtenerPorId(Long id);

    /** Inserta el coche y devuelve el mismo objeto con el id generado por MySQL. */
    Coche guardar(Coche coche);

    void actualizar(Coche coche);

    /** @return true si existía y se borró; false si no había ningún coche con ese id */
    boolean eliminar(Long id);

    boolean existeMatricula(String matricula, Long idExcluido);
}
