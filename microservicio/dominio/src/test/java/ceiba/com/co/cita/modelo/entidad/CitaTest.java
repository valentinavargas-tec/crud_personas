package ceiba.com.co.cita.modelo.entidad;

import ceiba.com.co.excepcion.ExcepcionValorInvalido;
import ceiba.com.co.excepcion.ExcepcionValorObligatorio;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CitaTest {

    private static final LocalDateTime FECHA_FUTURA = LocalDateTime.now().plusDays(5);
    private static final LocalDateTime FECHA_PASADA = LocalDateTime.now().minusDays(1);

    @Test
    @DisplayName("Debería crear una cita con estado PROGRAMADA por defecto cuando los datos son válidos")
    void deberiaCrearCita_CuandoDatosValidos() {
        Cita cita = Cita.builder()
                .conPacienteDocumento("123456789")
                .conDoctorDocumento("DOC-001")
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .conMotivo("Chequeo de rutina")
                .build();

        assertEquals(EstadoCita.PROGRAMADA, cita.getEstado());
        assertEquals("123456789", cita.getPacienteDocumento());
        assertEquals("DOC-001", cita.getDoctorDocumento());
        assertEquals(TipoCita.CONSULTA_GENERAL, cita.getTipoCita());
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionValorInvalido (Regla 6) cuando la fecha es pasada")
    void deberiaLanzarExcepcion_CuandoFechaEsPasada() {
        assertThrows(ExcepcionValorInvalido.class, () ->
                Cita.builder()
                        .conPacienteDocumento("123456789")
                        .conDoctorDocumento("DOC-001")
                        .conFechaHora(FECHA_PASADA)
                        .conTipoCita(TipoCita.CONTROL)
                        .build()
        );
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el documento del paciente es nulo")
    void deberiaLanzarExcepcion_CuandoPacienteDocumentoEsNulo() {
        assertThrows(ExcepcionValorObligatorio.class, () ->
                Cita.builder()
                        .conPacienteDocumento(null)
                        .conDoctorDocumento("DOC-001")
                        .conFechaHora(FECHA_FUTURA)
                        .conTipoCita(TipoCita.ESPECIALIZADA)
                        .build()
        );
    }

    @Test
    @DisplayName("Debería lanzar excepción cuando el documento del doctor es nulo")
    void deberiaLanzarExcepcion_CuandoDoctorDocumentoEsNulo() {
        assertThrows(ExcepcionValorObligatorio.class, () ->
                Cita.builder()
                        .conPacienteDocumento("123456789")
                        .conDoctorDocumento(null)
                        .conFechaHora(FECHA_FUTURA)
                        .conTipoCita(TipoCita.TELEMEDICINA)
                        .build()
        );
    }

    @Test
    @DisplayName("Debería cancelar cita exitosamente cuando está PROGRAMADA y fecha es futura")
    void deberiaCancelarCita_CuandoEstadoProgramadaYFechaFutura() {
        Cita cita = Cita.builder()
                .conPacienteDocumento("123456789")
                .conDoctorDocumento("DOC-001")
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .conMotivo("Chequeo")
                .build();

        cita.cancelar("Motivo personal");

        assertEquals(EstadoCita.CANCELADA, cita.getEstado());
        org.junit.jupiter.api.Assertions.assertTrue(cita.getObservaciones().contains("Cancelada: Motivo personal"));
    }

    @Test
    @DisplayName("Debería reasignar doctor y fecha exitosamente cuando cita está PROGRAMADA")
    void deberiaReasignarCita_CuandoEstadoProgramada() {
        Cita cita = Cita.builder()
                .conPacienteDocumento("123456789")
                .conDoctorDocumento("DOC-001")
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .build();

        LocalDateTime nuevaFecha = FECHA_FUTURA.plusDays(2);
        cita.reasignar("DOC-002", nuevaFecha);

        assertEquals(EstadoCita.REASIGNADA, cita.getEstado());
        assertEquals("DOC-002", cita.getDoctorDocumento());
        assertEquals(nuevaFecha, cita.getFechaHora());
    }

    @Test
    @DisplayName("Debería iniciar y completar atención legalmente")
    void deberiaIniciarYCompletarAtencion() {
        Cita cita = Cita.builder()
                .conPacienteDocumento("123456789")
                .conDoctorDocumento("DOC-001")
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .build();

        cita.iniciarAtencion();
        assertEquals(EstadoCita.EN_ATENCION, cita.getEstado());

        cita.completar();
        assertEquals(EstadoCita.COMPLETADA, cita.getEstado());
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionReglaNegocio al intentar completar una cita no iniciada")
    void deberiaLanzarExcepcion_AlCompletarCitaNoIniciada() {
        Cita cita = Cita.builder()
                .conPacienteDocumento("123456789")
                .conDoctorDocumento("DOC-001")
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .build();

        assertThrows(ceiba.com.co.excepcion.ExcepcionReglaNegocio.class, cita::completar);
    }
}
