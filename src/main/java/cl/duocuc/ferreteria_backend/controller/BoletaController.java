package cl.duocuc.ferreteria_backend.controller;

import cl.duocuc.ferreteria_backend.dto.BoletaRequest;
import cl.duocuc.ferreteria_backend.dto.BoletaResponse;
import cl.duocuc.ferreteria_backend.dto.EstadoRequest;
import cl.duocuc.ferreteria_backend.security.UsuarioAutenticado;
import cl.duocuc.ferreteria_backend.service.BoletaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/boletas")
@Tag(name = "Boletas", description = "Ventas: crear una boleta descuenta el stock automáticamente")
public class BoletaController {

  private final BoletaService service;

  public BoletaController(BoletaService service) {
    this.service = service;
  }

  @Operation(summary = "Listar todas las boletas (ADMIN y VENDEDOR)")
  @GetMapping
  public List<BoletaResponse> listar() {
    return service.listar();
  }

  @Operation(summary = "Listar MIS boletas (las del usuario del token)")
  @GetMapping("/mias")
  public List<BoletaResponse> mias(@Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario) {
    return service.listarPorCliente(usuario.correo());
  }

  @Operation(summary = "Buscar una boleta por id. Un CLIENTE solo puede ver las suyas")
  @GetMapping("/{id}")
  public BoletaResponse buscar(@PathVariable Long id,
      @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario) {
    BoletaResponse boleta = service.buscar(id);
    verificarAcceso(boleta, usuario);
    return boleta;
  }

  @Operation(summary = "Buscar una boleta por su número (ej: ORDER-1731600000000)")
  @GetMapping("/numero/{numero}")
  public BoletaResponse buscarPorNumero(@PathVariable String numero,
      @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario) {
    BoletaResponse boleta = service.buscarPorNumero(numero);
    verificarAcceso(boleta, usuario);
    return boleta;
  }

  // Un CLIENTE solo puede ver sus propias boletas
  private void verificarAcceso(BoletaResponse boleta, UsuarioAutenticado usuario) {
    boolean esCliente = "CLIENTE".equals(usuario.rol());
    if (esCliente && !boleta.clienteCorreo().equalsIgnoreCase(usuario.correo())) {
      throw new AccessDeniedException("No puedes ver boletas de otro cliente");
    }
  }

  @Operation(summary = "Crear una boleta a nombre del usuario del token (valida y descuenta el stock)")
  @PostMapping
  public ResponseEntity<BoletaResponse> crear(@Valid @RequestBody BoletaRequest datos,
      @Parameter(hidden = true) @AuthenticationPrincipal UsuarioAutenticado usuario) {
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(service.crear(datos, usuario.correo(), usuario.nombre()));
  }

  @Operation(summary = "Cambiar el estado de una boleta (solo ADMIN)")
  @PutMapping("/{id}/estado")
  public BoletaResponse cambiarEstado(@PathVariable Long id, @Valid @RequestBody EstadoRequest datos) {
    return service.cambiarEstado(id, datos.estado());
  }
}
