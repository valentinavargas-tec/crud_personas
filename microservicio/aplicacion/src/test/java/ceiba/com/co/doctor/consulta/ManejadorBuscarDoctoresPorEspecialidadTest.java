package ceiba.com.co.doctor.consulta;

import ceiba.com.co.doctor.puerto.dao.DaoDoctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorBuscarDoctoresPorEspecialidadTest {

    private DaoDoctor daoDoctor;
    private ManejadorBuscarDoctoresPorEspecialidad manejadorBuscarDoctoresPorEspecialidad;

    @BeforeEach
    void setUp() {
        daoDoctor = Mockito.mock(DaoDoctor.class);
        manejadorBuscarDoctoresPorEspecialidad = new ManejadorBuscarDoctoresPorEspecialidad(daoDoctor);
    }

    @Test
    @DisplayName("Debería retornar solo doctores habilitados de la especialidad")
    void deberiaRetornarSoloDoctoresHabilitados_Cuando_BuscanPorEspecialidad() {
        // Arrange
        DtoDoctor doc1 = new DtoDoctor("DOC-001", "Dr1", "Test", "TP-1", "CARDIOLOGIA", "dr1@hospital.com", true);
        DtoDoctor doc2 = new DtoDoctor("DOC-002", "Dr2", "Test", "TP-2", "CARDIOLOGIA", "dr2@hospital.com", true);

        when(daoDoctor.buscarPorEspecialidad("CARDIOLOGIA"))
                .thenReturn(List.of(doc1, doc2));

        // Act
        List<DtoDoctor> resultado = manejadorBuscarDoctoresPorEspecialidad.ejecutar("CARDIOLOGIA");

        // Assert
        assertEquals(2, resultado.size());
        resultado.forEach(dto -> assertTrue(dto.habilitado()));
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no hay doctores de la especialidad")
    void deberiaRetornarListaVacia_Cuando_NoHayDoctoresDeLaEspecialidad() {
        // Arrange
        when(daoDoctor.buscarPorEspecialidad("DERMATOLOGIA"))
                .thenReturn(List.of());

        // Act
        List<DtoDoctor> resultado = manejadorBuscarDoctoresPorEspecialidad.ejecutar("DERMATOLOGIA");

        // Assert
        assertTrue(resultado.isEmpty());
    }
}
