package es.uca.tfg.ceramic_affair_web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Configuración de seguridad de la aplicación.
 * Esta clase se encarga de definir las reglas de seguridad y autenticación
 * para la aplicación web.
 * 
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, JwtAuthEntryPoint jwtAuthEntryPoint) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    /**
     * Configuración del filtro de seguridad.
     * Define las reglas de autorización y desactiva CSRF para simplificar las pruebas.
     * 
     * @param http La configuración de seguridad HTTP.
     * @return SecurityFilterChain configurado.
     * @throws Exception Si ocurre un error durante la configuración.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactivar CSRF para simplificar las pruebas
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Usar sesiones sin estado
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthEntryPoint) // Manejar errores de autenticación
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/public/**", // Endpoints públicos
                    "/swagger-ui/**", // Swagger UI
                    "/v3/api-docs/**", // Documentación de la API
                    "swagger-ui.html" // Página principal de Swagger UI
                ).permitAll() // Permitir acceso a los endpoints públicos
                .requestMatchers("/api/admin/**").hasRole("ADMIN") // Requerir rol ADMIN para los endpoints de administración
                .anyRequest().authenticated() // Requerir autenticación para cualquier otra solicitud
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Añadir el filtro JWT antes del filtro de autenticación por nombre de usuario y contraseña
        return http.build();
    }

    /**
     * Bean para codificar contraseñas.
     * Utiliza BCrypt para codificar las contraseñas de los usuarios.
     * 
     * @return PasswordEncoder configurado con BCrypt.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Usar BCrypt para codificar contraseñas
    }
}
