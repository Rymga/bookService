package com.libreriaSanSebastian.bookService.service;

import com.libreriaSanSebastian.bookService.model.Autor;
import com.libreriaSanSebastian.bookService.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

/**
 * Servicio para la gestión de autores.
 * 
 * Proporciona métodos para listar, buscar, guardar y eliminar autores.
 */
@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    /**
     * Obtiene la lista de todos los autores.
     * 
     * @return Lista de autores.
     */
    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    /**
     * Busca un autor por su identificador.
     * 
     * @param id Identificador del autor.
     * @return Optional con el autor si existe.
     */
    public Optional<Autor> buscarPorId(Long id) {
        return autorRepository.findById(id);
    }

    /**
     * Busca un autor por su nombre.
     * 
     * @param nombre Nombre del autor.
     * @return Optional con el autor si existe.
     */
    public Optional<Autor> buscarPorNombre(String nombre) {
        return Optional.ofNullable(autorRepository.findByNombre(nombre));
    }

    /**
     * Guarda un autor en la base de datos.
     * 
     * @param autor Autor a guardar.
     * @return Autor guardado.
     */
    public Autor guardar(Autor autor) {
        return autorRepository.save(autor);
    }

    /**
     * Elimina un autor por su identificador.
     * 
     * @param id Identificador del autor a eliminar.
     */
    public void eliminar(Long id) {
        autorRepository.deleteById(id);
    }
}