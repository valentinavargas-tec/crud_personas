package ceiba.com.co.doctor.consulta;

import ceiba.com.co.doctor.puerto.dao.DaoDoctor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ManejadorListarDoctoresTest {

    private DaoDoctor daoDoctor;
    private ManejadorListarDoctores manejadorListarDoctores;

    @BeforeEach
    void setUp() {
        daoDoctor = Mockito.mock(DaoDoctor.class);
        manejadorListarDoctores = new ManejadorListarDoctores(daoDoctor);
    }

    @Test
    @DisplayName("Debería retornar lista de DtoDoctor cuando existen doctores")
    void deberiaRetornarListaDtoDoctor_Cuando_ExistenDoctores() {
        // Arrange
        DtoDoctor doc1 = new DtoDoctor("DOC-001", "Ana", "Torres", "TP-001", "CARDIOLOGIA", "ana@hospital.com", true);
        DtoDoctor doc2 = new DtoDoctor("DOC-002", "Luis", "Ramos", "TP-002", "NEUROLOGIA", "luis@hospital.com", true);
        when(daoDoctor.listarTodos()).thenReturn(List.of(doc1, doc2));

        // Act
        List<DtoDoctor> resultado = manejadorListarDoctores.ejecutar();

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("DOC-001", resultado.get(0).numeroDocumento());
        assertEquals("DOC-002", resultado.get(1).numeroDocumento());
    }

    @Test
    @DisplayName("Debería retornar lista vacía cuando no hay doctores")
    void deberiaRetornarListaVacia_Cuando_NoHayDoctores() {
        // Arrange
        when(daoDoctor.listarTodos()).thenReturn(List.of());

        // Act
        List<DtoDoctor> resultado = manejadorListarDoctores.ejecutar();

        // Assert
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}
