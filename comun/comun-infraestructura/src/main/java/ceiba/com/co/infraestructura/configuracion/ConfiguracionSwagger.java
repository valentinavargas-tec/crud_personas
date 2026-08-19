package ceiba.com.co.infraestructura.configuracion;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;


@Configuration
public class ConfiguracionSwagger {
    @Bean
    public OpenAPI customOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8083");
        devServer.setDescription("Entorno de Desarrollo Local");

        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de Pacientes")
                        .version("v1.0.0")
                        .description("""
                                ### Documentación Oficial de la API REST
                                
                                Esta API provee los servicios necesarios para la administración integral del ciclo de vida de las **Pacientes** dentro de la plataforma.
                                
                                #### Arquitectura y Patrones
                                - **Arquitectura Hexagonal**: Fuerte separación entre el Dominio, la Aplicación y la Infraestructura.
                                - **CQRS**: Separación de las operaciones de lectura (Queries) y escritura (Commands).
                                - **Manejo de Errores**: Estandarizado bajo la especificación **RFC 7807** (Problem Detail for HTTP APIs).
                                """)
                        .contact(new Contact()
                                .name("Valentina")
                                .email("vargas.valentina@ceiba.com.co")));
    }

    @Bean
    public OpenApiCustomizer pacientelizarSchemasOpenApi() {
        return openApi -> {
            if (openApi.getComponents() == null || openApi.getComponents().getSchemas() == null) {
                return;
            }

            Schema<?> comandoActualizarSchema = openApi.getComponents().getSchemas().get("ComandoActualizarPaciente");
            if (comandoActualizarSchema != null) {
                comandoActualizarSchema.setRequired(List.of("nombre", "apellido", "email", "fechaNacimiento"));
                enriquecerPropiedad(comandoActualizarSchema, "nombre", "Nombre(s) a actualizar", "Carlos Alberto");
                enriquecerPropiedad(comandoActualizarSchema, "apellido", "Apellido(s) a actualizar", "Pérez Gómez");

                Schema<?> emailProp = (Schema<?>) comandoActualizarSchema.getProperties().get("email");
                if (emailProp != null) {
                    emailProp.setDescription("Correo electrónico actualizado");
                    emailProp.setExample("carlos.updated@example.com");
                    emailProp.setFormat("email");
                }

                enriquecerPropiedad(comandoActualizarSchema, "fechaNacimiento", "Fecha de nacimiento (YYYY-MM-DD)", "1990-05-15");
            }

            Schema<?> pacienteDtoSchema = openApi.getComponents().getSchemas().get("PacienteDTO");
            if (pacienteDtoSchema != null) {
                pacienteDtoSchema.setRequired(List.of("cedula", "nombre", "apellido", "email"));

                enriquecerPropiedad(pacienteDtoSchema, "cedula", "Número de cédula de ciudadanía", 1017123456);
                enriquecerPropiedad(pacienteDtoSchema, "nombre", "Nombre(s) de la paciente", "Carlos");
                enriquecerPropiedad(pacienteDtoSchema, "apellido", "Apellido(s) de la paciente", "Pérez");

                Schema<?> emailProp = (Schema<?>) pacienteDtoSchema.getProperties().get("email");
                if (emailProp != null) {
                    emailProp.setDescription("Correo electrónico institucional o pacientel");
                    emailProp.setExample("carlos.perez@example.com");
                    emailProp.setFormat("email");
                }

                enriquecerPropiedad(pacienteDtoSchema, "fechaNacimiento", "Fecha de nacimiento (YYYY-MM-DD)", "1990-05-15");
            }

            Schema<?> comandoPacienteSchema = openApi.getComponents().getSchemas().get("ComandoPaciente");
            if (comandoPacienteSchema != null) {
                comandoPacienteSchema.setRequired(List.of("cedula", "nombre", "apellido", "email", "fechaNacimiento"));
                enriquecerPropiedad(comandoPacienteSchema, "cedula", "Número de cédula único para registro", 1017123456);
                enriquecerPropiedad(comandoPacienteSchema, "nombre", "Nombre(s) de la paciente", "Carlos");
                enriquecerPropiedad(comandoPacienteSchema, "apellido", "Apellido(s) de la paciente", "Pérez");

                Schema<?> emailProp = (Schema<?>) comandoPacienteSchema.getProperties().get("email");
                if (emailProp != null) {
                    emailProp.setDescription("Correo electrónico institucional o pacientel");
                    emailProp.setExample("carlos.perez@example.com");
                    emailProp.setFormat("email");
                }

                enriquecerPropiedad(comandoPacienteSchema, "fechaNacimiento", "Fecha de nacimiento (YYYY-MM-DD)", "1990-05-15");
            }

            Schema<?> problemDetailSchema = openApi.getComponents().getSchemas().get("ProblemDetail");
            if (problemDetailSchema != null) {
                enriquecerPropiedad(problemDetailSchema, "type", "URI que identifica el tipo de error", "about:blank");
                enriquecerPropiedad(problemDetailSchema, "title", "Resumen corto del error", "ExcepcionDuplicidad");
                enriquecerPropiedad(problemDetailSchema, "status", "Código de estado HTTP", 400);
                enriquecerPropiedad(problemDetailSchema, "detail", "Explicación detallada del error específico", "La paciente con cédula ya existe");
                enriquecerPropiedad(problemDetailSchema, "instance", "URI del endpoint donde ocurrió el error", "/api/pacientes");
            }

            Schema<?> paginaSchema = openApi.getComponents().getSchemas().get("Pagina");
            if (paginaSchema != null) {
                enriquecerPropiedad(paginaSchema, "totalElementos", "Cantidad total de elementos encontrados", 42L);
                enriquecerPropiedad(paginaSchema, "totalPaginas", "Cantidad total de páginas disponibles", 5);
                enriquecerPropiedad(paginaSchema, "numeroPagina", "Número de página actual (base 0)", 0);
                enriquecerPropiedad(paginaSchema, "tamanoPagina", "Tamaño de elementos por página", 10);

                Schema<?> contenidoProp = (Schema<?>) paginaSchema.getProperties().get("contenido");
                if (contenidoProp != null) {
                    contenidoProp.setDescription("Lista de pacientes registradas en la página actual");
                    Schema<?> pacienteRefSchema = new Schema<>().$ref("#/components/schemas/PacienteDTO");
                    contenidoProp.setItems(pacienteRefSchema);
                }
            }

            Schema<?> comandoRespuestaSchema = openApi.getComponents().getSchemas().get("ComandoRespuesta");
            if (comandoRespuestaSchema != null) {
                enriquecerPropiedad(comandoRespuestaSchema, "valor", "Resultado o mensaje devuelto por la ejecución del comando", "Operación realizada con éxito");
            }

        };
    }

    @SuppressWarnings("unchecked")
    private void enriquecerPropiedad(Schema<?> parentSchema, String propertyName, String description, Object example) {
        if (parentSchema.getProperties() != null && parentSchema.getProperties().containsKey(propertyName)) {
            Schema<Object> prop = (Schema<Object>) parentSchema.getProperties().get(propertyName);
            if (prop != null) {
                prop.setDescription(description);
                prop.setExample(example);
            }
        }
    }
}
