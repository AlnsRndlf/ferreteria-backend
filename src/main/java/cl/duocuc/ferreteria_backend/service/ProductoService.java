package cl.duocuc.ferreteria_backend.service;

import cl.duocuc.ferreteria_backend.dto.ProductoRequest;
import cl.duocuc.ferreteria_backend.exception.RecursoNoEncontradoException;
import cl.duocuc.ferreteria_backend.exception.ReglaNegocioException;
import cl.duocuc.ferreteria_backend.model.Categoria;
import cl.duocuc.ferreteria_backend.model.Producto;
import cl.duocuc.ferreteria_backend.repository.CategoriaRepository;
import cl.duocuc.ferreteria_backend.repository.ProductoRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ProductoService {

  private final ProductoRepository productos;
  private final CategoriaRepository categorias;

  public ProductoService(ProductoRepository productos, CategoriaRepository categorias) {
    this.productos = productos;
    this.categorias = categorias;
  }

  public List<Producto> listar() {
    return productos.findAll();
  }

  public Producto buscar(Long id) {
    return productos.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + id));
  }

  public Producto buscarPorCodigo(String codigo) {
    return productos.findByCodigo(codigo)
        .orElseThrow(() -> new RecursoNoEncontradoException("Producto no encontrado: " + codigo));
  }

  public List<Producto> stockCritico() {
    return productos.findStockCritico();
  }

  public Producto crear(ProductoRequest datos) {
    if (productos.existsByCodigo(datos.codigo())) {
      throw new ReglaNegocioException("Ya existe un producto con el código: " + datos.codigo());
    }
    Producto producto = new Producto();
    copiarDatos(producto, datos);
    return productos.save(producto);
  }

  public Producto actualizar(Long id, ProductoRequest datos) {
    Producto actual = buscar(id);
    boolean cambioCodigo = !actual.getCodigo().equals(datos.codigo());
    if (cambioCodigo && productos.existsByCodigo(datos.codigo())) {
      throw new ReglaNegocioException("Ya existe un producto con el código: " + datos.codigo());
    }
    copiarDatos(actual, datos);
    return productos.save(actual);
  }

  public void eliminar(Long id) {
    productos.delete(buscar(id));
  }

  // Pasa los datos del formulario (DTO) al producto
  private void copiarDatos(Producto producto, ProductoRequest datos) {
    Categoria categoria = categorias.findById(datos.categoriaId())
        .orElseThrow(() -> new RecursoNoEncontradoException(
            "Categoría no encontrada: " + datos.categoriaId()));

    producto.setCodigo(datos.codigo());
    producto.setNombre(datos.nombre());
    producto.setMarca(datos.marca());
    producto.setUnidad(datos.unidad());
    producto.setSubcategoria(datos.subcategoria());
    producto.setDescripcion(datos.descripcion());
    producto.setPrecio(datos.precio());
    producto.setStock(datos.stock());
    producto.setStockMinimo(datos.stockMinimo());
    producto.setDescuento(datos.descuento());
    producto.setImagen(datos.imagen());
    producto.setCategoria(categoria);
  }
}