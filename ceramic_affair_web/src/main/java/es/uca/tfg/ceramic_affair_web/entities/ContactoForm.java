package es.uca.tfg.ceramic_affair_web.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

/**
 * Clase que representa un formulario de contacto.
 * 
 * @version 1.0
 */
@Entity
public class ContactoForm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellidos;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String asunto;

    @Column(columnDefinition = "TEXT")
    @Lob
    private String mensaje;

    @Column(nullable = false)
    @CreationTimestamp
    private LocalDateTime fechaCreacion;

    /**
     * Constructor vacío para JPA
     */
    public ContactoForm() {
    }

    /**
     * Constructor con parámetros para crear un formulario de contacto.
     * 
     * @param nombre   el nombre del contacto
     * @param apellidos los apellidos del contacto
     * @param email    el email del contacto
     * @param asunto   el asunto del mensaje
     * @param mensaje  el contenido del mensaje
     */
    public ContactoForm(String nombre, String apellidos, String email, String asunto, String mensaje) {
        this.nombre = nombre;
        this.apellidos = apellidos;
        this.email = email;
        this.asunto = asunto;
        this.mensaje = mensaje;
    }

    /**
     * Método para obtener el ID del formulario de contacto.
     * 
     * @return el ID del formulario de contacto
     */
    public Long getId() {
        return id;
    }

    /**
     * Método para obtener el nombre del contacto.
     * 
     * @return el nombre del contacto
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Método para obtener los apellidos del contacto.
     * 
     * @return los apellidos del contacto
     */
    public String getApellidos() {
        return apellidos;
    }

    /**
     * Método para obtener el email del contacto.
     * 
     * @return el email del contacto
     */
    public String getEmail() {
        return email;
    }

    /**
     * Método para obtener el asunto del mensaje.
     * 
     * @return el asunto del mensaje
     */
    public String getAsunto() {
        return asunto;
    }

    /**
     * Método para obtener el mensaje del formulario de contacto.
     * 
     * @return el mensaje del formulario de contacto
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Método para obtener la fecha de creación del formulario de contacto.
     * 
     * @return la fecha de creación del formulario de contacto
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Método para establecer el id del formulario de contacto.
     * 
     * @param id el nuevo id del formulario de contacto
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Método para establecer la fecha de creación del formulario de contacto.
     * 
     * @param fechaCreacion la nueva fecha de creación del formulario de contacto
     */
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    /**
     * Método para establecer el nombre del contacto.
     * 
     * @param nombre el nuevo nombre del contacto
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Método para establecer los apellidos del contacto.
     * 
     * @param apellidos los nuevos apellidos del contacto
     */
    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    /**
     * Método para establecer el email del contacto.
     * 
     * @param email el nuevo email del contacto
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Método para establecer el asunto del mensaje.
     * 
     * @param asunto el nuevo asunto del mensaje
     */
    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    /**
     * Método para establecer el mensaje del formulario de contacto.
     * 
     * @param mensaje el nuevo mensaje del formulario de contacto
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}
