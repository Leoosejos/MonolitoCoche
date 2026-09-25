package com.example.MonolitoCoche.model;

import com.example.MonolitoCoche.model.enums.Combustible;
import com.example.MonolitoCoche.model.enums.Transmision;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

/**
 * CAPA MODELO (POJO)
 * Representa una fila de la tabla "coches". Con JDBC la correspondencia
 * columna <-> atributo la hace CocheDAOJdbc; aquí solo van los datos y su validación.
 */
public class Coche {

    private Long id;

    @NotBlank(message = "La marca es obligatoria")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String marca;

    @NotBlank(message = "El modelo es obligatorio")
    @Size(max = 50, message = "Máximo 50 caracteres")
    private String modelo;

    @NotBlank(message = "La matrícula es obligatoria")
    @Size(max = 15, message = "Máximo 15 caracteres")
    private String matricula;

    // "año" no es un identificador recomendable en Java/SQL, por eso se usa "anio"
    @NotNull(message = "El año es obligatorio")
    @Min(value = 1900, message = "El año debe ser posterior a 1900")
    @Max(value = 2100, message = "El año no es válido")
    private Integer anio;

    @NotBlank(message = "El color es obligatorio")
    @Size(max = 30, message = "Máximo 30 caracteres")
    private String color;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.0", message = "El precio no puede ser negativo")
    @Digits(integer = 10, fraction = 2, message = "Formato de precio no válido")
    private BigDecimal precio;

    @NotNull(message = "El kilometraje es obligatorio")
    @PositiveOrZero(message = "El kilometraje no puede ser negativo")
    private Integer kilometraje;

    @NotNull(message = "Selecciona un tipo de combustible")
    private Combustible combustible;

    @NotNull(message = "Selecciona un tipo de transmisión")
    private Transmision transmision;

    public Coche() {
    }

    // ---------- Getters y Setters ----------

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public Integer getKilometraje() { return kilometraje; }
    public void setKilometraje(Integer kilometraje) { this.kilometraje = kilometraje; }

    public Combustible getCombustible() { return combustible; }
    public void setCombustible(Combustible combustible) { this.combustible = combustible; }

    public Transmision getTransmision() { return transmision; }
    public void setTransmision(Transmision transmision) { this.transmision = transmision; }
}
