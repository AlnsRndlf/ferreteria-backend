package cl.duocuc.ferreteria_backend.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.util.List;

// El cliente ya no se envía: el backend lo toma del token JWT
public record BoletaRequest(
    @NotBlank @Pattern(regexp = "RETIRO|DESPACHO", message = "debe ser RETIRO o DESPACHO") String tipoEntrega,
    @NotEmpty @Valid List<ItemBoletaRequest> items
) {}
