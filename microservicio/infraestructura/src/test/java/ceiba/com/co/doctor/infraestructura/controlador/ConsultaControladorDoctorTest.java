package ceiba.com.co.doctor.infraestructura.controlador;

import ceiba.com.co.ApplicationMock;
import ceiba.com.co.doctor.controlador.ConsultaControladorDoctor;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ConsultaControladorDoctor.class)
@ContextConfiguration(classes = ApplicationMock.class)
@Sql(scripts = "/sql/reset-doctores.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ConsultaControladorDoctorTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Debería retornar lista de todos los doctores (habilitados y deshabilitados)")
    void deberia_RetornarListaDeDoctores_Cuando_SeConsultaTodos() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/doctores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].numeroDocumento", is("DOC-001")))
                .andExpect(jsonPath("$[2].numeroDocumento", is("DOC-003")));
    }

    @Test
    @DisplayName("Debería retornar el doctor cuando se busca por número de documento válido")
    void deberia_RetornarDoctor_Cuando_SeBuscaPorNumeroDocumento() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/doctores/DOC-001"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.numeroDocumento", is("DOC-001")))
                .andExpect(jsonPath("$.nombre", is("Ana")))
                .andExpect(jsonPath("$.habilitado", is(true)));
    }

    @Test
    @DisplayName("Debería retornar 404 cuando se busca por un número de documento que no existe")
    void deberia_Retornar404_Cuando_DoctorNoExiste() throws Exception {
        // Act & Assert
        mockMvc.perform(get("/api/doctores/NOEXISTE"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionSinDatos")));
    }

    @Test
    @DisplayName("Debería retornar lista de doctores filtrada por especialidad y solo los habilitados")
    void deberia_RetornarDoctoresHabilitados_Cuando_SeBuscaPorEspecialidad() throws Exception {
        // En reset-doctores.sql, DOC-001 es CARDIOLOGIA y true, DOC-003 es CARDIOLOGIA y false
        // Act & Assert
        mockMvc.perform(get("/api/doctores/especialidad/CARDIOLOGIA"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].numeroDocumento", is("DOC-001")));
    }
}
