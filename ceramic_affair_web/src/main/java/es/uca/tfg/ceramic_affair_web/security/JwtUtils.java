package es.uca.tfg.ceramic_affair_web.security;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import es.uca.tfg.ceramic_affair_web.entities.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.MacAlgorithm;
import io.jsonwebtoken.jackson.io.JacksonDeserializer;


/**
 * Clase utilitaria para manejar JWT (JSON Web Tokens).
 * Esta clase se encargará de generar, validar y extraer información de los JWT utilizados en la aplicación.
 * 
 * @version 1.0
 */
@Component
public class JwtUtils {

    // TODO: En producción, cargar la clave secreta desde variable de entorno o properties.
    private final MacAlgorithm ALGORITHM;

    private final SecretKey SECRET_KEY;

    private final long EXPIRATION_TIME;
    
    /**
     * Constructor sin parámetros de la clase JwtUtils.
     * 
     */
    public JwtUtils() {
        this.ALGORITHM = Jwts.SIG.HS256;
        this.SECRET_KEY = ALGORITHM.key().build();
        this.EXPIRATION_TIME = 3600000; // 1 hora en milisegundos
    }

    /**
     * Constructor de la clase JwtUtils (para tests)
     * Inicializa los parámetros necesarios para la generación y validación de JWT.
     */
    public JwtUtils(SecretKey secretKey, long expirationTime) {
        this.ALGORITHM = Jwts.SIG.HS256;
        this.SECRET_KEY = secretKey;
        this.EXPIRATION_TIME = expirationTime;
    }

    /**
     * Genera un JWT para un usuario, con un tiempo de expiración definido.
     * 
     * @param email El email del usuario para el cual se generará el JWT.
     * @return El JWT generado como String.
     */
    public String generateToken(Usuario usuario) {
        long now = System.currentTimeMillis();
        return Jwts.builder()
            .claims()
                .subject(usuario.getEmail())
                .issuedAt(new Date(now))
                .expiration(new Date(now + EXPIRATION_TIME))
                .add("userId", usuario.getId())
                .add("roles", usuario.getRoles()
                    .stream()
                    .map(Rol::name)
                    .collect(Collectors.toSet()))
            .and()
            .signWith(SECRET_KEY, ALGORITHM)
            .compact();
    }

    /**
     * Extrae el email del usuario desde un JWT.
     * 
     * @param token El JWT del cual se extraerá el email.
     * @return El email del usuario.
     */
    public String getEmailFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.getSubject();
    }

    /**
     * Extrae el ID del usuario desde un JWT.
     * 
     * @param token El JWT del cual se extraerá el ID del usuario.
     * @return El ID del usuario.
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = extractClaims(token);
        return claims.get("userId", Long.class);
    }

    /**
     * Extrae los roles del usuario desde un JWT.
     * 
     * @param token El JWT del cual se extraerán los roles.
     * @return Un conjunto de roles del usuario.
     */
    @SuppressWarnings("unchecked")
    public Set<String> getRolesFromToken(String token) {
        Claims claims = extractClaims(token);
        return new HashSet<>((List<String>) claims.get("roles"));
    }

    /**
     * Valida un JWT para asegurarse de que es correcto y no ha expirado.
     * 
     * @param token El JWT a validar.
     * @return true si el JWT es válido, false en caso contrario.
     */
    public boolean validateToken(String token) {
        try {
            extractClaims(token); // Si el token es válido, extraerá los claims sin lanzar excepción
            return true;
        } catch (Exception e) {
            return false; // El token es inválido o ha expirado
        }
    }

    /**
     * Método privado para extraer los claims del JWT.
     * 
     * @param token El JWT del cual se extraerán los claims.
     * @return Los claims del JWT.
     */
    private Claims extractClaims(String token) {
        return Jwts.parser()
            .verifyWith(SECRET_KEY)
            .json(new JacksonDeserializer<>())
            .build()
            .parseSignedClaims(token)
            .getPayload();
    }
}
