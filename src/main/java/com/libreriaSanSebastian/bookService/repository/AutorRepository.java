package com.libreriaSanSebastian.bookService.repository;

import com.libreriaSanSebastian.bookService.model.Autor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio de Autores
 * 
 * Proporciona métodos para realizar operaciones CRUD y consultas personalizadas sobre la entidad Autor.
 */
@Repository
public interface AutorRepository extends JpaRepository<Autor, Long> {
    /**
     * Busca un autor por su nombre.
     * 
     * @param nombre Nombre del autor.
     * @return Autor encontrado o null si no existe.
     */
    Autor findByNombre(String nombre);
}