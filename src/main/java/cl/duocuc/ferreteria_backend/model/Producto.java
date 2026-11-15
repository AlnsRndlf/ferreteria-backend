package cl.duocuc.ferreteria_backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "productos")
@Getter
@Setter
public class Producto {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank
  @Size(max = 20)
  @Column(nullable = false, unique = true, length = 20)
  private String codigo; // ej: MC001

  @NotBlank
  @Size(max = 150)
  @Column(nullable = false, length = 150)
  private String nombre;

  @Size(max = 60)
  private String marca;

  @Size(max = 30)
  private String unidad;

  @Size(max = 60)
  private String subcategoria;

  @Size(max = 500)
  private String descripcion;

  @Min(0)
  @Column(nullable = false)
  private int precio; // pesos chilenos, sin decimales

  @Min(0)
  @Column(nullable = false)
  private int stock;

  @Min(0)
  @Column(nullable = false)
  private int stockMinimo; // punto de reposición

  @Min(0)
  @Max(100)
  private int descuento; // porcentaje de oferta

  private String imagen;

  @NotNull
  @ManyToOne(optional = false)
  @JoinColumn(name = "categoria_id")
  private Categoria categoria;
}
