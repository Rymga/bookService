package com.libreriaSanSebastian.bookService.model;

import lombok.*;
import jakarta.persistence.*;

/**
 * Entidad Autor
 * 
 * Representa un autor en el sistema de la librería.
 * Incluye información básica como nombre, apellido y nacionalidad.
 */
@Entity
@Table(name = "autores")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Autor {
    /**
     * Identificador único del autor.
     * Se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre del autor.
     * No puede ser nulo y tiene un máximo de 100 caracteres.
     */
    @Column(nullable = false, length = 100)
    private String nombre;

    /**
     * Apellido del autor.
     * No puede ser nulo y tiene un máximo de 100 caracteres.
     */
    @Column(nullable = false, length = 100)
    private String apellido;

    /**
     * Nacionalidad del autor.
     * Campo opcional, máximo 50 caracteres.
     */
    @Column(length = 50)
    private String nacionalidad;
}