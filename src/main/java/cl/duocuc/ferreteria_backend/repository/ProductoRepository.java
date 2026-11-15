package cl.duocuc.ferreteria_backend.repository;

import cl.duocuc.ferreteria_backend.model.Producto;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
  Optional<Producto> findByCodigo(String codigo);

  boolean existsByCodigo(String codigo);

  List<Producto> findByCategoriaId(Long categoriaId);

  // Stock crítico: queda poco, pero no está agotado
  @Query("select p from Producto p where p.stock > 0 and p.stock <= p.stockMinimo")
  List<Producto> findStockCritico();
}