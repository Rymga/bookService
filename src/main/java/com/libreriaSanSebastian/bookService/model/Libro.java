package com.libreriaSanSebastian.bookService.model;

import lombok.*;
import jakarta.persistence.*;

@Entity
@Table(name = "libros")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(nullable = false)
    private String isbn;

    @Column(nullable = false)
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "autor_id")
    private Autor autor;
}