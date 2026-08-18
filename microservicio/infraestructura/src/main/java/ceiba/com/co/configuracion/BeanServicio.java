package ceiba.com.co.configuracion;


import ceiba.com.co.puerto.repositorio.RepositorioPersona;
import ceiba.com.co.servicio.ServicioActualizarPersona;
import ceiba.com.co.servicio.ServicioCrearPersona;
import ceiba.com.co.servicio.ServicioEliminarPersona;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanServicio {

    @Bean
    public ServicioCrearPersona servicioCrearPersona(RepositorioPersona repositorioPersona) {
        return new ServicioCrearPersona(repositorioPersona);
    }

    @Bean
    public ServicioActualizarPersona servicioActualizarPersona(RepositorioPersona repositorioPersona) {
        return new ServicioActualizarPersona(repositorioPersona);
    }

    @Bean
    public ServicioEliminarPersona servicioEliminarPersona(RepositorioPersona repositorioPersona) {
        return new ServicioEliminarPersona(repositorioPersona);
    }

}
