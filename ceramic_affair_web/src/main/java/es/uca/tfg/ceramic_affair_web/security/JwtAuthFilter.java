package es.uca.tfg.ceramic_affair_web.security;

import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Filtro de autenticación JWT.
 * Este filtro intercepta las solicitudes HTTP para verificar la validez del JWT y extraer la
 * información del usuario autenticado.
 * 
 * @version 1.0
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;

    public JwtAuthFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filter) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extrae el token del encabezado (elimina "Bearer ")

            if (jwtUtils.validateToken(token)) {
                String email = jwtUtils.getEmailFromToken(token);

                var roles = jwtUtils.getRolesFromToken(token).stream()
                    .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol))
                    .collect(Collectors.toList());

                var auth = new UsernamePasswordAuthenticationToken(email, null, roles);
                auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                 // Establece el contexto de seguridad con la autenticación
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }

        // Continúa con la cadena de filtros
        filter.doFilter(request, response);
    }
}
