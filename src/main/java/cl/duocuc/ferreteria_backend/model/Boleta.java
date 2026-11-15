package cl.duocuc.ferreteria_backend.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "boletas")
@Getter
@Setter
public class Boleta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, unique = true, length = 40)
  private String numero; // ej: ORDER-1731600000000

  // Referencia lógica al usuario del servicio Auth (sin llave foránea: otra base de datos)
  @Column(nullable = false)
  private String clienteCorreo;

  private String clienteNombre;

  @Column(nullable = false)
  private LocalDateTime fecha;

  @Column(nullable = false, length = 20)
  private String tipoEntrega; // RETIRO o DESPACHO

  @Column(nullable = false, length = 20)
  private String estado; // PENDIENTE, EN_PREPARACION, LISTO, ENTREGADO

  @Column(nullable = false)
  private int total;

  @OneToMany(mappedBy = "boleta", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<DetalleBoleta> detalles = new ArrayList<>();
}
