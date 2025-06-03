package com.libreriaSanSebastian.bookService.controller;

import com.libreriaSanSebastian.bookService.model.Libro;
import com.libreriaSanSebastian.bookService.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @GetMapping
    public List<Libro> listarTodos() {
        return libroService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerPorId(@PathVariable Long id) {
        return libroService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<Libro> obtenerPorTitulo(@PathVariable String titulo) {
        return libroService.buscarPorTitulo(titulo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Libro> crear(@RequestBody Libro libro) {
        return ResponseEntity.ok(libroService.guardar(libro));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Libro> actualizar(@PathVariable Long id, @RequestBody Libro libro) {
        return libroService.buscarPorId(id)
                .map(existente -> {
                    libro.setId(id);
                    return ResponseEntity.ok(libroService.guardar(libro));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (libroService.buscarPorId(id).isPresent()) {
            libroService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/decrementar-stock/{id}")
    public ResponseEntity<Void> decrementarStock(@PathVariable Long id) {
        if (libroService.decrementarStock(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
}