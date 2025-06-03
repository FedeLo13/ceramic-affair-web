package es.uca.tfg.ceramic_affair_web.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

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

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) // Desactivar CSRF para simplificar las pruebas
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() // Permitir todas las peticiones de momento
            );
        return http.build();
    }
}
