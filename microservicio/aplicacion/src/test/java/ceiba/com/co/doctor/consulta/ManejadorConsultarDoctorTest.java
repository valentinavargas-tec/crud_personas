package ceiba.com.co.doctor.consulta;

import ceiba.com.co.doctor.puerto.dao.DaoDoctor;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorConsultarDoctorTest {

    private DaoDoctor daoDoctor;
    private ManejadorConsultarDoctor manejadorConsultarDoctor;

    @BeforeEach
    void setUp() {
        daoDoctor = Mockito.mock(DaoDoctor.class);
        manejadorConsultarDoctor = new ManejadorConsultarDoctor(daoDoctor);
    }

    @Test
    @DisplayName("Debería retornar DtoDoctor cuando el doctor existe")
    void deberiaRetornarDtoDoctor_Cuando_DoctorExiste() {
        // Arrange
        String documento = "DOC-001";
        DtoDoctor doctorDto = new DtoDoctor(documento, "Ana", "Torres", "TP-001", "CARDIOLOGIA", "ana@hospital.com", true);
        when(daoDoctor.buscarPorNumeroDocumento(documento))
                .thenReturn(Optional.of(doctorDto));

        // Act
        DtoDoctor resultado = manejadorConsultarDoctor.ejecutar(documento);

        // Assert
        assertNotNull(resultado);
        assertEquals(documento, resultado.numeroDocumento());
        assertEquals("Ana", resultado.nombre());
        assertEquals("Torres", resultado.apellido());
        assertEquals("CARDIOLOGIA", resultado.especialidad());
        assertTrue(resultado.habilitado());
    }

    @Test
    @DisplayName("Debería lanzar ExcepcionSinDatos cuando el doctor no existe")
    void deberiaLanzarExcepcionSinDatos_Cuando_DoctorNoExiste() {
        // Arrange
        String documentoInexistente = "NOEXISTE";
        when(daoDoctor.buscarPorNumeroDocumento(documentoInexistente))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ExcepcionSinDatos.class,
                () -> manejadorConsultarDoctor.ejecutar(documentoInexistente));
    }
}
