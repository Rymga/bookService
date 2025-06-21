package com.libreriaSanSebastian.bookService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.libreriaSanSebastian.bookService.model.Autor;
import com.libreriaSanSebastian.bookService.service.AutorService;
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

@WebMvcTest(AutorController.class)
@ActiveProfiles("test")
class AutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AutorService autorService;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testListarTodos() throws Exception {
        when(autorService.listarTodos()).thenReturn(List.of(autor));

        mockMvc.perform(get("/api/v1/autores"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].nombre").value("Gabriel"))
                .andExpect(jsonPath("$[0].apellido").value("García Márquez"));

        verify(autorService, times(1)).listarTodos();
    }

    @Test
    void testObtenerPorId() throws Exception {
        when(autorService.buscarPorId(1L)).thenReturn(Optional.of(autor));

        mockMvc.perform(get("/api/v1/autores/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Gabriel"))
                .andExpect(jsonPath("$.apellido").value("García Márquez"));

        verify(autorService, times(1)).buscarPorId(1L);
    }


    @Test
    void testObtenerPorNombre() throws Exception {
        when(autorService.buscarPorNombre("Gabriel")).thenReturn(Optional.of(autor));

        mockMvc.perform(get("/api/v1/autores/nombre/Gabriel"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Gabriel"));

        verify(autorService, times(1)).buscarPorNombre("Gabriel");
    }

    @Test
    void testCrear() throws Exception {
        when(autorService.guardar(any(Autor.class))).thenReturn(autor);

        mockMvc.perform(post("/api/v1/autores")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(autor)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Gabriel"));

        verify(autorService, times(1)).guardar(any(Autor.class));
    }

    @Test
    void testActualizar() throws Exception {
        when(autorService.buscarPorId(1L)).thenReturn(Optional.of(autor));
        when(autorService.guardar(any(Autor.class))).thenReturn(autor);

        mockMvc.perform(put("/api/v1/autores/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(autor)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.nombre").value("Gabriel"));

        verify(autorService, times(1)).buscarPorId(1L);
        verify(autorService, times(1)).guardar(any(Autor.class));
    }

    @Test
    void testEliminar() throws Exception {
        when(autorService.buscarPorId(1L)).thenReturn(Optional.of(autor));
        doNothing().when(autorService).eliminar(1L);

        mockMvc.perform(delete("/api/v1/autores/1"))
                .andExpect(status().isNoContent());

        verify(autorService, times(1)).buscarPorId(1L);
        verify(autorService, times(1)).eliminar(1L);
    }

}