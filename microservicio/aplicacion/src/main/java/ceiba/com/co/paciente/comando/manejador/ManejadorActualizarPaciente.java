package ceiba.com.co.paciente.comando.manejador;

import ceiba.com.co.paciente.comando.ComandoActualizarPaciente;
import ceiba.com.co.paciente.servicio.ServicioActualizarPaciente;
import ceiba.com.co.paciente.modelo.entidad.Genero;
import org.springframework.stereotype.Component;

@Component
public class ManejadorActualizarPaciente {

    private final ServicioActualizarPaciente servicioActualizarPaciente;

    public ManejadorActualizarPaciente(ServicioActualizarPaciente servicioActualizarPaciente) {
        this.servicioActualizarPaciente = servicioActualizarPaciente;
    }

   public void ejecutar(Long numeroDocumento, ComandoActualizarPaciente comando) {
        this.servicioActualizarPaciente.ejecutar(
                numeroDocumento,
                comando.nombre(),
                comando.apellido(),
                comando.fechaNacimiento(),
                comando.telefono(),
                comando.correoElectronico(),
                comando.eps(),
                Genero.valueOf(comando.genero())
        );
    }
}
