package cl.duocuc.ferreteria_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CategoriaRequest(
    @NotBlank @Size(max = 60) String nombre
) {}
