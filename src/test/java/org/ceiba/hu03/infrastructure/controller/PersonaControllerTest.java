package org.ceiba.hu03.infrastructure.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.ceiba.hu03.domain.constant.AppConstants;
import org.ceiba.hu03.domain.model.Persona;
import org.ceiba.hu03.domain.model.PaginatedResult;
import org.ceiba.hu03.domain.port.in.*;
import org.ceiba.hu03.domain.exception.PersonaNotFoundException;
import org.ceiba.hu03.domain.exception.InvalidSearchCriteriaException;
import org.ceiba.hu03.infrastructure.controller.dto.PersonaDTO;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PersonaControllerTest {

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
    private CrearPersonaUseCase crearPersonaUseCase;

    @MockitoBean
    private BuscarPersonaUseCase buscarPersonaUseCase;

    @MockitoBean
    private ActualizarPersonaUseCase actualizarPersonaUseCase;

    @MockitoBean
    private EliminarPersonaUseCase eliminarPersonaUseCase;

    @MockitoBean
    private BuscarPersonaAvanzadaUseCase buscarPersonaAvanzadaUseCase;

    private Persona personaDominio;
    private PersonaDTO personaDTO;

    @BeforeEach
    void setUp() {
        personaDTO = PersonaDTO.builder()
                .cedula(CEDULA_EXISTENTE)
                .nombre(NOMBRE_EXISTENTE)
                .apellido(APELLIDO_EXISTENTE)
                .email(EMAIL_EXISTENTE)
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();

        personaDominio = Persona.builder()
                .cedula(CEDULA_EXISTENTE)
                .nombre(NOMBRE_EXISTENTE)
                .apellido(APELLIDO_EXISTENTE)
                .email(EMAIL_EXISTENTE)
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();
    }

    @Test
    @DisplayName("POST - Crear persona de forma exitosa")
    void should_ReturnStatusCreatedAndPersonaResponse_When_PersonaIsCreatedSuccessfully() throws Exception {
        Mockito.when(crearPersonaUseCase.crear(any(Persona.class))).thenReturn(personaDominio);

        mockMvc.perform(post("/api/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personaDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensaje").value(AppConstants.PERSONA_CREADA))
                .andExpect(jsonPath("$.persona.cedula").value(CEDULA_EXISTENTE))
                .andExpect(jsonPath("$.persona.nombre").value(NOMBRE_EXISTENTE))
                .andExpect(jsonPath("$.persona.apellido").value(APELLIDO_EXISTENTE))
                .andExpect(jsonPath("$.persona.email").value(EMAIL_EXISTENTE));
    }

    @Test
    @DisplayName("POST - Error 400 cuando los datos de creación son inválidos o vacíos")
    void should_ReturnStatusBadRequest_When_PersonaDataIsInvalidOrEmpty() throws Exception {
        PersonaDTO personaInvalida = PersonaDTO.builder().build();

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

    @Test
    @DisplayName("GET - Buscar persona existente por cédula")
    void should_ReturnStatusOkAndPersonaDto_When_PersonaIsFoundByCedula() throws Exception {
        Mockito.when(buscarPersonaUseCase.buscarPorCedula(CEDULA_EXISTENTE)).thenReturn(personaDominio);

        mockMvc.perform(get("/api/personas/{cedula}", CEDULA_EXISTENTE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cedula").value(CEDULA_EXISTENTE))
                .andExpect(jsonPath("$.nombre").value(NOMBRE_EXISTENTE))
                .andExpect(jsonPath("$.apellido").value(APELLIDO_EXISTENTE))
                .andExpect(jsonPath("$.email").value(EMAIL_EXISTENTE));
    }

    @Test
    @DisplayName("GET - Error 404 cuando la persona buscada por cédula no existe")
    void should_ReturnStatusNotFoundAndErrorResponse_When_PersonaDoesNotExist() throws Exception {
        Mockito.when(buscarPersonaUseCase.buscarPorCedula(CEDULA_NO_EXISTENTE))
                .thenThrow(new PersonaNotFoundException(AppConstants.PERSONA_NO_ENCONTRADA));

        mockMvc.perform(get("/api/personas/{cedula}", CEDULA_NO_EXISTENTE))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("PERSONA_NO_ENCONTRADA"))
                .andExpect(jsonPath("$.message").value(AppConstants.PERSONA_NO_ENCONTRADA));
    }

    @Test
    @DisplayName("PUT - Actualizar persona de forma exitosa")
    void should_ReturnStatusOkAndPersonaResponse_When_PersonaIsUpdatedSuccessfully() throws Exception {
        Mockito.when(actualizarPersonaUseCase.actualizar(eq(CEDULA_EXISTENTE), any(Persona.class)))
                .thenReturn(personaDominio);

        mockMvc.perform(put("/api/personas/{cedula}", CEDULA_EXISTENTE)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personaDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value(AppConstants.PERSONA_ACTUALIZADA))
                .andExpect(jsonPath("$.persona.cedula").value(CEDULA_EXISTENTE))
                .andExpect(jsonPath("$.persona.nombre").value(NOMBRE_EXISTENTE));
    }

    @Test
    @DisplayName("DELETE - Eliminar persona por cédula")
    void should_ReturnStatusOkAndPersonaResponse_When_PersonaIsDeletedSuccessfully() throws Exception {
        Mockito.when(eliminarPersonaUseCase.eliminar(CEDULA_EXISTENTE)).thenReturn(personaDominio);

        mockMvc.perform(delete("/api/personas/{cedula}", CEDULA_EXISTENTE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.mensaje").value(AppConstants.PERSONA_ELIMINADA))
                .andExpect(jsonPath("$.persona.cedula").value(CEDULA_EXISTENTE));
    }

    @Test
    @DisplayName("GET - Buscar personas con criterios avanzados de forma exitosa")
    void should_ReturnStatusOk_When_AdvancedSearchIsCalled() throws Exception {
        PaginatedResult<Persona> paginatedResult = PaginatedResult.<Persona>builder()
                .content(Collections.singletonList(personaDominio))
                .totalElements(1L)
                .totalPages(1)
                .currentPage(0)
                .size(10)
                .build();

        Mockito.when(buscarPersonaAvanzadaUseCase.buscar(
                eq("Valentina"), eq("Vargas"), eq(18), eq(50), eq(0), eq(10), eq("cedula,asc")
        )).thenReturn(paginatedResult);

        mockMvc.perform(get("/api/personas/search")
                .param("nombre", "Valentina")
                .param("apellido", "Vargas")
                .param("edadMinima", "18")
                .param("edadMaxima", "50")
                .param("page", "0")
                .param("size", "10")
                .param("sort", "cedula,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].cedula").value(CEDULA_EXISTENTE))
                .andExpect(jsonPath("$.content[0].nombre").value(NOMBRE_EXISTENTE))
                .andExpect(jsonPath("$.content[0].apellido").value(APELLIDO_EXISTENTE))
                .andExpect(jsonPath("$.totalElements").value(1))
                .andExpect(jsonPath("$.totalPages").value(1))
                .andExpect(jsonPath("$.currentPage").value(0))
                .andExpect(jsonPath("$.size").value(10));
    }

    @Test
    @DisplayName("GET - Error 400 cuando el parámetro de ordenamiento no está permitido")
    void should_ReturnBadRequest_When_SortFieldIsInvalid() throws Exception {
        mockMvc.perform(get("/api/personas/search")
                .param("sort", "hackingField,asc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("ARGUMENTO_INVALIDO"))
                .andExpect(jsonPath("$.message").value("El campo de ordenamiento solicitado no está permitido: hackingField"));
    }

    @Test
    @DisplayName("GET - Error 400 cuando el parámetro de ordenamiento contiene inyección SQL o caracteres especiales")
    void should_ReturnBadRequest_When_SortFieldContainsSqlInjection() throws Exception {
        mockMvc.perform(get("/api/personas/search")
                .param("sort", "cedula; drop table personas;--"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("ARGUMENTO_INVALIDO"))
                .andExpect(jsonPath("$.message").value("El formato del parámetro de ordenamiento es inválido o contiene caracteres sospechosos."));
    }

    @Test
    @DisplayName("GET - Error 400 cuando la edad mínima es mayor que la edad máxima")
    void should_ReturnBadRequest_When_MinAgeIsGreaterThanMaxAge() throws Exception {
        Mockito.when(buscarPersonaAvanzadaUseCase.buscar(
                any(), any(), eq(50), eq(18), anyInt(), anyInt(), any()
        )).thenThrow(new InvalidSearchCriteriaException("La edad mínima no puede ser mayor que la edad máxima: 50 > 18"));

        mockMvc.perform(get("/api/personas/search")
                .param("edadMinima", "50")
                .param("edadMaxima", "18"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("CRITERIO_BUSQUEDA_INVALIDO"))
                .andExpect(jsonPath("$.message").value("La edad mínima no puede ser mayor que la edad máxima: 50 > 18"));
    }

    @Test
    @DisplayName("GET - Error 400 cuando la edad mínima es negativa")
    void should_ReturnBadRequest_When_MinAgeIsNegative() throws Exception {
        Mockito.when(buscarPersonaAvanzadaUseCase.buscar(
                any(), any(), eq(-5), any(), anyInt(), anyInt(), any()
        )).thenThrow(new InvalidSearchCriteriaException("La edad mínima no puede ser negativa: -5"));

        mockMvc.perform(get("/api/personas/search")
                .param("edadMinima", "-5"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("CRITERIO_BUSQUEDA_INVALIDO"))
                .andExpect(jsonPath("$.message").value("La edad mínima no puede ser negativa: -5"));
    }

    @Test
    @DisplayName("POST - Error 400 cuando el nombre contiene caracteres inválidos")
    void should_ReturnBadRequest_When_NombreContainsInvalidCharacters() throws Exception {
        PersonaDTO personaConNombreInvalido = PersonaDTO.builder()
                .cedula(CEDULA_EXISTENTE)
                .nombre("Juan<script>")
                .apellido(APELLIDO_EXISTENTE)
                .email(EMAIL_EXISTENTE)
                .fechaNacimiento(FECHA_NACIMIENTO)
                .build();

        mockMvc.perform(post("/api/personas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personaConNombreInvalido)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.error").value("VALIDACION_FALLIDA"))
                .andExpect(jsonPath("$.fieldErrors.nombre").value(AppConstants.NOMBRE_INVALIDO));
    }
}
