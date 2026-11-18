package cl.duocuc.ferreteria_backend.config;

import cl.duocuc.ferreteria_backend.model.Categoria;
import cl.duocuc.ferreteria_backend.model.Producto;
import cl.duocuc.ferreteria_backend.repository.CategoriaRepository;
import cl.duocuc.ferreteria_backend.repository.ProductoRepository;
import java.util.HashMap;
import java.util.Map;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// Carga el catálogo base solo si la tabla de productos está vacía
@Configuration
public class CargaInicialCatalogo {

  @Bean
  CommandLineRunner cargarCatalogo(CategoriaRepository categorias, ProductoRepository productos) {
    return args -> {
      if (productos.count() > 0) {
        return;
      }

      Map<String, Categoria> porNombre = new HashMap<>();
      categorias.findAll().forEach(c -> porNombre.put(c.getNombre(), c));

      for (String[] fila : CatalogoInicial.FILAS) {
        Categoria categoria = porNombre.computeIfAbsent(fila[1], nombre -> {
          Categoria nueva = new Categoria();
          nueva.setNombre(nombre);
          return categorias.save(nueva);
        });

        Producto p = new Producto();
        p.setCodigo(fila[0]);
        p.setCategoria(categoria);
        p.setSubcategoria(fila[2]);
        p.setNombre(fila[3]);
        p.setMarca(fila[4]);
        p.setUnidad(fila[5]);
        p.setDescripcion(fila[6]);
        p.setPrecio(Integer.parseInt(fila[7]));
        p.setStock(Integer.parseInt(fila[8]));
        p.setStockMinimo(Integer.parseInt(fila[9]));
        p.setImagen(fila[10]);
        p.setDescuento(0);
        productos.save(p);
      }
    };
  }
}