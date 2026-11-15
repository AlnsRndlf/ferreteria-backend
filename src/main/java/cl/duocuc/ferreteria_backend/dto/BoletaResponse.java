package cl.duocuc.ferreteria_backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public record BoletaResponse(
    Long id,
    String numero,
    String clienteCorreo,
    String clienteNombre,
    LocalDateTime fecha,
    String tipoEntrega,
    String estado,
    int total,
    List<DetalleResponse> detalles
) {
  public record DetalleResponse(
      Long productoId,
      String productoCodigo,
      String productoNombre,
      int cantidad,
      int precioUnitario,
      int subtotal
  ) {}
}
