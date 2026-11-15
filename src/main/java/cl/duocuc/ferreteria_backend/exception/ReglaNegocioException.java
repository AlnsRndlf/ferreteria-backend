package cl.duocuc.ferreteria_backend.exception;

// Se lanza cuando se rompe una regla del negocio (ej: código de producto repetido)
public class ReglaNegocioException extends RuntimeException {
  public ReglaNegocioException(String mensaje) {
    super(mensaje);
  }
}