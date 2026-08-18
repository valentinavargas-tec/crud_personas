package ceiba.com.co.infraestructura.controlador;

import ceiba.com.co.ApplicationMock;
import ceiba.com.co.comando.ComandoPersonaTestDataBuilder;
import ceiba.com.co.controlador.ComandoControladorPersona;
import ceiba.com.co.puerto.repositorio.RepositorioPersona;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ComandoControladorPersona.class)
@ContextConfiguration(classes = ApplicationMock.class)
@Sql(scripts = "/sql/reset-personas.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ComandoControladorPersonaTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepositorioPersona repositorioPersona;

    @Test
    void deberia_CrearPersona_Cuando_DatosSonValidos() throws Exception {
        // Arrange
        var comando = ComandoPersonaTestDataBuilder.unComandoPersonaValido()
                .conCedula(111222333L)
                .conNombre("Carlos")
                .conApellido("Perez")
                .conEmail("carlos.perez@example.com")
                .conFechaNacimiento(LocalDate.of(1995, 4, 10))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/personas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.valor", is(111222333)));

        var personaGuardada = repositorioPersona.obtener(111222333L);
        Assertions.assertNotNull(personaGuardada);
        Assertions.assertEquals("Carlos", personaGuardada.getNombre());
        Assertions.assertEquals("Perez", personaGuardada.getApellido());
    }

    @Test
    void deberia_Retornar400_Cuando_CedulaEstaDuplicada() throws Exception {
        // Arrange
        var comando = ComandoPersonaTestDataBuilder.unComandoPersonaValido()
                .conCedula(123456789L)
                .conNombre("Otro")
                .conApellido("Usuario")
                .conEmail("otro@example.com")
                .conFechaNacimiento(LocalDate.of(1990, 1, 1))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/personas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionDuplicidad")));
    }

    @Test
    void deberia_Retornar400_Cuando_EmailEstaDuplicado() throws Exception {
        // Arrange
        var comando = ComandoPersonaTestDataBuilder.unComandoPersonaValido()
                .conCedula(999000111L)
                .conNombre("Nuevo")
                .conApellido("Usuario")
                .conEmail("juan.perez@gmail.com")
                .conFechaNacimiento(LocalDate.of(1990, 1, 1))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/personas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionDuplicidad")));
    }

    @Test
    void deberia_Retornar400_Cuando_EmailEsInvalido() throws Exception {
        // Arrange
        var comando = ComandoPersonaTestDataBuilder.unComandoPersonaValido()
                .conCedula(444555666L)
                .conNombre("Test")
                .conApellido("User")
                .conEmail("email-invalido")
                .conFechaNacimiento(LocalDate.of(1990, 1, 1))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/personas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberia_ActualizarPersona_Cuando_ExisteYDatosValidos() throws Exception {
        // Arrange
        var comandoUpdate = ComandoPersonaTestDataBuilder.unComandoPersonaValido()
                .conCedula(123456789L)
                .conNombre("Juan Actualizado")
                .conApellido("Perez")
                .conEmail("juan.perez@gmail.com")
                .conFechaNacimiento(LocalDate.of(1990, 5, 15))
                .build();

        // Act
        mockMvc.perform(put("/api/personas/123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comandoUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor", is(123456789)));

        // Assert
        var personaActualizada = repositorioPersona.obtener(123456789L);
        Assertions.assertEquals("Juan Actualizado", personaActualizada.getNombre());
    }

    @Test
    void deberia_Retornar404_Cuando_ActualizaPersonaInexistente() throws Exception {
        // Arrange
        var comandoUpdate = ComandoPersonaTestDataBuilder.unComandoPersonaValido()
                .conCedula(999999999L)
                .conNombre("Inexistente")
                .conApellido("Persona")
                .conEmail("inexistente@example.com")
                .conFechaNacimiento(LocalDate.of(1990, 1, 1))
                .build();

        // Act & Assert
        mockMvc.perform(put("/api/personas/999999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comandoUpdate)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionSinDatos")));
    }

    @Test
    void deberia_Retornar400_Cuando_ActualizaConEmailDuplicado() throws Exception {
        // Arrange
        var comandoUpdate = ComandoPersonaTestDataBuilder.unComandoPersonaValido()
                .conCedula(123456789L)
                .conNombre("Juan")
                .conApellido("Perez")
                .conEmail("maria.gomez@example.com")
                .conFechaNacimiento(LocalDate.of(1990, 5, 15))
                .build();

        // Act & Assert
        mockMvc.perform(put("/api/personas/123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comandoUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionDuplicidad")));
    }

    @Test
    void deberia_EliminarPersona_Cuando_PersonaExiste() throws Exception {
        // Arrange
        Assertions.assertTrue(repositorioPersona.existeConCedula(123456789L));

        // Act & Assert
        mockMvc.perform(delete("/api/personas/123456789"))
                .andExpect(status().isNoContent());

        // Assert persistence
        Assertions.assertFalse(repositorioPersona.existeConCedula(123456789L));
    }

    @Test
    void deberia_Retornar404_Cuando_EliminaPersonaInexistente() throws Exception {
        // Arrange
        Assertions.assertFalse(repositorioPersona.existeConCedula(888888888L));

        // Act & Assert
        mockMvc.perform(delete("/api/personas/888888888"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionSinDatos")));
    }
}
