package com.libreriaSanSebastian.bookService.controller;

import com.libreriaSanSebastian.bookService.assemblers.AutorModelAssembler;
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
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@RestController
@RequestMapping("/api/v1/autores")
@Tag(name = "Autores", description = "Operaciones relacionadas con la gestión de autores de libros")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @Autowired
    private AutorModelAssembler assembler;

    @Operation(summary = "Listar todos los autores",
               description = "Obtiene una lista completa de todos los autores registrados en el sistema")
    @ApiResponse(responseCode = "200", description = "Lista de autores obtenida exitosamente",
                 content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class)))
    @GetMapping
    public CollectionModel<EntityModel<Autor>> listarTodos() {
        List<EntityModel<Autor>> autores = autorService.listarTodos().stream()
                .map(assembler::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(autores,
                linkTo(methodOn(AutorController.class).listarTodos()).withSelfRel());
    }

    @Operation(summary = "Obtener autor por ID",
               description = "Busca y retorna un autor específico por su identificador único")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autor encontrado exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class))),
        @ApiResponse(responseCode = "404", description = "Autor no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<EntityModel<Autor>> obtenerPorId(
            @Parameter(description = "ID único del autor", required = true, example = "1")
            @PathVariable Long id) {
        return autorService.buscarPorId(id)
                .map(assembler::toModel)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Crear nuevo autor",
               description = "Registra un nuevo autor en el sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Autor creado exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                     content = @Content(mediaType = "application/json"))
    })
    @PostMapping
    public ResponseEntity<?> crear(
            @Parameter(description = "Datos del autor a crear", required = true)
            @RequestBody Autor autor) {
        if (autor.getNombre() == null || autor.getNombre().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El nombre del autor es requerido"));
        }
        Autor autorCreado = autorService.guardar(autor);
        EntityModel<Autor> autorModel = assembler.toModel(autorCreado);
        return ResponseEntity
                .created(linkTo(methodOn(AutorController.class).obtenerPorId(autorCreado.getId())).toUri())
                .body(autorModel);
    }

    @Operation(summary = "Actualizar autor",
               description = "Actualiza los datos de un autor existente")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Autor actualizado exitosamente",
                     content = @Content(mediaType = "application/json", schema = @Schema(implementation = Autor.class))),
        @ApiResponse(responseCode = "400", description = "Datos de entrada inválidos",
                     content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "404", description = "Autor no encontrado", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<?> actualizar(
            @Parameter(description = "ID único del autor", required = true, example = "1")
            @PathVariable Long id,
            @Parameter(description = "Datos actualizados del autor", required = true)
            @RequestBody Autor autor) {
        if (autor.getNombre() == null || autor.getNombre().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El nombre del autor es requerido"));
        }
        return autorService.buscarPorId(id)
                .map(existente -> {
                    autor.setId(id);
                    Autor actualizado = autorService.guardar(autor);
                    return ResponseEntity.ok(assembler.toModel(actualizado));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar autor",
               description = "Elimina permanentemente un autor del sistema")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "Autor eliminado exitosamente"),
        @ApiResponse(responseCode = "404", description = "Autor no encontrado")
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
