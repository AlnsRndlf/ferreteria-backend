package cl.duocuc.ferreteria_backend.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

// El guardia: en cada petición revisa la pulsera (encabezado Authorization)
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtService jwt;

  public JwtAuthenticationFilter(JwtService jwt) {
    this.jwt = jwt;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
    String encabezado = request.getHeader("Authorization");
    if (encabezado != null && encabezado.startsWith("Bearer ")) {
      try {
        Claims claims = jwt.validar(encabezado.substring(7));
        String rol = claims.get("rol", String.class);
        UsuarioAutenticado usuario = new UsuarioAutenticado(
            claims.getSubject(), claims.get("nombre", String.class), rol);
        var autenticacion = new UsernamePasswordAuthenticationToken(usuario, null,
            List.of(new SimpleGrantedAuthority("ROLE_" + rol)));
        SecurityContextHolder.getContext().setAuthentication(autenticacion);
      } catch (JwtException | IllegalArgumentException e) {
        // Pulsera falsa, vencida o dañada: se ignora y más abajo responde 401
        SecurityContextHolder.clearContext();
      }
    }
    chain.doFilter(request, response);
  }
}
