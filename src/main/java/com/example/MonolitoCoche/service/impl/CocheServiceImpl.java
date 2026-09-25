package com.example.MonolitoCoche.service.impl;

import com.example.MonolitoCoche.exception.CocheNotFoundException;
import com.example.MonolitoCoche.model.Coche;
import com.example.MonolitoCoche.repository.CocheDAO;
import com.example.MonolitoCoche.service.CocheService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * CAPA SERVICIO (Implementación)
 * Contiene la lógica de negocio y usa el DAO para acceder a los datos.
 */
@Service
public class CocheServiceImpl implements CocheService {

    private final CocheDAO cocheDAO;

    // Inyección de dependencias por constructor (forma recomendada)
    public CocheServiceImpl(CocheDAO cocheDAO) {
        this.cocheDAO = cocheDAO;
    }

    @Override
    public List<Coche> listarTodos() {
        return cocheDAO.listarTodos();
    }

    @Override
    public Coche buscarPorId(Long id) {
        return cocheDAO.obtenerPorId(id)
                .orElseThrow(() -> new CocheNotFoundException(id));
    }

    @Override
    public Coche guardar(Coche coche) {
        coche.setId(null); // garantiza que siempre se crea un registro nuevo
        normalizar(coche);
        return cocheDAO.guardar(coche);
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
        cocheDAO.actualizar(existente);
        return existente;
    }

    @Override
    public void eliminar(Long id) {
        if (!cocheDAO.eliminar(id)) {
            throw new CocheNotFoundException(id);
        }
    }

    @Override
    public boolean existeMatricula(String matricula, Long idExcluido) {
        if (matricula == null || matricula.isBlank()) {
            return false;
        }
        return cocheDAO.existeMatricula(matricula.trim(), idExcluido);
    }

    /** Limpia espacios y guarda la matrícula siempre en mayúsculas. */
    private void normalizar(Coche coche) {
        coche.setMarca(coche.getMarca().trim());
        coche.setModelo(coche.getModelo().trim());
        coche.setColor(coche.getColor().trim());
        coche.setMatricula(coche.getMatricula().trim().toUpperCase());
    }
}
