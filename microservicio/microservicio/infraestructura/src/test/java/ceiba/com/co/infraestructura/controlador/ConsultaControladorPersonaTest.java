package ceiba.com.co.infraestructura.controlador;

import ceiba.com.co.ApplicationMock;
import ceiba.com.co.controlador.ConsultaControladorPersona;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(ConsultaControladorPersona.class)
@ContextConfiguration(classes = ApplicationMock.class)
class ConsultaControladorPersonaTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void deberia_RetornarListaOrdenadaPorApellido_Cuando_ListaTodasLasPersonas() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].apellido", is("Alvarez")))
                .andExpect(jsonPath("$[1].apellido", is("Gomez")))
                .andExpect(jsonPath("$[2].apellido", is("Perez")));
    }

    @Test
    void deberia_RetornarPersona_Cuando_CedulaExiste() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas/123456789")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nombre", is("Juan")))
                .andExpect(jsonPath("$.apellido", is("Perez")))
                .andExpect(jsonPath("$.email", is("juan.perez@gmail.com")));
    }

    @Test
    void deberia_Retornar404_Cuando_CedulaNoExiste() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas/999999999")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionSinDatos")))
                .andExpect(jsonPath("$.mensaje", is("Persona no encontrada")));
    }


    @Test
    void deberia_FiltrarPorNombre_Cuando_ParametroNombreEsProporcionado() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas/search?nombre=Carlos")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElementos", is(1)))
                .andExpect(jsonPath("$.contenido[0].nombre", is("Carlos")))
                .andExpect(jsonPath("$.contenido[0].apellido", is("Alvarez")));
    }

    @Test
    void deberia_FiltrarPorRangoEdad_Cuando_EdadMinimaYMaximaSonProporcionadas() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas/search?edadMinima=30&edadMaxima=45")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElementos", is(2)))
                .andExpect(jsonPath("$.contenido[0].nombre", is("Maria")))
                .andExpect(jsonPath("$.contenido[1].nombre", is("Juan")));
    }

    @Test
    void deberia_FiltrarPorApellidoConOrdenamiento_Cuando_ParametroSortEsProporcionado() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas/search?apellido=Perez&sort=fechaNacimiento,desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElementos", is(1)))
                .andExpect(jsonPath("$.contenido[0].nombre", is("Juan")));
    }

    @Test
    void deberia_FiltrarPorEdadMaxima_Cuando_SoloEdadMaximaEsProporcionada() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas/search?edadMaxima=25")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElementos", is(1)))
                .andExpect(jsonPath("$.contenido[0].nombre", is("Carlos")));
    }

    @Test
    void deberia_FiltrarPorEdadMaximaConOrdenamiento_Cuando_AmbosParametrosSonProporcionados() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas/search?edadMaxima=30&sort=fechaNacimiento,desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void deberia_RetornarTodasLasPersonasConValoresPorDefecto_Cuando_SinCriteriosDeBusqueda() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas/search")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElementos", is(3)));
    }

    @Test
    void deberia_Retornar400_Cuando_CampoDeOrdenamientoInvalido() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas/search?sort=password,asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionValorInvalido")));
    }

    @Test
    void deberia_Retornar400_Cuando_FormatoDeOrdenamientoInvalido() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas/search?sort=nombre;drop table--")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.nombreExcepcion", is("ExcepcionValorInvalido")));
    }

    @Test
    void deberia_FiltrarSoloPorApellido_Cuando_SoloApellidoEsProporcionado() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas/search?apellido=Gomez")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElementos", is(1)))
                .andExpect(jsonPath("$.contenido[0].nombre", is("Maria")));
    }

    @Test
    void deberia_FiltrarPorEdadMinima_Cuando_SoloEdadMinimaEsProporcionada() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas/search?edadMinima=35")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalElementos", is(2)))
                .andExpect(jsonPath("$.contenido[0].nombre", is("Maria")));
    }

    @Test
    void deberia_UsarTamanoPaginaPersonalizado_Cuando_ParametroSizeEsProporcionado() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas/search?page=0&size=2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.tamanoPagina", is(2)));
    }

    @Test
    void deberia_OrdenarPorNombreDesc_Cuando_OrdenamientoEsSolicitadoDesc() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas/search?sort=nombre,desc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].nombre", is("Maria")));
    }

    @Test
    void deberia_OrdenarPorNombreAsc_Cuando_OrdenamientoEsPorCampoQueNoEsFechaNacimiento() throws Exception {
        // Arrange & Act & Assert
        mockMvc.perform(get("/api/personas/search?sort=nombre,asc")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.contenido[0].nombre", is("Carlos")));
    }
}
