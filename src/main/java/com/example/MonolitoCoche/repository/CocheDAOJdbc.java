package com.example.MonolitoCoche.repository;

import com.example.MonolitoCoche.exception.AccesoDatosException;
import com.example.MonolitoCoche.model.Coche;
import com.example.MonolitoCoche.model.enums.Combustible;
import com.example.MonolitoCoche.model.enums.Transmision;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * CAPA DAO (Implementación JDBC)
 * Escribe el SQL a mano con PreparedStatement (los "?" evitan la inyección SQL) y
 * obtiene la conexión del Singleton Conexion. No cierra la Connection: es compartida.
 */
@Repository
public class CocheDAOJdbc implements CocheDAO {

    private static final String SELECT_TODOS =
            "SELECT id, marca, modelo, matricula, anio, color, precio, kilometraje, combustible, transmision "
                    + "FROM coches ORDER BY marca, modelo";

    private static final String SELECT_POR_ID =
            "SELECT id, marca, modelo, matricula, anio, color, precio, kilometraje, combustible, transmision "
                    + "FROM coches WHERE id = ?";

    private static final String INSERT =
            "INSERT INTO coches (marca, modelo, matricula, anio, color, precio, kilometraje, combustible, transmision) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

    private static final String UPDATE =
            "UPDATE coches SET marca = ?, modelo = ?, matricula = ?, anio = ?, color = ?, precio = ?, "
                    + "kilometraje = ?, combustible = ?, transmision = ? WHERE id = ?";

    private static final String DELETE = "DELETE FROM coches WHERE id = ?";

    private static final String EXISTE_MATRICULA =
            "SELECT COUNT(*) FROM coches WHERE LOWER(matricula) = LOWER(?)";

    private static final String EXISTE_MATRICULA_EXCLUYENDO_ID = EXISTE_MATRICULA + " AND id <> ?";

    private Connection getConnection() {
        return Conexion.getInstancia().getConnection();
    }

    // ---------- READ ----------

    @Override
    public List<Coche> listarTodos() {
        List<Coche> coches = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_TODOS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                coches.add(mapear(rs));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar los coches", e);
        }
        return coches;
    }

    @Override
    public Optional<Coche> obtenerPorId(Long id) {
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_POR_ID)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? Optional.of(mapear(rs)) : Optional.empty();
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al buscar el coche " + id, e);
        }
    }

    // ---------- CREATE ----------

    @Override
    public Coche guardar(Coche coche) {
        try (PreparedStatement ps = getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            rellenar(ps, coche);
            ps.executeUpdate();
            try (ResultSet claves = ps.getGeneratedKeys()) {
                if (claves.next()) {
                    coche.setId(claves.getLong(1));
                }
            }
            return coche;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al guardar el coche", e);
        }
    }

    // ---------- UPDATE ----------

    @Override
    public void actualizar(Coche coche) {
        try (PreparedStatement ps = getConnection().prepareStatement(UPDATE)) {
            rellenar(ps, coche);
            ps.setLong(10, coche.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al actualizar el coche " + coche.getId(), e);
        }
    }

    // ---------- DELETE ----------

    @Override
    public boolean eliminar(Long id) {
        try (PreparedStatement ps = getConnection().prepareStatement(DELETE)) {
            ps.setLong(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al eliminar el coche " + id, e);
        }
    }

    // ---------- Consultas auxiliares ----------

    @Override
    public boolean existeMatricula(String matricula, Long idExcluido) {
        String sql = idExcluido == null ? EXISTE_MATRICULA : EXISTE_MATRICULA_EXCLUYENDO_ID;
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, matricula);
            if (idExcluido != null) {
                ps.setLong(2, idExcluido);
            }
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getLong(1) > 0;
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al comprobar la matrícula", e);
        }
    }

    // ---------- Auxiliares de mapeo ----------

    /** Parámetros 1..9 comunes a INSERT y UPDATE (mismo orden de columnas). */
    private void rellenar(PreparedStatement ps, Coche c) throws SQLException {
        ps.setString(1, c.getMarca());
        ps.setString(2, c.getModelo());
        ps.setString(3, c.getMatricula());
        ps.setInt(4, c.getAnio());
        ps.setString(5, c.getColor());
        ps.setBigDecimal(6, c.getPrecio());
        ps.setInt(7, c.getKilometraje());
        ps.setString(8, c.getCombustible().name());
        ps.setString(9, c.getTransmision().name());
    }

    /** Convierte la fila actual del ResultSet en un objeto Coche. */
    private Coche mapear(ResultSet rs) throws SQLException {
        Coche c = new Coche();
        c.setId(rs.getLong("id"));
        c.setMarca(rs.getString("marca"));
        c.setModelo(rs.getString("modelo"));
        c.setMatricula(rs.getString("matricula"));
        c.setAnio(rs.getInt("anio"));
        c.setColor(rs.getString("color"));
        c.setPrecio(rs.getBigDecimal("precio"));
        c.setKilometraje(rs.getInt("kilometraje"));
        c.setCombustible(Combustible.valueOf(rs.getString("combustible")));
        c.setTransmision(Transmision.valueOf(rs.getString("transmision")));
        return c;
    }

    @PreDestroy
    void cerrarConexion() {
        Conexion.getInstancia().cerrarConexion();
    }
}
