package com.libreriaSanSebastian.bookService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libreriaSanSebastian.bookService.model.Autor;
import com.libreriaSanSebastian.bookService.model.Libro;
import com.libreriaSanSebastian.bookService.service.LibroService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(LibroController.class)
@ActiveProfiles("test")
class LibroControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LibroService libroService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testListarTodos() throws Exception {
        when(libroService.listarTodos()).thenReturn(List.of(libro));

        mockMvc.perform(get("/api/v1/libros"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].titulo").value("Cien años de soledad"))
                .andExpect(jsonPath("$[0].isbn").value("978-84-376-0494-7"))
                .andExpect(jsonPath("$[0].stock").value(10))
                .andExpect(jsonPath("$[0].autor.nombre").value("Gabriel"))
                .andExpect(jsonPath("$[0].autor.apellido").value("García Márquez"));

        verify(libroService, times(1)).listarTodos();
    }

    @Test
    void testObtenerPorId() throws Exception {
        when(libroService.buscarPorId(1L)).thenReturn(Optional.of(libro));

        mockMvc.perform(get("/api/v1/libros/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.titulo").value("Cien años de soledad"))
                .andExpect(jsonPath("$.isbn").value("978-84-376-0494-7"))
                .andExpect(jsonPath("$.stock").value(10))
                .andExpect(jsonPath("$.autor.nombre").value("Gabriel"));

        verify(libroService, times(1)).buscarPorId(1L);
    }

    @Test
    void testObtenerPorIdNoEncontrado() throws Exception {
        when(libroService.buscarPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/libros/1"))
                .andExpect(status().isNotFound());

        verify(libroService, times(1)).buscarPorId(1L);
    }

    @Test
    void testObtenerPorTitulo() throws Exception {
        when(libroService.buscarPorTitulo("Cien años de soledad")).thenReturn(Optional.of(libro));

        mockMvc.perform(get("/api/v1/libros/titulo/Cien años de soledad"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.titulo").value("Cien años de soledad"))
                .andExpect(jsonPath("$.autor.nombre").value("Gabriel"));

        verify(libroService, times(1)).buscarPorTitulo("Cien años de soledad");
    }

    @Test
    void testObtenerPorTituloNoEncontrado() throws Exception {
        when(libroService.buscarPorTitulo("Libro inexistente")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/v1/libros/titulo/Libro inexistente"))
                .andExpect(status().isNotFound());

        verify(libroService, times(1)).buscarPorTitulo("Libro inexistente");
    }

    @Test
    void testCrear() throws Exception {
        when(libroService.guardar(any(Libro.class))).thenReturn(libro);

        mockMvc.perform(post("/api/v1/libros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(libro)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.titulo").value("Cien años de soledad"))
                .andExpect(jsonPath("$.isbn").value("978-84-376-0494-7"));

        verify(libroService, times(1)).guardar(any(Libro.class));
    }

    @Test
    void testActualizar() throws Exception {
        when(libroService.buscarPorId(1L)).thenReturn(Optional.of(libro));
        when(libroService.guardar(any(Libro.class))).thenReturn(libro);

        mockMvc.perform(put("/api/v1/libros/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(libro)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.titulo").value("Cien años de soledad"));

        verify(libroService, times(1)).buscarPorId(1L);
        verify(libroService, times(1)).guardar(any(Libro.class));
    }

    @Test
    void testActualizarNoEncontrado() throws Exception {
        when(libroService.buscarPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/v1/libros/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(libro)))
                .andExpect(status().isNotFound());

        verify(libroService, times(1)).buscarPorId(1L);
        verify(libroService, never()).guardar(any(Libro.class));
    }

    @Test
    void testEliminar() throws Exception {
        when(libroService.buscarPorId(1L)).thenReturn(Optional.of(libro));
        doNothing().when(libroService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/libros/1"))
                .andExpect(status().isNoContent());

        verify(libroService, times(1)).buscarPorId(1L);
        verify(libroService, times(1)).eliminar(1L);
    }

    @Test
    void testEliminarNoEncontrado() throws Exception {
        when(libroService.buscarPorId(1L)).thenReturn(Optional.empty());

        mockMvc.perform(delete("/api/v1/libros/1"))
                .andExpect(status().isNotFound());

        verify(libroService, times(1)).buscarPorId(1L);
        verify(libroService, never()).eliminar(anyLong());
    }

    @Test
    void testDecrementarStockExitoso() throws Exception {
        when(libroService.decrementarStock(1L)).thenReturn(true);

        mockMvc.perform(put("/api/v1/libros/decrementar-stock/1"))
                .andExpect(status().isOk());

        verify(libroService, times(1)).decrementarStock(1L);
    }

    @Test
    void testDecrementarStockSinStock() throws Exception {
        when(libroService.decrementarStock(1L)).thenReturn(false);

        mockMvc.perform(put("/api/v1/libros/decrementar-stock/1"))
                .andExpect(status().isBadRequest());

        verify(libroService, times(1)).decrementarStock(1L);
    }
}