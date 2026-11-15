package cl.duocuc.ferreteria_backend.controller;

import cl.duocuc.ferreteria_backend.dto.CategoriaRequest;
import cl.duocuc.ferreteria_backend.model.Categoria;
import cl.duocuc.ferreteria_backend.service.CategoriaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/categorias")
@Tag(name = "Categorías", description = "Gestión de categorías de productos")
public class CategoriaController {

  private final CategoriaService service;

  public CategoriaController(CategoriaService service) {
    this.service = service;
  }

  @Operation(summary = "Listar todas las categorías")
  @GetMapping
  public List<Categoria> listar() {
    return service.listar();
  }

  @Operation(summary = "Buscar una categoría por id")
  @GetMapping("/{id}")
  public Categoria buscar(@PathVariable Long id) {
    return service.buscar(id);
  }

  @Operation(summary = "Crear una categoría")
  @PostMapping
  public ResponseEntity<Categoria> crear(@Valid @RequestBody CategoriaRequest datos) {
    return ResponseEntity.status(HttpStatus.CREATED).body(service.crear(datos));
  }

  @Operation(summary = "Actualizar una categoría")
  @PutMapping("/{id}")
  public Categoria actualizar(@PathVariable Long id, @Valid @RequestBody CategoriaRequest datos) {
    return service.actualizar(id, datos);
  }

  @Operation(summary = "Eliminar una categoría")
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  public void eliminar(@PathVariable Long id) {
    service.eliminar(id);
  }
}
