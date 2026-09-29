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
import java.util.Map;
import java.util.Optional;

/**
 * CAPA DAO (Implementación JDBC)
 * Escribe el SQL a mano con PreparedStatement (los "?" evitan la inyección SQL) y
 * obtiene la conexión del Singleton Conexion. No cierra la Connection: es compartida.
 */
@Repository
public class CocheDAOJdbc implements CocheDAO {

    private static final String SELECT_CAMPOS =
            "SELECT id, marca, modelo, matricula, anio, color, precio, kilometraje, combustible, transmision "
                    + "FROM coches";

    private static final String SELECT_MARCAS = "SELECT DISTINCT marca FROM coches ORDER BY marca";

    /** Columnas por las que se puede ordenar, indexadas por el nombre que llega del formulario (evita inyección SQL). */
    private static final Map<String, String> COLUMNAS_ORDEN = Map.of(
            "marca", "marca, modelo",
            "precio", "precio",
            "anio", "anio",
            "kilometraje", "kilometraje"
    );

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
    public List<Coche> buscar(FiltroCoche filtro) {
        StringBuilder where = new StringBuilder();
        List<Object> parametros = new ArrayList<>();
        construirWhere(filtro, where, parametros);

        String columnaOrden = COLUMNAS_ORDEN.getOrDefault(filtro.getOrdenarPor(), COLUMNAS_ORDEN.get("marca"));
        String sentido = "desc".equalsIgnoreCase(filtro.getDireccion()) ? "DESC" : "ASC";
        String sql = SELECT_CAMPOS + where + " ORDER BY " + columnaOrden + " " + sentido + " LIMIT ? OFFSET ?";

        List<Coche> coches = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            int i = asignarParametros(ps, parametros, 1);
            ps.setInt(i++, filtro.getTamanioPagina());
            ps.setInt(i, filtro.getPagina() * filtro.getTamanioPagina());
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    coches.add(mapear(rs));
                }
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar los coches", e);
        }
        return coches;
    }

    @Override
    public long contar(FiltroCoche filtro) {
        StringBuilder where = new StringBuilder();
        List<Object> parametros = new ArrayList<>();
        construirWhere(filtro, where, parametros);

        try (PreparedStatement ps = getConnection().prepareStatement("SELECT COUNT(*) FROM coches" + where)) {
            asignarParametros(ps, parametros, 1);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? rs.getLong(1) : 0;
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al contar los coches", e);
        }
    }

    @Override
    public List<String> listarMarcas() {
        List<String> marcas = new ArrayList<>();
        try (PreparedStatement ps = getConnection().prepareStatement(SELECT_MARCAS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                marcas.add(rs.getString("marca"));
            }
        } catch (SQLException e) {
            throw new AccesoDatosException("Error al listar las marcas", e);
        }
        return marcas;
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

    // ---------- Construcción dinámica del WHERE ----------

    /** Añade una condición "columna IN (?,?,...)" por cada filtro con valores, unidas con AND. */
    private void construirWhere(FiltroCoche filtro, StringBuilder where, List<Object> parametros) {
        List<String> condiciones = new ArrayList<>();
        agregarFiltroIn("combustible", filtro.getCombustibles(), condiciones, parametros);
        agregarFiltroIn("transmision", filtro.getTransmisiones(), condiciones, parametros);
        agregarFiltroIn("marca", filtro.getMarcas(), condiciones, parametros);

        if (!condiciones.isEmpty()) {
            where.append(" WHERE ").append(String.join(" AND ", condiciones));
        }
    }

    private void agregarFiltroIn(String columna, List<String> valores, List<String> condiciones, List<Object> parametros) {
        if (valores == null || valores.isEmpty()) {
            return;
        }
        String interrogantes = String.join(",", valores.stream().map(v -> "?").toList());
        condiciones.add(columna + " IN (" + interrogantes + ")");
        parametros.addAll(valores);
    }

    /** @return el siguiente índice libre de parámetro, para poder seguir asignando (LIMIT, OFFSET...) a continuación. */
    private int asignarParametros(PreparedStatement ps, List<Object> parametros, int desde) throws SQLException {
        int i = desde;
        for (Object valor : parametros) {
            ps.setString(i++, (String) valor);
        }
        return i;
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
