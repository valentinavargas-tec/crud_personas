package org.ceiba.hu03.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ceiba.hu03.domain.Persona;
import org.ceiba.hu03.dto.PersonaDTO;
import org.ceiba.hu03.dto.PersonaResponse;
import org.ceiba.hu03.exception.PersonaNotFoundException;
import org.ceiba.hu03.service.PersonaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;


 // Valida los códigos de respuesta HTTP y la correcta integración de validaciones.

@SpringBootTest
@AutoConfigureMockMvc
public class PersonaControllerTest {

    // Constantes de prueba para evitar "valores mágicos" (Clean Code)
    private static final Long CEDULA_EXISTENTE = 123456L;
    private static final Long CEDULA_NO_EXISTENTE = 999999L;
    private static final String NOMBRE_EXISTENTE = "Valentina";
    private static final String APELLIDO_EXISTENTE = "Vargas";
    private static final String EMAIL_EXISTENTE = "valentina@mail.com";
    private static final LocalDate FECHA_NACIMIENTO = LocalDate.of(2000, 1, 1);

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PersonaService personaService;

    private Persona personaDominio;
    private PersonaDTO personaDTO;
    private PersonaResponse personaResponse;

    @BeforeEach
    void setUp() {
        // Estructuras de prueba basadas en DTOs y Entidades reales
        personaDTO = PersonaDTO.builder()
                .cedula(CEDULA_EXISTENTE)
                .nombre(NOMBRE_EXISTENTE)
                .apellido(APELLIDO_EXISTENTE)
                .email(EMAIL_EXISTENTE)
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();

        personaResponse = PersonaResponse.builder()
                .mensaje("Operación exitosa")
                .persona(personaDTO)
                .build();

        personaDominio = new Persona(CEDULA_EXISTENTE, NOMBRE_EXISTENTE, APELLIDO_EXISTENTE, EMAIL_EXISTENTE, FECHA_NACIMIENTO);
    }

    // Tests para crear persona exitosamente (POST)
    @Test
    @DisplayName("POST - Crear persona de forma exitosa")
    void should_ReturnStatusCreatedAndPersonaResponse_When_PersonaIsCreatedSuccessfully() throws Exception {
        Mockito.when(personaService.crear(any(Persona.class))).thenReturn(personaResponse);

        mockMvc.perform(post("/api/personas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personaDominio)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value("Operación exitosa"))
                .andExpect(jsonPath("$.persona.cedula").value(CEDULA_EXISTENTE))
                .andExpect(jsonPath("$.persona.nombre").value(NOMBRE_EXISTENTE))
                .andExpect(jsonPath("$.persona.apellido").value(APELLIDO_EXISTENTE))
                .andExpect(jsonPath("$.persona.email").value(EMAIL_EXISTENTE));
    }

    // Tests para crear persona con datos inválidos (casos de error 400 Bad Request)
    @Test
    @DisplayName("POST - Error 400 cuando los datos de creación son inválidos o vacíos")
    void should_ReturnStatusBadRequest_When_PersonaDataIsInvalidOrEmpty() throws Exception {
        Persona personaInvalida = new Persona();

        mockMvc.perform(post("/api/personas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personaInvalida)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("VALIDACION_FALLIDA"))
                .andExpect(jsonPath("$.fieldErrors.cedula").exists())
                .andExpect(jsonPath("$.fieldErrors.nombre").exists())
                .andExpect(jsonPath("$.fieldErrors.apellido").exists())
                .andExpect(jsonPath("$.fieldErrors.email").exists());
    }

    // Tests para buscar persona existente (GET)
    @Test
    @DisplayName("GET - Buscar persona existente por cédula")
    void should_ReturnStatusOkAndPersonaDto_When_PersonaIsFoundByCedula() throws Exception {
        Mockito.when(personaService.buscarPorCedulaDTO(CEDULA_EXISTENTE)).thenReturn(personaDTO);

        mockMvc.perform(get("/api/personas/{cedula}", CEDULA_EXISTENTE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cedula").value(CEDULA_EXISTENTE))
                .andExpect(jsonPath("$.nombre").value(NOMBRE_EXISTENTE))
                .andExpect(jsonPath("$.apellido").value(APELLIDO_EXISTENTE))
                .andExpect(jsonPath("$.email").value(EMAIL_EXISTENTE));
    }

    // Tests para buscar persona no existente (Error)
    @Test
    @DisplayName("GET - Error 404 cuando la persona buscada por cédula no existe")
    void should_ReturnStatusNotFoundAndErrorResponse_When_PersonaDoesNotExist() throws Exception {
        Mockito.when(personaService.buscarPorCedulaDTO(CEDULA_NO_EXISTENTE))
                .thenThrow(new PersonaNotFoundException("Persona no encontrada con cédula: " + CEDULA_NO_EXISTENTE));

        mockMvc.perform(get("/api/personas/{cedula}", CEDULA_NO_EXISTENTE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("PERSONA_NO_ENCONTRADA"))
                .andExpect(jsonPath("$.message").value("Persona no encontrada con cédula: " + CEDULA_NO_EXISTENTE));
    }

    // Tests para listar personas
    @Test
    @DisplayName("GET - Listar todas las personas exitosamente")
    void should_ReturnStatusOkAndPersonaDtoList_When_PersonasAreListedSuccessfully() throws Exception {
        List<PersonaDTO> lista = Collections.singletonList(personaDTO);
        Mockito.when(personaService.listar()).thenReturn(lista);

        mockMvc.perform(get("/api/personas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].cedula").value(CEDULA_EXISTENTE))
                .andExpect(jsonPath("$[0].nombre").value(NOMBRE_EXISTENTE))
                .andExpect(jsonPath("$[0].apellido").value(APELLIDO_EXISTENTE))
                .andExpect(jsonPath("$[0].email").value(EMAIL_EXISTENTE));
    }

    // Tests para actualizar persona exitosamente (PUT)
    @Test
    @DisplayName("PUT - Actualizar persona de forma exitosa")
    void should_ReturnStatusOkAndPersonaResponse_When_PersonaIsUpdatedSuccessfully() throws Exception {
        Mockito.when(personaService.actualizar(eq(CEDULA_EXISTENTE), any(Persona.class))).thenReturn(personaResponse);

        mockMvc.perform(put("/api/personas/{cedula}", CEDULA_EXISTENTE)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(personaDominio)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Operación exitosa"))
                .andExpect(jsonPath("$.persona.cedula").value(CEDULA_EXISTENTE))
                .andExpect(jsonPath("$.persona.nombre").value(NOMBRE_EXISTENTE));
    }

    // Tests para eliminar persona (DELETE)
    @Test
    @DisplayName("DELETE - Eliminar persona por cédula")
    void should_ReturnStatusOkAndPersonaResponse_When_PersonaIsDeletedSuccessfully() throws Exception {
        Mockito.when(personaService.eliminar(CEDULA_EXISTENTE)).thenReturn(personaResponse);

        mockMvc.perform(delete("/api/personas/{cedula}", CEDULA_EXISTENTE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value("Operación exitosa"))
                .andExpect(jsonPath("$.persona.cedula").value(CEDULA_EXISTENTE));
    }
}
