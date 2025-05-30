package com.libreriaSanSebastian.bookService.service;

import com.libreriaSanSebastian.bookService.model.Autor;
import com.libreriaSanSebastian.bookService.repository.AutorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class AutorService {

    @Autowired
    private AutorRepository autorRepository;

    public List<Autor> listarTodos() {
        return autorRepository.findAll();
    }

    public Optional<Autor> buscarPorId(Long id) {
        return autorRepository.findById(id);
    }

    public Optional<Autor> buscarPorNombre(String nombre) {
        return Optional.ofNullable(autorRepository.findByNombre(nombre));
    }

    public Autor guardar(Autor autor) {
        return autorRepository.save(autor);
    }

    public void eliminar(Long id) {
        autorRepository.deleteById(id);
    }
}