package com.libreriaSanSebastian.bookService.repository;

import com.libreriaSanSebastian.bookService.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Libro findByTitulo(String titulo);

    @Modifying
    @Transactional
    @Query("UPDATE Libro l SET l.stock = l.stock - 1 WHERE l.id = :id AND l.stock > 0")
    int decrementarStock(Long id);

    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.autor WHERE l.id = :id")
    Optional<Libro> findByIdWithAutor(Long id);

    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.autor")
    List<Libro> findAllWithAutor();

    @Query("SELECT l FROM Libro l LEFT JOIN FETCH l.autor WHERE l.titulo = :titulo")
    Libro findByTituloWithAutor(String titulo);
}