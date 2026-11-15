package cl.duocuc.ferreteria_backend.dto;

import jakarta.validation.constraints.*;

public record ProductoRequest(
    @NotBlank @Size(max = 20) String codigo,
    @NotBlank @Size(max = 150) String nombre,
    @Size(max = 60) String marca,
    @Size(max = 30) String unidad,
    @Size(max = 60) String subcategoria,
    @Size(max = 500) String descripcion,
    @Min(0) int precio,
    @Min(0) int stock,
    @Min(0) int stockMinimo,
    @Min(0) @Max(100) int descuento,
    String imagen,
    @NotNull Long categoriaId
) {}