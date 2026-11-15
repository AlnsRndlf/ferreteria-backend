package cl.duocuc.ferreteria_backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "detalle_boleta")
@Getter
@Setter
public class DetalleBoleta {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @JsonIgnore
  @ManyToOne(optional = false)
  @JoinColumn(name = "boleta_id")
  private Boleta boleta;

  @ManyToOne(optional = false)
  @JoinColumn(name = "producto_id")
  private Producto producto;

  @Column(nullable = false)
  private int cantidad;

  @Column(nullable = false)
  private int precioUnitario; // precio al momento de la compra

  @Column(nullable = false)
  private int subtotal;
}
