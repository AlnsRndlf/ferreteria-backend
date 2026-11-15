package cl.duocuc.ferreteria_backend.repository;

import cl.duocuc.ferreteria_backend.model.Boleta;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoletaRepository extends JpaRepository<Boleta, Long> {
  Optional<Boleta> findByNumero(String numero);

  List<Boleta> findByClienteCorreoIgnoreCaseOrderByFechaDesc(String correo);
}