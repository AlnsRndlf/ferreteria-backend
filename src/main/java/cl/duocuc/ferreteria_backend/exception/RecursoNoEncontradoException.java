package cl.duocuc.ferreteria_backend.exception;

public class RecursoNoEncontradoException extends RuntimeException {
  public RecursoNoEncontradoException(String mensaje) {
    super(mensaje);
  }
}