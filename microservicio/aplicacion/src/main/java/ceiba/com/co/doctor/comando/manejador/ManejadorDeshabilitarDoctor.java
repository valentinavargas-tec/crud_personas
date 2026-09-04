package ceiba.com.co.doctor.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.doctor.servicio.ServicioDeshabilitarDoctor;
import org.springframework.stereotype.Component;

@Component
public class ManejadorDeshabilitarDoctor {

    private final ServicioDeshabilitarDoctor servicioDeshabilitarDoctor;

    public ManejadorDeshabilitarDoctor(ServicioDeshabilitarDoctor servicioDeshabilitar) {
        this.servicioDeshabilitarDoctor = servicioDeshabilitar;
    }

    public ComandoRespuesta<String> ejecutar(String numeroDocumento) {
        this.servicioDeshabilitarDoctor.ejecutar(numeroDocumento);
        return new ComandoRespuesta<>(numeroDocumento, "Doctor deshabilitado exitosamente");
    }

}