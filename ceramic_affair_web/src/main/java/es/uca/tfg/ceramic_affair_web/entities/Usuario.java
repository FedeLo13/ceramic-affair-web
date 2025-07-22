package es.uca.tfg.ceramic_affair_web.entities;

import java.util.HashSet;
import java.util.Set;

import es.uca.tfg.ceramic_affair_web.security.Rol;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Id;

/**
 * Clase que representa un usuario en el sistema.
 * 
 * @version 1.0
 */
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "usuario_roles", joinColumns = @JoinColumn(name = "usuario_id"))
    @Column(name = "rol")
    private Set<Rol> roles = new HashSet<>();

    /**
     * Constructor vacío para JPA.
     */
    public Usuario() {
    }

    /**
     * Constructor con parámetros para crear un usuario.
     * 
     * @param email El correo electrónico del usuario.
     * @param password La contraseña del usuario.
     * @param roles Los roles asignados al usuario.
     */
    public Usuario(String email, String password, Set<Rol> roles) {
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    /**
     * Método para obtener el ID del usuario.
     * 
     * @return El ID del usuario.
     */
    public Long getId() {
        return id;
    }

    /**
     * Método para obtener el correo electrónico del usuario.
     * 
     * @return El correo electrónico del usuario.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Método para obtener la contraseña del usuario.
     * 
     * @return La contraseña del usuario.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Método para obtener los roles del usuario.
     * 
     * @return Un conjunto de roles asignados al usuario.
     */
    public Set<Rol> getRoles() {
        return roles;
    }

    /**
     * Método para establecer el ID del usuario.
     * 
     * @param id El nuevo ID del usuario.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Método para establecer el email del usuario.
     * 
     * @param email El nuevo correo electrónico del usuario.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Método para establecer la contraseña del usuario.
     * 
     * @param password La nueva contraseña del usuario.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Método para establecer los roles del usuario.
     * 
     * @param roles El nuevo conjunto de roles del usuario.
     */
    public void setRoles(Set<Rol> roles) {
        this.roles = roles;
    }
}
