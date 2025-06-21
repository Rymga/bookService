package com.libreriaSanSebastian.bookService.model;

import lombok.*;
import jakarta.persistence.*;

/**
 *  Entidad Libro
 *  Representa un libro en el sistema de la librería.
 *  Incluye información básica como título, ISBN, stock y autor.
 */
@Entity
@Table(name = "libros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {
    
    /**
     * Identificador único del libro.
     * Se genera automáticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     *  Titulo del libro.
     *  No puede ser nulo y tiene un maximo de 150 caracteres.
     *  Representa el nombre del libro.
     */
    @Column(nullable = false, length = 150)
    private String titulo;

    /**
     *  Codigo ISBN del libro.
     *  No puede ser nulo y debe ser unico.
     *  Representa el identificador internacional del libro.
     */
    @Column(nullable = false)
    private String isbn;

    /**
     *  Stock del libro.
     *  No puede ser nulo y debe ser un valor positivo.
     *  Representa la cantidad de copias disponibles del libro.
     */
    @Column(nullable = false)
    private Integer stock;

    /**
     *  Autor del libro.
     *  Relación muchos a uno con la entidad Autor.
    */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "autor_id")
    private Autor autor;
}