package ceiba.com.co.paciente.configuracion;

import ceiba.com.co.paciente.puerto.repositorio.RepositorioPaciente;
import ceiba.com.co.paciente.servicio.ServicioActualizarPaciente;
import ceiba.com.co.paciente.servicio.ServicioCrearPaciente;
import ceiba.com.co.paciente.servicio.ServicioEliminarPaciente;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class BeanServicioTest {

    @Mock
    private RepositorioPaciente repositorioPaciente;

    private BeanServicio beanServicio;

    @BeforeEach
    void setUp() {
        beanServicio = new BeanServicio();
    }

    @Test
    void deberiaCrearBeanServicioCrearPaciente() {
        // Arrange & Act
        ServicioCrearPaciente servicio = beanServicio.servicioCrearPaciente(repositorioPaciente);

        // Assert
        assertNotNull(servicio);
    }

    @Test
    void deberiaCrearBeanServicioActualizarPaciente() {
        // Arrange & Act
        ServicioActualizarPaciente servicio = beanServicio.servicioActualizarPaciente(repositorioPaciente);

        // Assert
        assertNotNull(servicio);
    }

    @Test
    void deberiaCrearBeanServicioEliminarPaciente() {
        // Arrange & Act
        ServicioEliminarPaciente servicio = beanServicio.servicioEliminarPaciente(repositorioPaciente);

        // Assert
        assertNotNull(servicio);
    }
}