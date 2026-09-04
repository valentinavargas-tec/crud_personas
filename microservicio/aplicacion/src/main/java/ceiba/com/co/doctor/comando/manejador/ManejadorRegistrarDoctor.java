package ceiba.com.co.doctor.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.doctor.comando.ComandoDoctor;
import ceiba.com.co.doctor.comando.fabrica.FabricaDoctor;
import ceiba.com.co.doctor.modelo.entidad.Doctor;
import ceiba.com.co.doctor.servicio.ServicioCrearDoctor;
import org.springframework.stereotype.Component;

@Component
public class ManejadorRegistrarDoctor {

    private final ServicioCrearDoctor servicioCrearDoctor;
    private final FabricaDoctor fabricaDoctor;

    public ManejadorRegistrarDoctor(ServicioCrearDoctor servicioCrearDoctor, FabricaDoctor fabricaDoctor) {
        this.servicioCrearDoctor = servicioCrearDoctor;
        this.fabricaDoctor = fabricaDoctor;
    }

    public ComandoRespuesta<String> ejecutar(ComandoDoctor comandoDoctor) {
        Doctor doctor = this.fabricaDoctor.crear(comandoDoctor);
        String numeroDocumento = this.servicioCrearDoctor.ejecutar(doctor);
        return new ComandoRespuesta<>(numeroDocumento, "Doctor registrado exitosamente");
    }
}
