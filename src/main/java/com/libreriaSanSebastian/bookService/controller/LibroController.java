package com.libreriaSanSebastian.bookService.controller;

import com.libreriaSanSebastian.bookService.model.Libro;
import com.libreriaSanSebastian.bookService.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
//
import java.util.List;

/**
 * Controlador REST para la gestión de libros.
 * Proporciona endpoints para operaciones CRUD y gestión de stock.
 */
@RestController
@RequestMapping("/api/v1/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    /**
     * Obtiene la lista de todos los libros.
     * @return Lista de libros.
     */
    @GetMapping
    public List<Libro> listarTodos() {
        return libroService.listarTodos();
    }

    /**
     * Obtiene un libro por su ID.
     * @param id ID del libro.
     * @return Libro encontrado o 404 si no existe.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id) {
        return libroService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Obtiene un libro por su título.
     * @param titulo Título del libro.
     * @return Libro encontrado o 404 si no existe.
     */
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<Libro> obtenerPorTitulo(@PathVariable String titulo) {
        return libroService.buscarPorTitulo(titulo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Crea un nuevo libro.
     * @param libro Objeto libro a crear.
     * @return Libro creado.
     */
    @PostMapping
    public ResponseEntity<Libro> crear(@RequestBody Libro libro) {
        return ResponseEntity.ok(libroService.guardar(libro));
    }

    /**
     * Actualiza un libro existente.
     * @param id ID del libro a actualizar.
     * @param libro Datos actualizados del libro.
     * @return Libro actualizado o 404 si no existe.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @RequestBody Libro libro) {
        return libroService.buscarPorId(id)
                .map(existente -> {
                    libro.setId(id);
                    return ResponseEntity.ok(libroService.guardar(libro));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Elimina un libro por su ID.
     * @param id ID del libro a eliminar.
     * @return 204 si se elimina, 404 si no existe.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (libroService.buscarPorId(id).isPresent()) {
            libroService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    /**
     * Decrementa el stock de un libro por su ID.
     * @param id ID del libro.
     * @return 200 si se decrementa, 400 si no es posible.
     */
    @PutMapping("/decrementar-stock/{id}")
    public ResponseEntity<Void> decrementarStock(@PathVariable Long id) {
        if (libroService.decrementarStock(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}