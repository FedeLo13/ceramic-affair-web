package es.uca.tfg.ceramic_affair_web.controllers.common;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import es.uca.tfg.ceramic_affair_web.DTOs.LoginDTO;
import es.uca.tfg.ceramic_affair_web.entities.Usuario;
import es.uca.tfg.ceramic_affair_web.payload.ApiResponseType;
import es.uca.tfg.ceramic_affair_web.repositories.UsuarioRepo;
import es.uca.tfg.ceramic_affair_web.security.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;

/**
 * Controlador para la gestión de inicio de sesión.
 * Este controlador maneja las solicitudes de inicio de sesión y proporciona endpoints para autenticar usuarios.
 * 
 * @version 1.0
 */
@RestController
@RequestMapping("/api/public/login")
@Tag(name = "Login", description = "Controlador para la gestión de inicio de sesión")
@CrossOrigin(origins = "http://localhost:5173")
// TODO: Cambiar la URL de origen a la de producción cuando esté disponible
public class LoginController {

    @Autowired
    private UsuarioRepo usuarioRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión", description = "Permite a un usuario iniciar sesión con su email y contraseña", tags = { "Login" })
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos proporcionados"),
        @ApiResponse(responseCode = "401", description = "Credenciales inválidas"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    public ResponseEntity<ApiResponseType<Map<String, String>>> login(@Valid @RequestBody LoginDTO loginDTO) {
        Usuario usuario = usuarioRepo.findByEmail(loginDTO.getEmail())
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        if (!passwordEncoder.matches(loginDTO.getPassword(), usuario.getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        String token = jwtUtils.generateToken(usuario);
        return ResponseEntity.ok(new ApiResponseType<>(true, "Inicio de sesión exitoso", Map.of("token", token)));
    }
}
