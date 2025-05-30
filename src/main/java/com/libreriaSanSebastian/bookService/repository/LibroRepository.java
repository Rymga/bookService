package com.libreriaSanSebastian.bookService.repository;

import com.libreriaSanSebastian.bookService.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Libro findByTitulo(String titulo);

    @Modifying
    @Transactional
    @Query("UPDATE Libro l SET l.stock = l.stock - 1 WHERE l.id = :id AND l.stock > 0")
    int decrementarStock(Long id);
}