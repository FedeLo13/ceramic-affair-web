package es.uca.tfg.ceramic_affair_web.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import es.uca.tfg.ceramic_affair_web.controllers.common.LoginController;
import es.uca.tfg.ceramic_affair_web.entities.Usuario;
import es.uca.tfg.ceramic_affair_web.repositories.UsuarioRepo;
import es.uca.tfg.ceramic_affair_web.security.JwtUtils;
import es.uca.tfg.ceramic_affair_web.security.Rol;

/**
 * Clase de prueba para el controlador de inicio de sesión.
 * Proporciona pruebas de capa web para las operaciones de autenticación, incluyendo el inicio de sesión y la obtención de tokens JWT.
 * 
 * @version 1.0
 */
@WebMvcTest(controllers = LoginController.class)
@AutoConfigureMockMvc // No desactivar la configuración de seguridad en este caso (login no requiere autenticación previa)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UsuarioRepo usuarioRepo;

    @MockitoBean
    private PasswordEncoder passwordEncoder;

    @MockitoBean
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("Controlador - Iniciar sesión con credenciales válidas")
    void testIniciarSesionConCredencialesValidas() throws Exception {
        String email = "admin@example.com";
        String password = "1234";
        String encodedPassword = "encoded1234"; // Simula la contraseña codificada

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail(email);
        usuario.setPassword(encodedPassword);
        usuario.setRoles(Set.of(Rol.ADMIN)); // Simula que el usuario tiene un rol de administrador

        // Simula que el usuario existe y la contraseña es correcta
        when(usuarioRepo.findByEmail(email)).thenReturn(Optional.of(usuario));
        when(passwordEncoder.matches(password, encodedPassword)).thenReturn(true);
        when(jwtUtils.generateToken(usuario)).thenReturn("mocked-jwt-token");

        String jsonBody = """
            {
                "email": "admin@example.com",
                "password": "1234"
            }
        """;

        mockMvc.perform(post("/api/public/login/login")
                .contentType("application/json")
                .content(jsonBody))
            .andExpect(status().isOk())
            .andExpect(jsonPath("success").value(true))
            .andExpect(jsonPath("message").value("Inicio de sesión exitoso"))
            .andExpect(jsonPath("data.token").value("mocked-jwt-token"));
    }

    @Test
    @DisplayName("Controlador - Iniciar sesión con credenciales inválidas")
    void testIniciarSesionConCredencialesInvalidas() throws Exception {
        when(usuarioRepo.findByEmail("user@example.com")).thenReturn(Optional.empty());

        String jsonBody = """
            {
                "email": "user@example.com",
                "password": "wrongpassword"
            }
        """;

        mockMvc.perform(post("/api/public/login/login")
                .contentType("application/json")
                .content(jsonBody))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.status").value(401))
            .andExpect(jsonPath("$.error").value("Excepción de estado de respuesta"))
            .andExpect(jsonPath("$.message").value("Credenciales inválidas"))
            .andExpect(jsonPath("$.path").value("/api/public/login/login"));
    }

    // Configuración para desactivar la seguridad correctamente
    @TestConfiguration
    public static class NoSecurityConfig {
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
            http.csrf(csrf -> csrf.disable()) // Desactivar CSRF para simplificar las pruebas
                .authorizeHttpRequests(auth -> auth
                    .anyRequest().permitAll() // Permitir todas las solicitudes en las pruebas
                );
            return http.build();
        }
    }
}
