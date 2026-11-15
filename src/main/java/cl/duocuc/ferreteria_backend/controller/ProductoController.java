package cl.duocuc.ferreteria_backend.controller;

import cl.duocuc.ferreteria_backend.dto.ProductoRequest;
import cl.duocuc.ferreteria_backend.model.Producto;
import cl.duocuc.ferreteria_backend.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/productos")
@Tag(name = "Productos", description = "Gestión del catálogo e inventario")
public class ProductoController {

  private final ProductoService service;

  public ProductoController(ProductoService service) {
    this.service = service;
  }

  @Operation(summary = "Listar todos los productos")
  @GetMapping
  public List<Producto> listar() {
    return service.listar();
  }

  @Operation(summary = "Listar productos con stock crítico (queda poco, pero no está agotado)")
  @GetMapping("/stock-critico")
  public List<Producto> stockCritico() {
    return service.stockCritico();
  }

  @Operation(summary = "Buscar un producto por id")
  @GetMapping("/{id}")
  public Producto buscar(@PathVariable Long id) {
    return service.buscar(id);
  }

  @Operation(summary = "Crear un producto")
  @PostMapping
  public ResponseEntity<Producto> crear(@Valid @RequestBody ProductoRequest datos) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(datos));
  }

  @Operation(summary = "Actualizar un producto")
  @PutMapping("/{id}")
  public Producto actualizar(@PathVariable Long id, @Valid @RequestBody ProductoRequest datos) {
    return service.actualizar(id, datos);
  }

  @Operation(summary = "Eliminar un producto")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminar(@PathVariable Long id) {
    service.eliminar(id);
  }
}