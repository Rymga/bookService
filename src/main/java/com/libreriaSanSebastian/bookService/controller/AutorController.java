package com.libreriaSanSebastian.bookService.controller;

import com.libreriaSanSebastian.bookService.model.Autor;
import com.libreriaSanSebastian.bookService.service.AutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @GetMapping
    public List<Autor> listarTodos() {
        return autorService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Autor> obtenerPorId(@PathVariable Long id) {
        return autorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Autor> obtenerPorNombre(@PathVariable String nombre) {
        return autorService.buscarPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Autor> crear(@RequestBody Autor autor) {
        return ResponseEntity.ok(autorService.guardar(autor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Autor> actualizar(@PathVariable Long id, @RequestBody Autor autor) {
        return autorService.buscarPorId(id)
                .map(existente -> {
                    autor.setId(id);
                    return ResponseEntity.ok(autorService.guardar(autor));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (autorService.buscarPorId(id).isPresent()) {
            autorService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}