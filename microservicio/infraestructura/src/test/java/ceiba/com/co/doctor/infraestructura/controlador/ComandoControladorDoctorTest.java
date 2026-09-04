package ceiba.com.co.doctor.infraestructura.controlador;

import ceiba.com.co.ApplicationMock;
import ceiba.com.co.doctor.comando.ComandoActualizarDoctor;
import ceiba.com.co.doctor.comando.ComandoDoctorTestDataBuilder;
import ceiba.com.co.doctor.controlador.ComandoControladorDoctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
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

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComandoControladorDoctor.class)
@ContextConfiguration(classes = ApplicationMock.class)
@Sql(scripts = "/sql/reset-doctores.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ComandoControladorDoctorTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepositorioDoctor repositorioDoctor;

    @Test
    @DisplayName("Debería crear doctor cuando los datos son válidos")
    void deberia_CrearDoctor_Cuando_DatosSonValidos() throws Exception {
        // Arrange
        var comando = ComandoDoctorTestDataBuilder.unComandoDoctorValido()
                .conNumeroDocumento("DOC-004")
                .conTarjetaProfesional("TP-004")
                .conCorreoInstitucional("nuevo@hospital.com")
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/doctores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.valor", is("DOC-004")));

        var doctorGuardado = repositorioDoctor.obtenerPorNumeroDocumento("DOC-004").orElse(null);
        Assertions.assertNotNull(doctorGuardado);
        Assertions.assertEquals("Ana", doctorGuardado.getNombre());
    }

    @Test
    @DisplayName("Debería retornar 409 cuando el correo ya está en uso por otro doctor (creación)")
    void deberia_Retornar409_Cuando_CorreoEstaDuplicado() throws Exception {
        // Arrange
        var comando = ComandoDoctorTestDataBuilder.unComandoDoctorValido()
                .conNumeroDocumento("DOC-999")
                .conTarjetaProfesional("TP-999")
                .conCorreoInstitucional("ana.torres@hospital.com") // Ya en uso por DOC-001
                .build();

        // Act & Assert
        mockMvc.perform(post("/api/doctores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionDuplicidad")));
    }

    @Test
    @DisplayName("Debería actualizar doctor cuando los datos son válidos")
    void deberia_ActualizarDoctor_Cuando_DatosValidos() throws Exception {
        // Arrange
        var comandoUpdate = new ComandoActualizarDoctor("Ana Modificada", "Torres", "CARDIOLOGIA", "ana.mod@hospital.com");

        // Act
        mockMvc.perform(put("/api/doctores/DOC-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comandoUpdate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor", is("DOC-001")));

        // Assert
        var doctor = repositorioDoctor.obtenerPorNumeroDocumento("DOC-001").orElseThrow();
        Assertions.assertEquals("Ana Modificada", doctor.getNombre());
        Assertions.assertEquals("ana.mod@hospital.com", doctor.getCorreoInstitucional());
    }

    @Test
    @DisplayName("Debería retornar 409 al intentar actualizar con correo de otro doctor")
    void deberia_Retornar409_Cuando_ActualizaConCorreoDeOtroDoctor() throws Exception {
        // Arrange
        var comandoUpdate = new ComandoActualizarDoctor("Ana", "Torres", "CARDIOLOGIA", "luis.ramos@hospital.com"); // Correo de DOC-002

        // Act & Assert
        mockMvc.perform(put("/api/doctores/DOC-001")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comandoUpdate)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionDuplicidad")));
    }

    @Test
    @DisplayName("Debería deshabilitar doctor exitosamente")
    void deberia_DeshabilitarDoctor_Cuando_EstaHabilitado() throws Exception {
        // Arrange
        var doctorInicial = repositorioDoctor.obtenerPorNumeroDocumento("DOC-001").orElseThrow();
        Assertions.assertTrue(doctorInicial.isHabilitado());

        // Act
        mockMvc.perform(delete("/api/doctores/DOC-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor", is("DOC-001")));

        // Assert
        var doctorDeshabilitado = repositorioDoctor.obtenerPorNumeroDocumento("DOC-001").orElseThrow();
        Assertions.assertFalse(doctorDeshabilitado.isHabilitado());
    }

    @Test
    @DisplayName("Debería retornar 404 cuando el doctor a deshabilitar no existe")
    void deberia_Retornar404_Cuando_DoctorNoExisteParaDeshabilitar() throws Exception {
        // Act & Assert
        mockMvc.perform(delete("/api/doctores/NOEXISTE"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionSinDatos")));
    }
}
