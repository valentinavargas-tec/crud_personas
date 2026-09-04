package ceiba.com.co.doctor.configuracion;

import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioEspecialidad;
import ceiba.com.co.doctor.servicio.ServicioActualizarDoctor;
import ceiba.com.co.doctor.servicio.ServicioCrearDoctor;
import ceiba.com.co.doctor.servicio.ServicioDeshabilitarDoctor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanServicioDoctor {

    @Bean
    public ServicioCrearDoctor servicioCrearDoctor(RepositorioDoctor repositorioDoctor, RepositorioEspecialidad repositorioEspecialidad) {
        return new ServicioCrearDoctor(repositorioDoctor, repositorioEspecialidad);
    }

    @Bean
    public ServicioActualizarDoctor servicioActualizarDoctor(RepositorioDoctor repositorioDoctor) {
        return new ServicioActualizarDoctor(repositorioDoctor);
    }

    @Bean
    public ServicioDeshabilitarDoctor servicioDeshabilitarDoctor(RepositorioDoctor repositorioDoctor) {
        return new ServicioDeshabilitarDoctor(repositorioDoctor);
    }
}
