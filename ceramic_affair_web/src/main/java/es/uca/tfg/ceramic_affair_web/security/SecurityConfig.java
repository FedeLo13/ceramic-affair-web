package es.uca.tfg.ceramic_affair_web.security;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Configuración de seguridad de la aplicación.
 * Esta clase se encarga de definir las reglas de seguridad y autenticación
 * para la aplicación web.
 * 
 * @version 1.1
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final JwtAuthEntryPoint jwtAuthEntryPoint;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter, JwtAuthEntryPoint jwtAuthEntryPoint) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.jwtAuthEntryPoint = jwtAuthEntryPoint;
    }

    /**
     * Configuración del CORS
     * Permite solicitudes desde el frontend en desarrollo y define los métodos HTTP permitidos.
     * 
     * @return UrlBasedCorsConfigurationSource configurado.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of(frontendUrl)); // Permitir solicitudes desde el frontend
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")); // Permitir todos los métodos HTTP
        configuration.setAllowedHeaders(List.of("*")); // Permitir todos los encabezados
        configuration.setAllowCredentials(true); // Permitir credenciales
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplicar la configuración a todas las rutas
        return source;
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
            .csrf(csrf -> csrf.disable()) // Desactivar CSRF para JWT
            .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configurar CORS
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Usar sesiones sin estado
            )
            .exceptionHandling(ex -> ex
                .authenticationEntryPoint(jwtAuthEntryPoint) // Manejar errores de autenticación
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/api/public/**", // Endpoints públicos
                    "/uploads/**", // Archivos subidos
                    "/swagger-ui/**", // Swagger UI
                    "/v3/api-docs/**", // Documentación de la API
                    "/swagger-ui.html" // Página principal de Swagger UI
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
