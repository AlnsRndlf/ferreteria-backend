package cl.duocuc.ferreteria_backend.controller;

import cl.duocuc.ferreteria_backend.dto.BoletaRequest;
import cl.duocuc.ferreteria_backend.dto.BoletaResponse;
import cl.duocuc.ferreteria_backend.dto.EstadoRequest;
import cl.duocuc.ferreteria_backend.service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/boletas")
@Tag(name = "Boletas", description = "Ventas: crear una boleta descuenta el stock automáticamente")
public class BoletaController {

  private final BoletaService service;

  public BoletaController(BoletaService service) {
    this.service = service;
  }

  @Operation(summary = "Listar todas las boletas")
  @GetMapping
  public List<BoletaResponse> listar() {
    return service.listar();
  }

  @Operation(summary = "Listar las boletas de un cliente por su correo")
  @GetMapping("/por-cliente")
  public List<BoletaResponse> listarPorCliente(@RequestParam String correo) {
    return service.listarPorCliente(correo);
  }

  @Operation(summary = "Buscar una boleta por id (con su detalle)")
  @GetMapping("/{id}")
  public BoletaResponse buscar(@PathVariable Long id) {
    return service.buscar(id);
  }

  @Operation(summary = "Crear una boleta (valida y descuenta el stock)")
  @PostMapping
  public ResponseEntity<BoletaResponse> crear(@Valid @RequestBody BoletaRequest datos) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(datos));
  }

  @Operation(summary = "Cambiar el estado de una boleta (PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO)")
  @PutMapping("/{id}/estado")
  public BoletaResponse cambiarEstado(@PathVariable Long id, @Valid @RequestBody EstadoRequest datos) {
    return service.cambiarEstado(id, datos.estado());
  }
}