package com.libreriaSanSebastian.bookService.service;

import com.libreriaSanSebastian.bookService.model.Libro;
import com.libreriaSanSebastian.bookService.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de libros.
 * 
 * Proporciona métodos para listar, buscar, guardar, eliminar y actualizar el stock de libros.
 */
@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    /**
     * Obtiene la lista de todos los libros junto con sus autores.
     * 
     * @return Lista de libros con información de autor.
     */
    public List<Libro> listarTodos() {
        return libroRepository.findAllWithAutor();
    }

    /**
     * Busca un libro por su identificador, incluyendo la información del autor.
     * 
     * @param id Identificador del libro.
     * @return Optional con el libro y su autor si existe.
     */
    public Optional<Libro> buscarPorId(Long id) {
        return libroRepository.findByIdWithAutor(id);
    }

    /**
     * Busca un libro por su título, incluyendo la información del autor.
     * 
     * @param titulo Título del libro.
     * @return Optional con el libro y su autor si existe.
     */
    public Optional<Libro> buscarPorTitulo(String titulo) {
        return Optional.ofNullable(libroRepository.findByTituloWithAutor(titulo));
    }

    /**
     * Guarda un libro en la base de datos.
     * 
     * @param libro Libro a guardar.
     * @return Libro guardado.
     */
    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }

    /**
     * Elimina un libro por su identificador.
     * 
     * @param id Identificador del libro a eliminar.
     */
    public void eliminar(Long id) {
        libroRepository.deleteById(id);
    }

    /**
     * Disminuye el stock de un libro en 1 si hay stock disponible.
     * 
     * @param id Identificador del libro.
     * @return true si se decrementó el stock, false en caso contrario.
     */
    public boolean decrementarStock(Long id) {
        return libroRepository.decrementarStock(id) > 0;
    }
}