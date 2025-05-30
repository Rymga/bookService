package com.libreriaSanSebastian.bookService.repository;

import com.libreriaSanSebastian.bookService.model.Libro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepository extends JpaRepository<Libro, Long> {
    Libro findByTitulo(String titulo);

}