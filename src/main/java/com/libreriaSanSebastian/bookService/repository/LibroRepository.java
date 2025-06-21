package com.libreriaSanSebastian.bookService.repository;

import com.libreriaSanSebastian.bookService.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio de Libros
 * 
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas sobre la entidad Libro.
 */
@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    
    /**
     * Busca un libro por su título.
     * 
     * @param titulo Título del libro.
     * @return Libro encontrado o null si no existe.
     */
    Libro findByTitulo(String titulo);

    /**
     * Disminuye el stock de un libro en 1, solo si el stock es mayor a 0.
     * 
     * @param id Identificador del libro.
     * @return Número de filas afectadas (1 si se actualizó, 0 si no).
     */
    @Modifying
    @Transactional
    @Query("UPDATE Libro l SET l.stock = l.stock - 1 WHERE l.id = :id AND l.stock > 0")
    int decrementarStock(Long id);

    /**
     * Busca un libro por su id e incluye la información del autor.
     * 
     * @param id Identificador del libro.
     * @return Optional con el libro y su autor si existe.
     */
    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.autor WHERE l.id = :id")
    Optional<Libro> findByIdWithAutor(Long id);

    /**
     * Obtiene todos los libros junto con la información de sus autores.
     * 
     * @return Lista de libros con autores.
     */
    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.autor")
    List<Libro> findAllWithAutor();

    /**
     * Busca un libro por su título e incluye la información del autor.
     * 
     * @param titulo Título del libro.
     * @return Libro encontrado con su autor o null si no existe.
     */
    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.autor WHERE l.titulo = :titulo")
    Libro findByTituloWithAutor(String titulo);
}