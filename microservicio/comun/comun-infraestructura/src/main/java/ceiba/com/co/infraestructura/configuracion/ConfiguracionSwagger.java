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
                        .title("API de Gestión de Personas")
                        .version("v1.0.0")
                        .description("""
                                ### Documentación Oficial de la API REST
                                
                                Esta API provee los servicios necesarios para la administración integral del ciclo de vida de las **Personas** dentro de la plataforma.
                                
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
    public OpenApiCustomizer personalizarSchemasOpenApi() {
        return openApi -> {
            if (openApi.getComponents() == null || openApi.getComponents().getSchemas() == null) {
                return;
            }

            Schema<?> comandoActualizarSchema = openApi.getComponents().getSchemas().get("ComandoActualizarPersona");
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

            Schema<?> personaDtoSchema = openApi.getComponents().getSchemas().get("PersonaDTO");
            if (personaDtoSchema != null) {
                personaDtoSchema.setRequired(List.of("cedula", "nombre", "apellido", "email"));

                enriquecerPropiedad(personaDtoSchema, "cedula", "Número de cédula de ciudadanía", 1017123456);
                enriquecerPropiedad(personaDtoSchema, "nombre", "Nombre(s) de la persona", "Carlos");
                enriquecerPropiedad(personaDtoSchema, "apellido", "Apellido(s) de la persona", "Pérez");

                Schema<?> emailProp = (Schema<?>) personaDtoSchema.getProperties().get("email");
                if (emailProp != null) {
                    emailProp.setDescription("Correo electrónico institucional o personal");
                    emailProp.setExample("carlos.perez@example.com");
                    emailProp.setFormat("email");
                }

                enriquecerPropiedad(personaDtoSchema, "fechaNacimiento", "Fecha de nacimiento (YYYY-MM-DD)", "1990-05-15");
            }

            Schema<?> comandoPersonaSchema = openApi.getComponents().getSchemas().get("ComandoPersona");
            if (comandoPersonaSchema != null) {
                comandoPersonaSchema.setRequired(List.of("cedula", "nombre", "apellido", "email", "fechaNacimiento"));
                enriquecerPropiedad(comandoPersonaSchema, "cedula", "Número de cédula único para registro", 1017123456);
                enriquecerPropiedad(comandoPersonaSchema, "nombre", "Nombre(s) de la persona", "Carlos");
                enriquecerPropiedad(comandoPersonaSchema, "apellido", "Apellido(s) de la persona", "Pérez");

                Schema<?> emailProp = (Schema<?>) comandoPersonaSchema.getProperties().get("email");
                if (emailProp != null) {
                    emailProp.setDescription("Correo electrónico institucional o personal");
                    emailProp.setExample("carlos.perez@example.com");
                    emailProp.setFormat("email");
                }

                enriquecerPropiedad(comandoPersonaSchema, "fechaNacimiento", "Fecha de nacimiento (YYYY-MM-DD)", "1990-05-15");
            }

            Schema<?> problemDetailSchema = openApi.getComponents().getSchemas().get("ProblemDetail");
            if (problemDetailSchema != null) {
                enriquecerPropiedad(problemDetailSchema, "type", "URI que identifica el tipo de error", "about:blank");
                enriquecerPropiedad(problemDetailSchema, "title", "Resumen corto del error", "ExcepcionDuplicidad");
                enriquecerPropiedad(problemDetailSchema, "status", "Código de estado HTTP", 400);
                enriquecerPropiedad(problemDetailSchema, "detail", "Explicación detallada del error específico", "La persona con cédula ya existe");
                enriquecerPropiedad(problemDetailSchema, "instance", "URI del endpoint donde ocurrió el error", "/api/personas");
            }

            Schema<?> paginaSchema = openApi.getComponents().getSchemas().get("Pagina");
            if (paginaSchema != null) {
                enriquecerPropiedad(paginaSchema, "totalElementos", "Cantidad total de elementos encontrados", 42L);
                enriquecerPropiedad(paginaSchema, "totalPaginas", "Cantidad total de páginas disponibles", 5);
                enriquecerPropiedad(paginaSchema, "numeroPagina", "Número de página actual (base 0)", 0);
                enriquecerPropiedad(paginaSchema, "tamanoPagina", "Tamaño de elementos por página", 10);

                Schema<?> contenidoProp = (Schema<?>) paginaSchema.getProperties().get("contenido");
                if (contenidoProp != null) {
                    contenidoProp.setDescription("Lista de personas registradas en la página actual");
                    Schema<?> personaRefSchema = new Schema<>().$ref("#/components/schemas/PersonaDTO");
                    contenidoProp.setItems(personaRefSchema);
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
