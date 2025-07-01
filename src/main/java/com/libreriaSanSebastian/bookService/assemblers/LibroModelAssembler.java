package com.libreriaSanSebastian.bookService.assemblers;

import com.libreriaSanSebastian.bookService.controller.LibroController;
import com.libreriaSanSebastian.bookService.model.Libro;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class LibroModelAssembler implements RepresentationModelAssembler<Libro, EntityModel<Libro>> {

    @Override
    public EntityModel<Libro> toModel(Libro libro) {
        EntityModel<Libro> libroModel = EntityModel.of(libro,
                linkTo(methodOn(LibroController.class).obtenerPorId(libro.getId())).withSelfRel(),
                linkTo(methodOn(LibroController.class).listarTodos()).withRel("libros"));

        // Enlace para actualizar el libro
        libroModel.add(linkTo(methodOn(LibroController.class).actualizar(libro.getId(), libro)).withRel("actualizar"));

        // Enlace para eliminar el libro
        libroModel.add(linkTo(methodOn(LibroController.class).eliminar(libro.getId())).withRel("eliminar"));

        return libroModel;
    }
}
