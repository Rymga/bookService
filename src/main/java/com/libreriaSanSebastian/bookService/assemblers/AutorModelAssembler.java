package com.libreriaSanSebastian.bookService.assemblers;

import com.libreriaSanSebastian.bookService.controller.AutorController;
import com.libreriaSanSebastian.bookService.model.Autor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class AutorModelAssembler implements RepresentationModelAssembler<Autor, EntityModel<Autor>> {

    @Override
    public EntityModel<Autor> toModel(Autor autor) {
        EntityModel<Autor> autorModel = EntityModel.of(autor,
                linkTo(methodOn(AutorController.class).obtenerPorId(autor.getId())).withSelfRel(),
                linkTo(methodOn(AutorController.class).listarTodos()).withRel("autores"));

        // Enlace para actualizar autor
        autorModel.add(linkTo(methodOn(AutorController.class).actualizar(autor.getId(), autor)).withRel("actualizar"));

        // Enlace para eliminar autor
        autorModel.add(linkTo(methodOn(AutorController.class).eliminar(autor.getId())).withRel("eliminar"));

        return autorModel;
    }
}
