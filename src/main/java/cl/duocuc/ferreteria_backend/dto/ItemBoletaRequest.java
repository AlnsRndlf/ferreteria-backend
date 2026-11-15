package cl.duocuc.ferreteria_backend.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record ItemBoletaRequest(
    @NotNull Long productoId,
    @Min(1) int cantidad
) {}
