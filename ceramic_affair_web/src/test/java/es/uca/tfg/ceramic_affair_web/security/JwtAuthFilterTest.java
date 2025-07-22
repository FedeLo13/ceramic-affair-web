package es.uca.tfg.ceramic_affair_web.security;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;

/**
 * Clase de prueba para JwtAuthFilter.
 * Esta clase contiene pruebas unitarias para verificar el correcto funcionamiento del filtro de autenticación JWT.
 * 
 * @version 1.0
 */
@ExtendWith(MockitoExtension.class)
public class JwtAuthFilterTest {

    @Mock
    private JwtUtils jwtUtils;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    @Mock
    private FilterChain filterChain;

    private MockHttpServletRequest request;
    private MockHttpServletResponse response;

    @BeforeEach
    void setUp() {
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
    }

    @Test
    @DisplayName("Filtro de autenticación con token válido")
    void testDoFilterInternal_ValidToken() throws Exception {
        String token = "valid.jwt.token";
        request.addHeader("Authorization", "Bearer " + token);

        // Simula el comportamiento de jwtUtils para un token válido
        when(jwtUtils.validateToken(token)).thenReturn(true);
        when(jwtUtils.getEmailFromToken(token)).thenReturn("test@example.com");
        when(jwtUtils.getRolesFromToken(token)).thenReturn(Set.of("USER"));

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verifica que el filtro continúe con la cadena de filtros
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertEquals("test@example.com", auth.getName());
        assertTrue(auth.getAuthorities().stream().
            anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_USER")));

        // Limpia el contexto de seguridad después de la prueba
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Filtro de autenticación sin token")
    void testDoFilterInternal_NoToken() throws Exception {
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Verifica que no se haya establecido ningún contexto de seguridad
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth);
    }
}
