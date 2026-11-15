package cl.duocuc.ferreteria_backend.service;

import cl.duocuc.ferreteria_backend.dto.BoletaRequest;
import cl.duocuc.ferreteria_backend.dto.BoletaResponse;
import cl.duocuc.ferreteria_backend.dto.ItemBoletaRequest;
import cl.duocuc.ferreteria_backend.exception.RecursoNoEncontradoException;
import cl.duocuc.ferreteria_backend.exception.ReglaNegocioException;
import cl.duocuc.ferreteria_backend.model.Boleta;
import cl.duocuc.ferreteria_backend.model.DetalleBoleta;
import cl.duocuc.ferreteria_backend.model.Producto;
import cl.duocuc.ferreteria_backend.repository.BoletaRepository;
import cl.duocuc.ferreteria_backend.repository.ProductoRepository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BoletaService {

  private static final Set<String> ESTADOS_VALIDOS =
      Set.of("PENDIENTE", "EN_PREPARACION", "LISTO", "ENTREGADO");

  private final BoletaRepository boletas;
  private final ProductoRepository productos;

  public BoletaService(BoletaRepository boletas, ProductoRepository productos) {
    this.boletas = boletas;
    this.productos = productos;
  }

  @Transactional(readOnly = true)
  public List<BoletaResponse> listar() {
    return boletas.findAll().stream().map(this::aRespuesta).toList();
  }

  @Transactional(readOnly = true)
  public BoletaResponse buscar(Long id) {
    return aRespuesta(buscarEntidad(id));
  }

  @Transactional(readOnly = true)
  public List<BoletaResponse> listarPorCliente(String correo) {
    return boletas.findByClienteCorreoIgnoreCaseOrderByFechaDesc(correo)
        .stream().map(this::aRespuesta).toList();
  }

  // Si algo falla a la mitad, @Transactional deshace todo (no queda stock descontado a medias)
  @Transactional
  public BoletaResponse crear(BoletaRequest datos) {
    Boleta boleta = new Boleta();
    boleta.setNumero("ORDER-" + System.currentTimeMillis());
    boleta.setClienteCorreo(datos.clienteCorreo());
    boleta.setClienteNombre(datos.clienteNombre());
    boleta.setFecha(LocalDateTime.now());
    boleta.setTipoEntrega(datos.tipoEntrega());
    boleta.setEstado("PENDIENTE");

    int total = 0;
    for (ItemBoletaRequest item : datos.items()) {
      Producto producto = productos.findById(item.productoId())
          .orElseThrow(() -> new RecursoNoEncontradoException(
              "Producto no encontrado: " + item.productoId()));

      // REGLA 1: no se puede vender más de lo que hay
      if (item.cantidad() > producto.getStock()) {
        throw new ReglaNegocioException("Stock insuficiente para '" + producto.getNombre()
            + "'. Disponible: " + producto.getStock() + ", solicitado: " + item.cantidad());
      }

      // REGLA 2: se cobra el precio con el descuento de oferta aplicado
      int precioUnitario = precioFinal(producto);
      int subtotal = precioUnitario * item.cantidad();

      // REGLA 3: se descuenta el stock
      producto.setStock(producto.getStock() - item.cantidad());

      DetalleBoleta detalle = new DetalleBoleta();
      detalle.setBoleta(boleta);
      detalle.setProducto(producto);
      detalle.setCantidad(item.cantidad());
      detalle.setPrecioUnitario(precioUnitario);
      detalle.setSubtotal(subtotal);
      boleta.getDetalles().add(detalle);

      total += subtotal;
    }
    boleta.setTotal(total);

    return aRespuesta(boletas.save(boleta));
  }

  @Transactional
  public BoletaResponse cambiarEstado(Long id, String nuevoEstado) {
    if (!ESTADOS_VALIDOS.contains(nuevoEstado)) {
      throw new ReglaNegocioException("Estado inválido. Use uno de: " + ESTADOS_VALIDOS);
    }
    Boleta boleta = buscarEntidad(id);
    boleta.setEstado(nuevoEstado);
    return aRespuesta(boletas.save(boleta));
  }

  private Boleta buscarEntidad(Long id) {
    return boletas.findById(id)
        .orElseThrow(() -> new RecursoNoEncontradoException("Boleta no encontrada: " + id));
  }

  private int precioFinal(Producto p) {
    return (int) Math.round(p.getPrecio() * (100 - p.getDescuento()) / 100.0);
  }

  private BoletaResponse aRespuesta(Boleta b) {
    List<BoletaResponse.DetalleResponse> detalles = b.getDetalles().stream()
        .map(d -> new BoletaResponse.DetalleResponse(
            d.getProducto().getId(),
            d.getProducto().getCodigo(),
            d.getProducto().getNombre(),
            d.getCantidad(),
            d.getPrecioUnitario(),
            d.getSubtotal()))
        .toList();
    return new BoletaResponse(b.getId(), b.getNumero(), b.getClienteCorreo(),
        b.getClienteNombre(), b.getFecha(), b.getTipoEntrega(), b.getEstado(),
        b.getTotal(), detalles);
  }
}
