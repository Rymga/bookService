package com.libreriaSanSebastian.bookService.service;
import com.libreriaSanSebastian.bookService.model.Libro;
import com.libreriaSanSebastian.bookService.repository.LibroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class LibroService {

    @Autowired
    private LibroRepository libroRepository;

    public List<Libro> listarTodos() {
        return libroRepository.findAllWithAutor();
    }

    public Optional<Libro> buscarPorId(Long id) {
        return libroRepository.findByIdWithAutor(id);
    }

    public Optional<Libro> buscarPorTitulo(String titulo) {
        return Optional.ofNullable(libroRepository.findByTituloWithAutor(titulo));
    }

    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }

    public void eliminar(Long id) {
        libroRepository.deleteById(id);
    }

    public boolean decrementarStock(Long id) {
        return libroRepository.decrementarStock(id) > 0;
    }
}
