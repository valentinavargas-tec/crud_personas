package ceiba.com.co.paciente.infraestructura.controlador;

import ceiba.com.co.ApplicationMock;
import ceiba.com.co.paciente.comando.ComandoPacienteTestDataBuilder;
import ceiba.com.co.paciente.controlador.ComandoControladorPaciente;
import ceiba.com.co.paciente.puerto.repositorio.RepositorioPaciente;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComandoControladorPaciente.class)
@ContextConfiguration(classes = ApplicationMock.class)
@Sql(scripts = "/sql/reset-pacientes.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ComandoControladorPacienteTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepositorioPaciente repositorioPaciente;

    @Test
    void deberia_CrearPaciente_Cuando_DatosSonValidos() throws Exception {
        // Arrange
        var comando = ComandoPacienteTestDataBuilder.unComandoPacienteValido()
                .conNumeroDocumento(111222333L)
                .conNombre("Carlos")
                .conApellido("Perez")
                .conCorreoElectronico("carlos.perez@example.com")
                .conFechaNacimiento(LocalDate.of(1995, 4, 10))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.valor", is(111222333)));

        var pacienteGuardada = repositorioPaciente.obtener(111222333L);
        Assertions.assertNotNull(pacienteGuardada);
        Assertions.assertEquals("Carlos", pacienteGuardada.getNombre());
        Assertions.assertEquals("Perez", pacienteGuardada.getApellido());
    }

    @Test
    void deberia_Retornar400_Cuando_CedulaEstaDuplicada() throws Exception {
        // Arrange
        var comando = ComandoPacienteTestDataBuilder.unComandoPacienteValido()
                .conNumeroDocumento(123456789L)
                .conNombre("Otro")
                .conApellido("Usuario")
                .conCorreoElectronico("otro@example.com")
                .conFechaNacimiento(LocalDate.of(1990, 1, 1))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionDuplicidad")));
    }

    @Test
    void deberia_Retornar400_Cuando_EmailEstaDuplicado() throws Exception {
        // Arrange
        var comando = ComandoPacienteTestDataBuilder.unComandoPacienteValido()
                .conNumeroDocumento(999000111L)
                .conNombre("Nuevo")
                .conApellido("Usuario")
                .conCorreoElectronico("juan.perez@gmail.com")
                .conFechaNacimiento(LocalDate.of(1990, 1, 1))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionDuplicidad")));
    }

    @Test
    void deberia_Retornar400_Cuando_EmailEsInvalido() throws Exception {
        // Arrange
        var comando = ComandoPacienteTestDataBuilder.unComandoPacienteValido()
                .conNumeroDocumento(444555666L)
                .conNombre("Test")
                .conApellido("User")
                .conCorreoElectronico("email-invalido")
                .conFechaNacimiento(LocalDate.of(1990, 1, 1))
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/pacientes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deberia_ActualizarPaciente_Cuando_ExisteYDatosValidos() throws Exception {
        // Arrange
        var comandoUpdate = ComandoPacienteTestDataBuilder.unComandoPacienteValido()
                .conNumeroDocumento(123456789L)
                .conNombre("Juan Actualizado")
                .conApellido("Perez")
                .conCorreoElectronico("juan.perez@gmail.com")
                .conFechaNacimiento(LocalDate.of(1990, 5, 15))
                .build();

        // Act
        mockMvc.perform(put("/api/pacientes/123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comandoUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor", is(123456789)));

        // Assert
        var pacienteActualizada = repositorioPaciente.obtener(123456789L);
        Assertions.assertEquals("Juan Actualizado", pacienteActualizada.getNombre());
    }

    @Test
    void deberia_Retornar404_Cuando_ActualizaPacienteInexistente() throws Exception {
        // Arrange
        var comandoUpdate = ComandoPacienteTestDataBuilder.unComandoPacienteValido()
                .conNumeroDocumento(999999999L)
                .conNombre("Inexistente")
                .conApellido("Paciente")
                .conCorreoElectronico("inexistente@example.com")
                .conFechaNacimiento(LocalDate.of(1990, 1, 1))
                .build();

        // Act & Assert
        mockMvc.perform(put("/api/pacientes/999999999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comandoUpdate)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionSinDatos")));
    }

    @Test
    void deberia_Retornar400_Cuando_ActualizaConEmailDuplicado() throws Exception {
        // Arrange
        var comandoUpdate = ComandoPacienteTestDataBuilder.unComandoPacienteValido()
                .conNumeroDocumento(123456789L)
                .conNombre("Juan")
                .conApellido("Perez")
                .conCorreoElectronico("maria.gomez@example.com")
                .conFechaNacimiento(LocalDate.of(1990, 5, 15))
                .build();

        // Act & Assert
        mockMvc.perform(put("/api/pacientes/123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comandoUpdate)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionDuplicidad")));
    }

    @Test
    @DisplayName("Debería eliminar paciente exitosamente")
    void deberia_EliminarPaciente() throws Exception {
        // Arrange
        Assertions.assertTrue(repositorioPaciente.existeConNumeroDocumento(123456789L));

        // Act & Assert
        mockMvc.perform(delete("/api/pacientes/123456789"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor", is(123456789)))
                .andExpect(jsonPath("$.mensaje", is("Paciente eliminado exitosamente")));

        // Assert persistence
        Assertions.assertFalse(repositorioPaciente.existeConNumeroDocumento(123456789L));
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionSinDatos al intentar eliminar paciente que no existe")
    void deberia_LanzarExcepcion_Cuando_EliminarPacienteNoExistente() throws Exception {
        // Arrange
        Assertions.assertFalse(repositorioPaciente.existeConNumeroDocumento(888888888L));

        // Act & Assert
        mockMvc.perform(delete("/api/pacientes/888888888"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionSinDatos")));
    }
}
