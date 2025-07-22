package es.uca.tfg.ceramic_affair_web.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.uca.tfg.ceramic_affair_web.entities.Usuario;
import io.jsonwebtoken.Jwts;

/**
 * Clase de prueba para JwtUtils.
 * Esta clase contiene pruebas unitarias para verificar el correcto funcionamiento de la clase JwtUtils.
 * 
 * @version 1.0
 */
public class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();
    }

    @Test
    @DisplayName("Generación de token válido")
    void testGenerateValidToken() {
        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@example.com");
        usuario.setRoles(Set.of(Rol.USER));

        String token = jwtUtils.generateToken(usuario);
        assertNotNull(token);
        assertTrue(jwtUtils.validateToken(token));

        String email = jwtUtils.getEmailFromToken(token);
        assertEquals("usuario@example.com", email);

        Long userId = jwtUtils.getUserIdFromToken(token);
        assertEquals(1L, userId);

        Set<String> roles = jwtUtils.getRolesFromToken(token);
        assertTrue(roles.contains("USER"));
    }

    @Test
    @DisplayName("Validación de token expirado")
    void testValidateExpiredToken() {
        JwtUtils jwtUtils = new JwtUtils(
            Jwts.SIG.HS256.key().build(),
            10 // Expiración de 10 milisegundos para pruebas
        );

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@example.com");
        usuario.setRoles(Set.of(Rol.USER));

        String token = jwtUtils.generateToken(usuario);
        try {
            Thread.sleep(20); // Esperar a que el token expire
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertFalse(jwtUtils.validateToken(token));
    }

    @Test
    @DisplayName("Validación de token inválido")
    void testValidateInvalidToken() {
        String invalidToken = "invalid.token.string";
        assertFalse(jwtUtils.validateToken(invalidToken));
    }
}
