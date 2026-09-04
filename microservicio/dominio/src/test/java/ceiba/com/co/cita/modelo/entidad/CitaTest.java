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

    @Test
    @DisplayName("Debería reprogramar cita exitosamente cuando estado es PROGRAMADA y fecha es futura")
    void deberiaReprogramarCita_CuandoDatosValidos() {
        Cita cita = Cita.builder()
                .conPacienteDocumento("123456789")
                .conDoctorDocumento("DOC-001")
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .conMotivo("Chequeo")
                .build();

        LocalDateTime nuevaFecha = FECHA_FUTURA.plusDays(3);
        cita.reprogramar(nuevaFecha, TipoCita.ESPECIALIZADA, "Cambio por viaje");

        assertEquals(nuevaFecha, cita.getFechaHora());
        assertEquals(TipoCita.ESPECIALIZADA, cita.getTipoCita());
        assertEquals("Cambio por viaje", cita.getMotivo());
        assertEquals(EstadoCita.PROGRAMADA, cita.getEstado());
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionValorInvalido (Regla 6) al reprogramar con fecha pasada")
    void deberiaLanzarExcepcion_AlReprogramarConFechaPasada() {
        Cita cita = Cita.builder()
                .conPacienteDocumento("123456789")
                .conDoctorDocumento("DOC-001")
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .build();

        assertThrows(ExcepcionValorInvalido.class, () ->
                cita.reprogramar(FECHA_PASADA, TipoCita.CONTROL, "Nuevo motivo"));
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionReglaNegocio al reprogramar cita que no está PROGRAMADA")
    void deberiaLanzarExcepcion_AlReprogramarCitaNoProgramada() {
        Cita cita = Cita.builder()
                .conPacienteDocumento("123456789")
                .conDoctorDocumento("DOC-001")
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .build();

        cita.cancelar("Ya no deseo asistir");

        assertThrows(ceiba.com.co.excepcion.ExcepcionReglaNegocio.class, () ->
                cita.reprogramar(FECHA_FUTURA.plusDays(2), TipoCita.CONTROL, "Intento"));
    }

    @Test
    @DisplayName("Debería cancelar cita sin motivo y lanzar excepción si se intenta cancelar dos veces")
    void deberiaCancelarCitaSinMotivo_YLanzarExcepcionAlCancelarDosVeces() {
        Cita cita = Cita.builder()
                .conPacienteDocumento("123456789")
                .conDoctorDocumento("DOC-001")
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .build();

        cita.cancelar();
        assertEquals(EstadoCita.CANCELADA, cita.getEstado());

        assertThrows(ceiba.com.co.excepcion.ExcepcionReglaNegocio.class, cita::cancelar);
    }

    @Test
    @DisplayName("Debería reasignar doctor manteniendo la misma fechaHora original")
    void deberiaReasignarDoctor_ManteniendoMismaFechaHora() {
        Cita cita = Cita.builder()
                .conPacienteDocumento("123456789")
                .conDoctorDocumento("DOC-001")
                .conFechaHora(FECHA_FUTURA)
                .conTipoCita(TipoCita.CONSULTA_GENERAL)
                .build();

        cita.reasignar("DOC-002");
        assertEquals(EstadoCita.REASIGNADA, cita.getEstado());
        assertEquals("DOC-002", cita.getDoctorDocumento());
        assertEquals(FECHA_FUTURA, cita.getFechaHora());
    }
}
