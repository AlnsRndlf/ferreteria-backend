package cl.duocuc.ferreteria_backend.repository;

import cl.duocuc.ferreteria_backend.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
  boolean existsByNombreIgnoreCase(String nombre);
}