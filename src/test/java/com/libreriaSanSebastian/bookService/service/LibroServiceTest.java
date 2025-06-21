package com.libreriaSanSebastian.bookService.service;

import com.libreriaSanSebastian.bookService.model.Autor;
import com.libreriaSanSebastian.bookService.model.Libro;
import com.libreriaSanSebastian.bookService.repository.LibroRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class LibroServiceTest {

    @Mock
    private LibroRepository libroRepository;

    @InjectMocks
    private LibroService libroService;

    private Libro libro;
    private Autor autor;

    @BeforeEach
    void setUp() {
        autor = new Autor();
        autor.setId(1L);
        autor.setNombre("Gabriel");
        autor.setApellido("García Márquez");
        autor.setNacionalidad("Colombiana");

        libro = new Libro();
        libro.setId(1L);
        libro.setTitulo("Cien años de soledad");
        libro.setIsbn("978-84-376-0494-7");
        libro.setStock(10);
        libro.setAutor(autor);
    }

    @Test
    void testListarTodos() {
        // Configuración del mock
        when(libroRepository.findAllWithAutor()).thenReturn(List.of(libro));

        // Llamada al método del servicio
        List<Libro> libros = libroService.listarTodos();

        // Verificaciones
        assertNotNull(libros);
        assertEquals(1, libros.size());
        assertEquals("Cien años de soledad", libros.get(0).getTitulo());
        assertEquals("Gabriel", libros.get(0).getAutor().getNombre());
        verify(libroRepository, times(1)).findAllWithAutor();
    }

    @Test
    void testBuscarPorId() {
        // Configuración del mock
        when(libroRepository.findByIdWithAutor(1L)).thenReturn(Optional.of(libro));

        // Llamada al método del servicio
        Optional<Libro> libroEncontrado = libroService.buscarPorId(1L);

        // Verificaciones
        assertTrue(libroEncontrado.isPresent());
        assertEquals("Cien años de soledad", libroEncontrado.get().getTitulo());
        assertEquals(10, libroEncontrado.get().getStock());
        verify(libroRepository, times(1)).findByIdWithAutor(1L);
    }

    @Test
    void testBuscarPorTitulo() {
        // Configuración del mock
        when(libroRepository.findByTituloWithAutor("Cien años de soledad")).thenReturn(libro);

        // Llamada al método del servicio
        Optional<Libro> libroEncontrado = libroService.buscarPorTitulo("Cien años de soledad");

        // Verificaciones
        assertTrue(libroEncontrado.isPresent());
        assertEquals("Cien años de soledad", libroEncontrado.get().getTitulo());
        verify(libroRepository, times(1)).findByTituloWithAutor("Cien años de soledad");
    }

    @Test
    void testGuardar() {
        // Configuración del mock
        when(libroRepository.save(libro)).thenReturn(libro);

        // Llamada al método del servicio
        Libro libroGuardado = libroService.guardar(libro);

        // Verificaciones
        assertNotNull(libroGuardado);
        assertEquals("Cien años de soledad", libroGuardado.getTitulo());
        verify(libroRepository, times(1)).save(libro);
    }

    @Test
    void testEliminar() {
        // Configuración del mock
        doNothing().when(libroRepository).deleteById(1L);

        // Llamada al método del servicio
        libroService.eliminar(1L);

        // Verificación
        verify(libroRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDecrementarStockExitoso() {
        // Configuración del mock - simula que se afectó 1 fila
        when(libroRepository.decrementarStock(1L)).thenReturn(1);

        // Llamada al método del servicio
        boolean resultado = libroService.decrementarStock(1L);

        // Verificaciones
        assertTrue(resultado);
        verify(libroRepository, times(1)).decrementarStock(1L);
    }

    @Test
    void testDecrementarStockSinStock() {
        // Configuración del mock - simula que no se afectó ninguna fila (sin stock)
        when(libroRepository.decrementarStock(1L)).thenReturn(0);

        // Llamada al método del servicio
        boolean resultado = libroService.decrementarStock(1L);

        // Verificaciones
        assertFalse(resultado);
        verify(libroRepository, times(1)).decrementarStock(1L);
    }
}