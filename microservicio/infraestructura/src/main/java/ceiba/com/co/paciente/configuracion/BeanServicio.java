package ceiba.com.co.paciente.configuracion;


import ceiba.com.co.paciente.puerto.repositorio.RepositorioPaciente;
import ceiba.com.co.paciente.servicio.ServicioActualizarPaciente;
import ceiba.com.co.paciente.servicio.ServicioCrearPaciente;
import ceiba.com.co.paciente.servicio.ServicioEliminarPaciente;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanServicio {

    @Bean
    public ServicioCrearPaciente servicioCrearPaciente(RepositorioPaciente repositorioPaciente) {
        return new ServicioCrearPaciente(repositorioPaciente);
    }

    @Bean
    public ServicioActualizarPaciente servicioActualizarPaciente(RepositorioPaciente repositorioPaciente) {
        return new ServicioActualizarPaciente(repositorioPaciente);
    }

    @Bean
    public ServicioEliminarPaciente servicioEliminarPaciente(RepositorioPaciente repositorioPaciente) {
        return new ServicioEliminarPaciente(repositorioPaciente);
    }

}
