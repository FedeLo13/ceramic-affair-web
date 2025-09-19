package es.uca.tfg.ceramic_affair_web.configuration;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import es.uca.tfg.ceramic_affair_web.entities.Usuario;
import es.uca.tfg.ceramic_affair_web.repositories.UsuarioRepo;
import es.uca.tfg.ceramic_affair_web.security.Rol;

/**
 * Clase de configuración para inicializar el usuario administrador.
 * Implementa CommandLineRunner para ejecutar código al inicio de la aplicación.
 * 
 * @version 1.0
 */
@Component
//TODO: Cambiar el email y la contraseña del administrador a valores seguros en producción.
public class AdminUserInit implements CommandLineRunner {

    private final UsuarioRepo usuarioRepo;
    private final PasswordEncoder passwordEncoder;

    public AdminUserInit(UsuarioRepo usuarioRepo, PasswordEncoder passwordEncoder) {
        this.usuarioRepo = usuarioRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        
        String emailAdmin = "tepigfe@gmail.com";
        String passwordAdmin = "admin123";

        if (!usuarioRepo.existsByEmail(emailAdmin)) {
            Usuario admin = new Usuario(emailAdmin, passwordEncoder.encode(passwordAdmin), Set.of(Rol.ADMIN));
            usuarioRepo.save(admin);
            System.out.println("Usuario administrador creado: " + emailAdmin);
        } else {
            System.out.println("El usuario administrador ya existe: " + emailAdmin);
        }
    }
}
