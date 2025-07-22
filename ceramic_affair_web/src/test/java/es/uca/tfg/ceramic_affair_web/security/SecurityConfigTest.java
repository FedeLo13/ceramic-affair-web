package es.uca.tfg.ceramic_affair_web.security;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import es.uca.tfg.ceramic_affair_web.entities.Usuario;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Set;


/**
 * Clase de prueba para la configuración de seguridad.
 * Esta clase se utiliza para realizar pruebas de integración relacionadas con la seguridad de la aplicación.
 * 
 * @version 1.0
 */
@SpringBootTest
@AutoConfigureMockMvc
public class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JwtUtils jwtUtils;

    @Test
    @DisplayName("Permitir acceso a recursos públicos")
    public void testPublicAccess() throws Exception {
        mockMvc.perform(get("/api/public/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Hello from public!"));
    }

    @Test
    @DisplayName("Rechazar acceso a recursos protegidos sin autenticación")
    public void testProtectedAccessWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/api/protected/resource"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Permitir acceso al administrador con autenticación")
    public void testAdminAccessWithAuthentication() throws Exception {
        String token = generateTestToken("admin@example.com", Set.of(Rol.ADMIN));
        mockMvc.perform(get("/api/admin/dashboard")
                .header("Authorization", "Bearer " + token))
            .andExpect(status().isOk())
            .andExpect(content().string("Admin dashboard"));
    }

    // Método auxiliar para generar tokens de prueba
    private String generateTestToken(String email, Set<Rol> roles) {
        Usuario user = new Usuario();
        user.setId(1L);
        user.setEmail(email);
        user.setRoles(roles);

        return jwtUtils.generateToken(user);
    }
}
