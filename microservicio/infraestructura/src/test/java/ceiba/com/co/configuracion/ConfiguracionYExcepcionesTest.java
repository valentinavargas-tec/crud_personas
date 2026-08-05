package ceiba.com.co.configuracion;

import ceiba.com.co.infraestructura.configuracion.ConfiguracionHeader;
import ceiba.com.co.infraestructura.configuracion.ConfiguracionSwagger;
import ceiba.com.co.infraestructura.error.Error;
import ceiba.com.co.infraestructura.excepcion.ExcepcionTecnica;
import ceiba.com.co.infraestructura.filtro.FiltroHeaderSeguridad;
import ceiba.com.co.infraestructura.jdbc.sqlstatement.StatementException;
import io.swagger.v3.oas.models.OpenAPI;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ConfiguracionYExcepcionesTest {

    @Test
    void deberiaCrearCustomOpenAPI() {
        ConfiguracionSwagger swagger = new ConfiguracionSwagger();
        OpenAPI openAPI = swagger.customOpenAPI();
        assertNotNull(openAPI);
        assertEquals("API de Gestión de Personas", openAPI.getInfo().getTitle());
    }

    @Test
    void deberiaCrearFiltroHeader() {
        ConfiguracionHeader header = new ConfiguracionHeader();
        FiltroHeaderSeguridad filtro = header.filtroHeader();
        assertNotNull(filtro);
    }

    @Test
    void deberiaCrearErrorDTO() {
        Error error = new Error("NombreExcepcion", "MensajeError");
        assertEquals("NombreExcepcion", error.getNombreExcepcion());
        assertEquals("MensajeError", error.getMensaje());
    }

    @Test
    void deberiaCrearExcepcionesTecnicas() {
        ExcepcionTecnica et = new ExcepcionTecnica("Error tecnico", new RuntimeException("Causa"));
        StatementException se = new StatementException("Error en SQL");

        assertEquals("Error tecnico", et.getMessage());
        assertNotNull(et.getCause());
        assertEquals("Error en SQL", se.getMessage());
    }

    @Test
    void deberiaAgregarHeadersDeSeguridad_Cuando_DoFilterEsLlamado() throws Exception {
        // Arrange
        FiltroHeaderSeguridad filtro = new FiltroHeaderSeguridad();
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        // Act
        filtro.doFilter(request, response, chain);

        // Assert
        verify(response).setHeader("X-XSS-Protection", "1; mode=block");
        verify(response).setHeader("X-Content-Type-Options", "nosniff");
        verify(response).setHeader("Pragma", "no-cache");
        verify(response).setHeader("X-Frame-Options", "SAMEORIGIN");
        verify(chain).doFilter(request, response);
    }
}