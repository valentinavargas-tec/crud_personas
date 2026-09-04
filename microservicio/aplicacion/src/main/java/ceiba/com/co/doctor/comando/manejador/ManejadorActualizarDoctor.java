package ceiba.com.co.doctor.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.doctor.comando.ComandoActualizarDoctor;
import ceiba.com.co.doctor.modelo.entidad.Doctor;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.doctor.servicio.ServicioActualizarDoctor;
import org.springframework.stereotype.Component;

@Component
public class ManejadorActualizarDoctor {

    private final ServicioActualizarDoctor servicioActualizarDoctor;
    private final RepositorioDoctor repositorioDoctor;

    public ManejadorActualizarDoctor(ServicioActualizarDoctor servicioActualizarDoctor, RepositorioDoctor repositorioDoctor) {
        this.servicioActualizarDoctor = servicioActualizarDoctor;
        this.repositorioDoctor = repositorioDoctor;
    }

    public ComandoRespuesta<String> ejecutar(String numeroDocumento, ComandoActualizarDoctor comando) {
        String tarjetaProfesional = obtenerTarjetaActual(numeroDocumento);
        if (tarjetaProfesional == null) {
            throw new ceiba.com.co.excepcion.ExcepcionSinDatos("No existe un doctor con el número de documento " + numeroDocumento + ".");
        }

        Doctor doctorActualizado = Doctor.builder()
                .conNumeroDocumento(numeroDocumento)
                .conNombre(comando.getNombre())
                .conApellido(comando.getApellido())
                .conTarjetaProfesional(tarjetaProfesional)
                .conEspecialidad(comando.getEspecialidad())
                .conCorreoInstitucional(comando.getCorreoInstitucional())
                .build();

        this.servicioActualizarDoctor.ejecutar(doctorActualizado);

        return new ComandoRespuesta<>(numeroDocumento, "Doctor actualizado exitosamente");
    }

    private String obtenerTarjetaActual(String numeroDocumento) {
        return this.repositorioDoctor
                .obtenerPorNumeroDocumento(numeroDocumento)
                .map(Doctor::getTarjetaProfesional)
                .orElse(null);
    }
}
