package es.uca.tfg.ceramic_affair_web.security;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.uca.tfg.ceramic_affair_web.entities.Usuario;

/**
 * Clase de prueba para JwtUtils.
 * Esta clase contiene pruebas unitarias para verificar el correcto funcionamiento de la clase JwtUtils.
 * 
 * @version 1.1
 */
public class JwtUtilsTest {

    private JwtUtils jwtUtils;

    @BeforeEach
    void setUp() {
        jwtUtils = new JwtUtils();

        String fakeSecret = "myFakeSecretKeyForTestsOnly1234567890";
        jwtUtils.setSecret(fakeSecret);
        jwtUtils.setExpiration(3600000); // 1 hora
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
        JwtUtils jwtUtilsShort = new JwtUtils();
        String fakeSecret = "myFakeSecretKeyForTestsOnly1234567890";
        jwtUtilsShort.setSecret(fakeSecret);
        jwtUtilsShort.setExpiration(10); // Token expira en 10 ms

        Usuario usuario = new Usuario();
        usuario.setId(1L);
        usuario.setEmail("usuario@example.com");
        usuario.setRoles(Set.of(Rol.USER));

        String token = jwtUtilsShort.generateToken(usuario);
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
