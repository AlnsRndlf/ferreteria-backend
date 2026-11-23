package cl.duocuc.ferreteria_backend.config;

import cl.duocuc.ferreteria_backend.security.JwtAuthenticationFilter;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtFilter)
      throws Exception {
    return http.csrf(csrf -> csrf.disable())
        .cors(Customizer.withDefaults())
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
            .requestMatchers("/swagger-ui/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()

            // Productos y categorías: ver = cualquier usuario con sesión; modificar = ADMIN
            .requestMatchers(HttpMethod.GET, "/api/v1/productos/stock-critico").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/productos/**", "/api/v1/categorias/**").authenticated()
            .requestMatchers("/api/v1/productos/**", "/api/v1/categorias/**").hasRole("ADMIN")

            // Boletas
            .requestMatchers(HttpMethod.POST, "/api/v1/boletas").hasAnyRole("ADMIN", "CLIENTE")
            .requestMatchers(HttpMethod.GET, "/api/v1/boletas").hasAnyRole("ADMIN", "VENDEDOR")
            .requestMatchers(HttpMethod.GET, "/api/v1/boletas/por-cliente").hasRole("ADMIN")
            .requestMatchers(HttpMethod.GET, "/api/v1/boletas/mias").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/v1/boletas/numero/*").authenticated()
            .requestMatchers(HttpMethod.GET, "/api/v1/boletas/*").authenticated()
            .requestMatchers(HttpMethod.PUT, "/api/v1/boletas/*/estado").hasRole("ADMIN")

            // Todo lo demás está prohibido
            .anyRequest().denyAll())
        // Sin pulsera válida -> 401
        .exceptionHandling(e -> e.authenticationEntryPoint(
            new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration c = new CorsConfiguration();
    c.setAllowedOrigins(List.of("http://localhost:5173"));
    c.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
    c.setAllowedHeaders(List.of("Content-Type", "Authorization"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/api/**", c);
    return source;
  }
}
