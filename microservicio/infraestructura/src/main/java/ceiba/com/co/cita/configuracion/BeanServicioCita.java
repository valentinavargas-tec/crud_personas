package ceiba.com.co.cita.configuracion;

import ceiba.com.co.cita.puerto.repositorio.RepositorioCita;
import ceiba.com.co.cita.servicio.ServicioAgendarCita;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.paciente.puerto.repositorio.RepositorioPaciente;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanServicioCita {

    @Bean
    public ServicioAgendarCita servicioAgendarCita(RepositorioCita repositorioCita,
                                                    RepositorioDoctor repositorioDoctor,
                                                    RepositorioPaciente repositorioPaciente) {
        return new ServicioAgendarCita(repositorioCita, repositorioDoctor, repositorioPaciente);
    }
}
