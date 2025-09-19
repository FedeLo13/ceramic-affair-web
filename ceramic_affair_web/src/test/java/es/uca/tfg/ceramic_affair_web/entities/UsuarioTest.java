package es.uca.tfg.ceramic_affair_web.entities;

import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.uca.tfg.ceramic_affair_web.security.Rol;

/**
 * Clase de prueba para la entidad Usuario.
 * Proporciona pruebas unitarias para los métodos de la clase Usuario.
 * 
 * @version 1.0
 */
public class UsuarioTest {

    @Test
    @DisplayName("Usuario - Constructor vacío")
    public void testConstructorVacio() {
        Usuario usuario = new Usuario();

        assertNotNull(usuario); // Verifica que la instancia no sea nula
        assertNull(usuario.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertNull(usuario.getEmail()); // Verifica que el email sea nulo (no se ha establecido)
        assertNull(usuario.getPassword()); // Verifica que la contraseña sea nula (no se ha establecido)
        assertNotNull(usuario.getRoles()); // Verifica que los roles no sean nulos
        assertTrue(usuario.getRoles().isEmpty()); // Verifica que los roles estén vacíos
    }

    @Test
    @DisplayName("Usuario - Constructor con parámetros")
    public void testConstructorConParametros() {
        String email = "test@example.com";
        String password = "password";
        Set<Rol> roles = Set.of(Rol.USER); // Asignamos un rol de usuario

        Usuario usuario = new Usuario(email, password, roles);

        assertNotNull(usuario); // Verifica que la instancia no sea nula
        assertNull(usuario.getId()); // Verifica que el ID sea nulo (aún no persiste en la base de datos)
        assertNotNull(usuario.getEmail()); // Verifica que el email no sea nulo
        assertEquals(email, usuario.getEmail()); // Verifica que el email sea el esperado
        assertNotNull(usuario.getPassword()); // Verifica que la contraseña no sea nula
        assertEquals(password, usuario.getPassword()); // Verifica que la contraseña sea la esperada
        assertNotNull(usuario.getRoles()); // Verifica que los roles no sean nulos
        assertEquals(1, usuario.getRoles().size()); // Verifica que se haya establecido un rol
        assertTrue(usuario.getRoles().contains(Rol.USER)); // Verifica que el rol de usuario esté presente
    }

    @Test
    @DisplayName("Usuario - Setters y Getters")
    public void testSettersYGetters() {
        Usuario usuario = new Usuario();

        String email = "test@example.com";
        String password = "password";
        Set<Rol> roles = Set.of(Rol.USER);

        usuario.setEmail(email);
        usuario.setPassword(password);
        usuario.setRoles(roles);

        assertEquals(email, usuario.getEmail()); // Verifica que el email se haya establecido correctamente
        assertEquals(password, usuario.getPassword()); // Verifica que la contraseña se haya establecido correctamente
        assertNotNull(usuario.getRoles()); // Verifica que los roles no sean nulos
        assertEquals(1, usuario.getRoles().size()); // Verifica que se haya establecido un rol
        assertTrue(usuario.getRoles().contains(Rol.USER)); // Verifica que el rol de usuario esté presente
    }
}
