package ceiba.com.co.configuracion;

import ceiba.com.co.puerto.repositorio.RepositorioPersona;
import ceiba.com.co.servicio.ServicioActualizarPersona;
import ceiba.com.co.servicio.ServicioCrearPersona;
import ceiba.com.co.servicio.ServicioEliminarPersona;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
class BeanServicioTest {

    @Mock
    private RepositorioPersona repositorioPersona;

    private BeanServicio beanServicio;

    @BeforeEach
    void setUp() {
        beanServicio = new BeanServicio();
    }

    @Test
    void deberiaCrearBeanServicioCrearPersona() {
        // Arrange & Act
        ServicioCrearPersona servicio = beanServicio.servicioCrearPersona(repositorioPersona);

        // Assert
        assertNotNull(servicio);
    }

    @Test
    void deberiaCrearBeanServicioActualizarPersona() {
        // Arrange & Act
        ServicioActualizarPersona servicio = beanServicio.servicioActualizarPersona(repositorioPersona);

        // Assert
        assertNotNull(servicio);
    }

    @Test
    void deberiaCrearBeanServicioEliminarPersona() {
        // Arrange & Act
        ServicioEliminarPersona servicio = beanServicio.servicioEliminarPersona(repositorioPersona);

        // Assert
        assertNotNull(servicio);
    }
}