package com.libreriaSanSebastian.bookService.controller;

import com.libreriaSanSebastian.bookService.model.Autor;
import com.libreriaSanSebastian.bookService.service.AutorService;
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
@RequestMapping("/api/v1/autores")
@Tag(name = "Autores", description = "Operaciones relacionadas con la gestión de autores de libros")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @Operation(
        summary = "Listar todos los autores",
        description = "Obtiene una lista completa de todos los autores registrados en el sistema"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Lista de autores obtenida exitosamente",
        content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class))
    )
    @GetMapping
    public List<Autor> listarTodos() {
        return autorService.listarTodos();
    }

    @Operation(
        summary = "Obtener autor por ID",
        description = "Busca y retorna un autor específico por su identificador único"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autor encontrado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Autor no encontrado",
            content = @Content
        )
    })
    @GetMapping("/{id}")
    public ResponseEntity<Autor> obtenerPorId(
            @Parameter(description = "ID único del autor", required = true, example = "1")
            @PathVariable Long id) {
        return autorService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Obtener autor por nombre",
        description = "Busca y retorna un autor específico por su nombre"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autor encontrado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class))
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Autor no encontrado",
            content = @Content
        )
    })
    @GetMapping("/nombre/{nombre}")
    public ResponseEntity<Autor> obtenerPorNombre(
            @Parameter(description = "Nombre del autor", required = true, example = "Gabriel García Márquez")
            @PathVariable String nombre) {
        return autorService.buscarPorNombre(nombre)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(
        summary = "Crear nuevo autor",
        description = "Registra un nuevo autor en el sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "201",
            description = "Autor creado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(mediaType = "application/json")
        )
    })
    @PostMapping
    public ResponseEntity<?> crear(
            @Parameter(description = "Datos del autor a crear", required = true)
            @RequestBody Autor autor) {
        try {
            if (autor.getNombre() == null || autor.getNombre().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El nombre del autor es requerido"));
            }
            
            Autor autorCreado = autorService.guardar(autor);
            return ResponseEntity.status(HttpStatus.CREATED).body(autorCreado);
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
        summary = "Actualizar autor",
        description = "Actualiza los datos de un autor existente"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Autor actualizado exitosamente",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class))
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(mediaType = "application/json")
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Autor no encontrado",
            content = @Content
        )
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID único del autor", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del autor", required = true)
            @RequestBody Autor autor) {
        try {
            if (autor.getNombre() == null || autor.getNombre().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(Map.of("error", "El nombre del autor es requerido"));
            }
            
            return autorService.buscarPorId(id)
                    .map(existente -> {
                        autor.setId(id);
                        Autor actualizado = autorService.guardar(autor);
                        return ResponseEntity.ok(actualizado);
                    })
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @Operation(
        summary = "Eliminar autor",
        description = "Elimina permanentemente un autor del sistema"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "204",
            description = "Autor eliminado exitosamente"
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Autor no encontrado"
        )
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(
            @Parameter(description = "ID único del autor", required = true, example = "1")
            @PathVariable Long id) {
        if (autorService.buscarPorId(id).isPresent()) {
            autorService.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}