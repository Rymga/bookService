package com.libreriaSanSebastian.bookService.service;

import com.libreriaSanSebastian.bookService.model.Autor;
import com.libreriaSanSebastian.bookService.repository.AutorRepository;
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
class AutorServiceTest {

    @Mock
    private AutorRepository autorRepository;

    @InjectMocks
    private AutorService autorService;

    private Autor autor;

    @BeforeEach
    void setUp() {
        autor = new Autor();
        autor.setId(1L);
        autor.setNombre("Gabriel");
        autor.setApellido("García Márquez");
        autor.setNacionalidad("Colombiana");
    }

    @Test
    void testListarTodos() {
        // Configuración del mock
        when(autorRepository.findAll()).thenReturn(List.of(autor));

        // Llamada al método del servicio
        List<Autor> autores = autorService.listarTodos();

        // Verificaciones
        assertNotNull(autores);
        assertEquals(1, autores.size());
        assertEquals("Gabriel", autores.get(0).getNombre());
        verify(autorRepository, times(1)).findAll();
    }

    @Test
    void testBuscarPorId() {
        // Configuración del mock
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));

        // Llamada al método del servicio
        Optional<Autor> autorEncontrado = autorService.buscarPorId(1L);

        // Verificaciones
        assertTrue(autorEncontrado.isPresent());
        assertEquals("Gabriel", autorEncontrado.get().getNombre());
        assertEquals("García Márquez", autorEncontrado.get().getApellido());
        verify(autorRepository, times(1)).findById(1L);
    }


    @Test
    void testBuscarPorNombre() {
        // Configuración del mock
        when(autorRepository.findByNombre("Gabriel")).thenReturn(autor);

        // Llamada al método del servicio
        Optional<Autor> autorEncontrado = autorService.buscarPorNombre("Gabriel");

        // Verificaciones
        assertTrue(autorEncontrado.isPresent());
        assertEquals("Gabriel", autorEncontrado.get().getNombre());
        verify(autorRepository, times(1)).findByNombre("Gabriel");
    }

    @Test
    void testGuardar() {
        // Configuración del mock
        when(autorRepository.save(autor)).thenReturn(autor);

        // Llamada al método del servicio
        Autor autorGuardado = autorService.guardar(autor);

        // Verificaciones
        assertNotNull(autorGuardado);
        assertEquals("Gabriel", autorGuardado.getNombre());
        verify(autorRepository, times(1)).save(autor);
    }

    @Test
    void testEliminar() {
        // Configuración del mock
        doNothing().when(autorRepository).deleteById(1L);

        // Llamada al método del servicio
        autorService.eliminar(1L);

        // Verificación
        verify(autorRepository, times(1)).deleteById(1L);
    }
}