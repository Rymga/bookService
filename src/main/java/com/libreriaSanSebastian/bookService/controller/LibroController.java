package com.libreriaSanSebastian.bookService.controller;

import com.libreriaSanSebastian.bookService.model.Libro;
import com.libreriaSanSebastian.bookService.service.LibroService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/libros")
@Tag(name = "Libros", description = "Operaciones relacionadas con la gestión de libros")
public class LibroController {

    @Autowired
    private LibroService libroService;

    @Operation(
        summary = "Listar todos los libros",
        description = "Obtiene una lista completa de todos los libros registrados en el sistema"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de libros obtenida exitosamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class))
    )
    @GetMapping
    public List<Libro> listarTodos() {
        return libroService.listarTodos();
    }

    @Operation(
        summary = "Obtener libro por ID",
        description = "Busca y retorna un libro específico por su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Libro encontrado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Libro no encontrado",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Libro> obtenerPorId(
            @Parameter(description = "ID único del libro", required = true, example = "1")
            @PathVariable Long id) {
        return libroService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Obtener libro por título",
        description = "Busca y retorna un libro específico por su título exacto"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Libro encontrado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Libro no encontrado",
            content = @Content
        )
    })
    @GetMapping("/titulo/{titulo}")
    public ResponseEntity<Libro> obtenerPorTitulo(
            @Parameter(description = "Título del libro", required = true, example = "Cien años de soledad")
            @PathVariable String titulo) {
        return libroService.buscarPorTitulo(titulo)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nuevo libro",
        description = "Registra un nuevo libro en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Libro creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping
    public ResponseEntity<?> crear(
            @Parameter(description = "Datos del libro a crear", required = true)
            @RequestBody Libro libro) {
        try {
            if (libro.getTitulo() == null || libro.getTitulo().isEmpty() ||
                libro.getIsbn() == null || libro.getIsbn().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El título y ISBN son campos requeridos"));
            }
            
            Libro libroCreado = libroService.guardar(libro);
            return ResponseEntity.status(HttpStatus.CREATED).body(libroCreado);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
        summary = "Actualizar libro",
        description = "Actualiza los datos de un libro existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Libro actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Libro.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Libro no encontrado",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID único del libro", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del libro", required = true)
            @RequestBody Libro libro) {
        try {
            if (libro.getTitulo() == null || libro.getTitulo().isEmpty() ||
                libro.getIsbn() == null || libro.getIsbn().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El título y ISBN son campos requeridos"));
            }
            
            return libroService.buscarPorId(id)
                    .map(existente -> {
                        libro.setId(id);
                        Libro actualizado = libroService.guardar(libro);
                        return ResponseEntity.ok(actualizado);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
        summary = "Eliminar libro",
        description = "Elimina permanentemente un libro del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Libro eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Libro no encontrado"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID único del libro", required = true, example = "1")
            @PathVariable Long id) {
        if (libroService.buscarPorId(id).isPresent()) {
            libroService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @Operation(
        summary = "Decrementar stock de libro",
        description = "Reduce en 1 el stock disponible de un libro"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Stock decrementado exitosamente"
        ),
        @ApiResponse(
            responseCode = "400",
            description = "No hay stock disponible para decrementar"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Libro no encontrado"
        )
    })
    @PutMapping("/decrementar-stock/{id}")
    public ResponseEntity<?> decrementarStock(
            @Parameter(description = "ID único del libro", required = true, example = "1")
            @PathVariable Long id) {
        if (!libroService.buscarPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        
        if (libroService.decrementarStock(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest()
                .body(Map.of("error", "No hay stock disponible para decrementar"));
    }
}