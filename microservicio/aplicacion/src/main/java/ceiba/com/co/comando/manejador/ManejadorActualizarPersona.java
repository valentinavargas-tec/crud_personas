package ceiba.com.co.comando.manejador;

import ceiba.com.co.comando.ComandoActualizarPersona;
import ceiba.com.co.servicio.ServicioActualizarPersona;
import org.springframework.stereotype.Component;

@Component
public class ManejadorActualizarPersona {

    private final ServicioActualizarPersona servicioActualizarPersona;

    public ManejadorActualizarPersona(ServicioActualizarPersona servicioActualizarPersona) {
        this.servicioActualizarPersona = servicioActualizarPersona;
    }

   public void ejecutar(Long cedula, ComandoActualizarPersona comando) {
        this.servicioActualizarPersona.ejecutar(
                cedula,
                comando.nombre(),
                comando.apellido(),
                comando.email(),
                comando.fechaNacimiento()
        );
    }
}
