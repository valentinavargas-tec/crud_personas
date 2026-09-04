package ceiba.com.co.cita.infraestructura.controlador;

import ceiba.com.co.ApplicationMock;
import ceiba.com.co.cita.comando.ComandoActualizarCita;
import ceiba.com.co.cita.comando.ComandoCancelarCita;
import ceiba.com.co.cita.comando.ComandoReasignarCita;
import ceiba.com.co.cita.controlador.ComandoControladorCita;
import ceiba.com.co.cita.puerto.repositorio.RepositorioCita;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComandoControladorCita.class)
@ContextConfiguration(classes = ApplicationMock.class)
@Sql(scripts = "/sql/reset-citas.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ComandoControladorCitaTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RepositorioCita repositorioCita;

    @Test
    @DisplayName("Debería retornar 200 OK y DTO al reprogramar cita exitosamente")
    void deberiaReprogramarCita_CuandoDatosSonValidos() throws Exception {
        LocalDateTime nuevaFecha = LocalDateTime.of(2028, 10, 12, 11, 0, 0);
        ComandoActualizarCita comando = new ComandoActualizarCita(nuevaFecha, "TELEMEDICINA", "Reprogramación por trabajo");

        mockMvc.perform(put("/api/citas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor.id", is(1)))
                .andExpect(jsonPath("$.valor.tipoCita", is("TELEMEDICINA")))
                .andExpect(jsonPath("$.valor.motivo", is("Reprogramación por trabajo")))
                .andExpect(jsonPath("$.mensaje", is("Cita médica reprogramada exitosamente")));
    }

    @Test
    @DisplayName("Debería retornar 404 Not Found cuando la cita no existe")
    void deberiaRetornar404_CuandoCitaNoExiste() throws Exception {
        LocalDateTime nuevaFecha = LocalDateTime.of(2028, 10, 12, 11, 0, 0);
        ComandoActualizarCita comando = new ComandoActualizarCita(nuevaFecha, "ESPECIALIZADA", "Nueva cita");

        mockMvc.perform(put("/api/citas/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Debería retornar 400 Bad Request cuando la cita no está en estado PROGRAMADA")
    void deberiaRetornar400_CuandoCitaNoEstaProgramada() throws Exception {
        LocalDateTime nuevaFecha = LocalDateTime.of(2028, 10, 16, 10, 0, 0);
        ComandoActualizarCita comando = new ComandoActualizarCita(nuevaFecha, "CONSULTA_GENERAL", "Intento en cancelada");

        // Cita con id 2 está CANCELADA en reset-citas.sql
        mockMvc.perform(put("/api/citas/2")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Regla 6 — Debería retornar 400 Bad Request cuando la fecha es pasada")
    void deberiaRetornar400_CuandoFechaEsPasada() throws Exception {
        LocalDateTime fechaPasada = LocalDateTime.of(2020, 1, 1, 10, 0, 0);
        ComandoActualizarCita comando = new ComandoActualizarCita(fechaPasada, "CONSULTA_GENERAL", "Fecha pasada");

        mockMvc.perform(put("/api/citas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("Regla 10 — Debería retornar 409 Conflict cuando hay cruce de horario con otra cita")
    void deberiaRetornar409_CuandoHayCruceDeHorarioConOtraCita() throws Exception {
        // En reset-citas.sql, Cita 3 tiene fecha '2028-10-20 16:00:00' para paciente 123456789
        LocalDateTime fechaCruce = LocalDateTime.of(2028, 10, 20, 16, 0, 0);
        ComandoActualizarCita comando = new ComandoActualizarCita(fechaCruce, "CONSULTA_GENERAL", "Intento cruce");

        mockMvc.perform(put("/api/citas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("Regla 10 — Debería retornar 200 OK cuando se mantiene la misma hora de la propia cita (auto-exclusión)")
    void deberiaRetornar200_CuandoSeMantieneMismaHoraDeLaCita() throws Exception {
        // Cita 1 ya está en '2028-10-10 10:00:00'. Reprogramar a esa misma hora solo cambiando tipoCita no debe dar 409
        LocalDateTime mismaHora = LocalDateTime.of(2028, 10, 10, 10, 0, 0);
        ComandoActualizarCita comando = new ComandoActualizarCita(mismaHora, "CONTROL", "Misma hora cambio a control");

        mockMvc.perform(put("/api/citas/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor.tipoCita", is("CONTROL")));
    }

    // --- PRUEBAS HU-08: CANCELACIÓN DE CITAS MÉDICAS ---

    @Test
    @DisplayName("HU-08: Debería retornar 200 OK al cancelar cita exitosamente")
    void deberiaCancelarCita_CuandoExisteYEstaProgramada() throws Exception {
        ComandoCancelarCita comando = new ComandoCancelarCita("Calamidad personal");

        mockMvc.perform(patch("/api/citas/1/cancelar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor.id", is(1)))
                .andExpect(jsonPath("$.valor.estado", is("CANCELADA")))
                .andExpect(jsonPath("$.mensaje", is("Cita médica cancelada exitosamente")));
    }

    @Test
    @DisplayName("HU-08: Debería retornar 404 Not Found al cancelar cita inexistente")
    void deberiaRetornar404_AlCancelarCitaInexistente() throws Exception {
        mockMvc.perform(patch("/api/citas/999/cancelar"))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("HU-08 (Regla 7): Debería retornar 400 Bad Request al cancelar cita que no está PROGRAMADA")
    void deberiaRetornar400_AlCancelarCitaYaCancelada() throws Exception {
        // Cita 2 ya está CANCELADA en reset-citas.sql
        mockMvc.perform(patch("/api/citas/2/cancelar"))
                .andExpect(status().isBadRequest());
    }

    // --- PRUEBAS HU-09: REASIGNACIÓN DE CITAS MÉDICAS ENTRE DOCTORES ---

    @Test
    @DisplayName("HU-09: Debería retornar 200 OK al reasignar cita exitosamente a otro doctor de la misma especialidad")
    void deberiaReasignarCita_Exitosamente() throws Exception {
        ComandoReasignarCita comando = new ComandoReasignarCita("79123456");

        mockMvc.perform(patch("/api/citas/1/reasignar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor.id", is(1)))
                .andExpect(jsonPath("$.valor.doctorDocumento", is("79123456")))
                .andExpect(jsonPath("$.valor.estado", is("REASIGNADA")))
                .andExpect(jsonPath("$.mensaje", is("Cita médica reasignada exitosamente")));
    }

    @Test
    @DisplayName("HU-09: Debería retornar 404 Not Found al reasignar cita inexistente")
    void deberiaRetornar404_AlReasignarCitaInexistente() throws Exception {
        ComandoReasignarCita comando = new ComandoReasignarCita("79123456");

        mockMvc.perform(patch("/api/citas/999/reasignar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("HU-09 (Regla 1): Debería retornar 404 Not Found cuando el nuevo doctor no existe")
    void deberiaRetornar404_AlReasignarDoctorInexistente() throws Exception {
        ComandoReasignarCita comando = new ComandoReasignarCita("99999999");

        mockMvc.perform(patch("/api/citas/1/reasignar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("HU-09 (Regla 13): Debería retornar 400 Bad Request cuando el nuevo doctor está inhabilitado")
    void deberiaRetornar400_AlReasignarDoctorInhabilitado() throws Exception {
        // Doctor 91234567 tiene habilitado = false en reset-citas.sql
        ComandoReasignarCita comando = new ComandoReasignarCita("91234567");

        mockMvc.perform(patch("/api/citas/1/reasignar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("HU-09 (Regla 9): Debería retornar 400 Bad Request cuando el nuevo doctor tiene diferente especialidad")
    void deberiaRetornar400_AlReasignarDoctorDiferenteEspecialidad() throws Exception {
        // Doctor original Ana Torres es CARDIOLOGIA. Doctor 80234567 es PEDIATRIA
        ComandoReasignarCita comando = new ComandoReasignarCita("80234567");

        mockMvc.perform(patch("/api/citas/1/reasignar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("HU-09 (Regla 4): Debería retornar 409 Conflict cuando el nuevo doctor tiene cruce de horario")
    void deberiaRetornar409_AlReasignarDoctorConCruceDeHorario() throws Exception {
        // Doctor 92345678 ya tiene la cita 4 en la misma fecha y hora '2028-10-10 10:00:00'
        ComandoReasignarCita comando = new ComandoReasignarCita("92345678");

        mockMvc.perform(patch("/api/citas/1/reasignar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(comando)))
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("HU-08: Debería permitir cancelar una cita que previamente fue reasignada")
    void deberiaCancelarCita_PreviamenteReasignada() throws Exception {
        ComandoReasignarCita reasignarComando = new ComandoReasignarCita("79123456");
        mockMvc.perform(patch("/api/citas/1/reasignar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reasignarComando)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor.estado", is("REASIGNADA")));

        ComandoCancelarCita cancelarComando = new ComandoCancelarCita("Paciente no puede asistir");
        mockMvc.perform(patch("/api/citas/1/cancelar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cancelarComando)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor.id", is(1)))
                .andExpect(jsonPath("$.valor.estado", is("CANCELADA")));
    }

    @Test
    @DisplayName("HU-09: Debería permitir volver a reasignar una cita que ya fue reasignada a otro doctor")
    void deberiaPermitirReasignarCita_PreviamenteReasignada() throws Exception {
        ComandoReasignarCita primerComando = new ComandoReasignarCita("79123456");
        mockMvc.perform(patch("/api/citas/1/reasignar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(primerComando)))
                .andExpect(status().isOk());

        ComandoReasignarCita segundoComando = new ComandoReasignarCita("21754896");
        mockMvc.perform(patch("/api/citas/1/reasignar")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(segundoComando)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valor.doctorDocumento", is("21754896")))
                .andExpect(jsonPath("$.valor.estado", is("REASIGNADA")));
    }
}
