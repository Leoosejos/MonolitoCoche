package com.example.MonolitoCoche.service.impl;

import com.example.MonolitoCoche.exception.CocheNotFoundException;
import com.example.MonolitoCoche.model.Coche;
import com.example.MonolitoCoche.repository.CocheRepository;
import com.example.MonolitoCoche.service.CocheService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * CAPA SERVICIO (Implementación)
 * Contiene la lógica de negocio y usa el repositorio para acceder a los datos.
 */
@Service
@Transactional
public class CocheServiceImpl implements CocheService {

    private final CocheRepository cocheRepository;

    // Inyección de dependencias por constructor (forma recomendada)
    public CocheServiceImpl(CocheRepository cocheRepository) {
        this.cocheRepository = cocheRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Coche> listarTodos() {
        return cocheRepository.findAll(Sort.by("marca", "modelo"));
    }

    @Override
    @Transactional(readOnly = true)
    public Coche buscarPorId(Long id) {
        return cocheRepository.findById(id)
                .orElseThrow(() -> new CocheNotFoundException(id));
    }

    @Override
    public Coche guardar(Coche coche) {
        coche.setId(null); // garantiza que siempre se crea un registro nuevo
        normalizar(coche);
        return cocheRepository.save(coche);
    }

    @Override
    public Coche actualizar(Long id, Coche datos) {
        Coche existente = buscarPorId(id);
        normalizar(datos);
        existente.setMarca(datos.getMarca());
        existente.setModelo(datos.getModelo());
        existente.setMatricula(datos.getMatricula());
        existente.setAnio(datos.getAnio());
        existente.setColor(datos.getColor());
        existente.setPrecio(datos.getPrecio());
        existente.setKilometraje(datos.getKilometraje());
        existente.setCombustible(datos.getCombustible());
        existente.setTransmision(datos.getTransmision());
        return cocheRepository.save(existente);
    }

    @Override
    public void eliminar(Long id) {
        if (!cocheRepository.existsById(id)) {
            throw new CocheNotFoundException(id);
        }
        cocheRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existeMatricula(String matricula, Long idExcluido) {
        if (matricula == null || matricula.isBlank()) {
            return false;
        }
        String m = matricula.trim();
        return idExcluido == null
                ? cocheRepository.existsByMatriculaIgnoreCase(m)
                : cocheRepository.existsByMatriculaIgnoreCaseAndIdNot(m, idExcluido);
    }

    /** Limpia espacios y guarda la matrícula siempre en mayúsculas. */
    private void normalizar(Coche coche) {
        coche.setMarca(coche.getMarca().trim());
        coche.setModelo(coche.getModelo().trim());
        coche.setColor(coche.getColor().trim());
        coche.setMatricula(coche.getMatricula().trim().toUpperCase());
    }
}
