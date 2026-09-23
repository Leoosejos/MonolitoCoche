package com.example.MonolitoCoche.repository;

import com.example.MonolitoCoche.model.Coche;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * CAPA REPOSITORIO (Acceso a datos)
 * Al extender JpaRepository, Spring genera automáticamente:
 * findAll, findById, save, deleteById, existsById, count...
 */
@Repository
public interface CocheRepository extends JpaRepository<Coche, Long> {

    // Consultas derivadas del nombre del método (Spring las implementa solo)
    boolean existsByMatriculaIgnoreCase(String matricula);

    boolean existsByMatriculaIgnoreCaseAndIdNot(String matricula, Long id);
}
