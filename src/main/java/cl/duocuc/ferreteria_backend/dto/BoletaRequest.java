package cl.duocuc.ferreteria_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

public record BoletaRequest(
    @NotBlank @Email String clienteCorreo,
    String clienteNombre,
    @NotBlank @Pattern(regexp = "RETIRO|DESPACHO", message = "debe ser RETIRO o DESPACHO") String tipoEntrega,
    @NotEmpty @Valid List<ItemBoletaRequest> items
) {}
