package cl.duocuc.ferreteria_backend.security;

// Los datos de la persona que mostró la pulsera (token) válida
public record UsuarioAutenticado(String correo, String nombre, String rol) {}
