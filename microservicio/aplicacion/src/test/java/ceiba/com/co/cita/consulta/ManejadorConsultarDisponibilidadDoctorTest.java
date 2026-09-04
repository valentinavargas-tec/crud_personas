package ceiba.com.co.cita.consulta;

import ceiba.com.co.cita.consulta.manejador.ManejadorConsultarDisponibilidadDoctor;
import ceiba.com.co.cita.puerto.dao.DaoCita;
import ceiba.com.co.doctor.modelo.entidad.Doctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

class ManejadorConsultarDisponibilidadDoctorTest {

    private DaoCita daoCita;
    private RepositorioDoctor repositorioDoctor;
    private ManejadorConsultarDisponibilidadDoctor manejador;

    @BeforeEach
    void setUp() {
        daoCita = Mockito.mock(DaoCita.class);
        repositorioDoctor = Mockito.mock(RepositorioDoctor.class);
        manejador = new ManejadorConsultarDisponibilidadDoctor(daoCita, repositorioDoctor);
    }

    @Test
    @DisplayName("Debería retornar las franjas libres restando las ocupadas")
    void calcularDisponibilidad() {
        LocalDate fecha = LocalDate.of(2026, 9, 15);
        Doctor doctor = Doctor.builder().conNumeroDocumento("DOC").conNombre("N").conApellido("A")
                .conTarjetaProfesional("TP").conEspecialidad("ESP").conCorreoInstitucional("C").build();
        
        when(repositorioDoctor.obtenerPorNumeroDocumento("DOC")).thenReturn(Optional.of(doctor));

        DtoCita cita9am = new DtoCita(1L, "123", "DOC", LocalDateTime.of(fecha, LocalTime.of(9, 0)), "T", "PROGRAMADA", "M");
        DtoCita cita11am = new DtoCita(2L, "123", "DOC", LocalDateTime.of(fecha, LocalTime.of(11, 0)), "T", "EN_ATENCION", "M");
        DtoCita cita1pm = new DtoCita(3L, "123", "DOC", LocalDateTime.of(fecha, LocalTime.of(13, 0)), "T", "CANCELADA", "M");

        when(daoCita.buscarCitasDoctorPorFecha("DOC", fecha)).thenReturn(List.of(cita9am, cita11am, cita1pm));

        DtoDisponibilidadDoctor resultado = manejador.ejecutar("DOC", fecha);

        assertEquals(8, resultado.franjasLibres().size());
        assertEquals(LocalTime.of(8, 0), resultado.franjasLibres().get(0));
        assertEquals(LocalTime.of(10, 0), resultado.franjasLibres().get(1));
        assertEquals(LocalTime.of(13, 0), resultado.franjasLibres().get(3)); // Cita cancelada cuenta como libre
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionSinDatos si el doctor no existe")
    void doctorNoExiste() {
        when(repositorioDoctor.obtenerPorNumeroDocumento("DOC")).thenReturn(Optional.empty());
        assertThrows(ExcepcionSinDatos.class, () -> manejador.ejecutar("DOC", LocalDate.now()));
    }
}
