package cl.duocuc.ferreteria_backend.service;

import cl.duocuc.ferreteria_backend.dto.CategoriaRequest;
import cl.duocuc.ferreteria_backend.exception.RecursoNoEncontradoException;
import cl.duocuc.ferreteria_backend.exception.ReglaNegocioException;
import cl.duocuc.ferreteria_backend.model.Categoria;
import cl.duocuc.ferreteria_backend.repository.CategoriaRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class CategoriaService {

  private final CategoriaRepository repository;

  public CategoriaService(CategoriaRepository repository) {
    this.repository = repository;
  }

  public List<Categoria> listar() {
    return repository.findAll();
  }

  public Categoria buscar(Long id) {
    return repository.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Categoría no encontrada: " + id));
  }

  public Categoria crear(CategoriaRequest datos) {
    if (repository.existsByNombreIgnoreCase(datos.nombre().trim())) {
      throw new ReglaNegocioException("Ya existe una categoría con el nombre: " + datos.nombre());
    }
    Categoria categoria = new Categoria();
    categoria.setNombre(datos.nombre().trim());
    return repository.save(categoria);
  }

  public Categoria actualizar(Long id, CategoriaRequest datos) {
    Categoria actual = buscar(id);
    String nuevoNombre = datos.nombre().trim();
    boolean cambioNombre = !actual.getNombre().equalsIgnoreCase(nuevoNombre);
    if (cambioNombre && repository.existsByNombreIgnoreCase(nuevoNombre)) {
      throw new ReglaNegocioException("Ya existe una categoría con el nombre: " + nuevoNombre);
    }
    actual.setNombre(nuevoNombre);
    return repository.save(actual);
  }

  public void eliminar(Long id) {
    repository.delete(buscar(id));
  }
}