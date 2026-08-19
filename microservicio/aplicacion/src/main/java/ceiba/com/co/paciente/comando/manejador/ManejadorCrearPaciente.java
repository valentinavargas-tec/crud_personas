package ceiba.com.co.paciente.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.manejador.ManejadorComandoRespuesta;
import ceiba.com.co.paciente.comando.ComandoPaciente;
import ceiba.com.co.paciente.comando.fabrica.FabricaPaciente;
import ceiba.com.co.paciente.modelo.entidad.Paciente;
import ceiba.com.co.paciente.servicio.ServicioCrearPaciente;
import org.springframework.stereotype.Component;

@Component
public class ManejadorCrearPaciente implements ManejadorComandoRespuesta<ComandoPaciente, ComandoRespuesta<Long>> {

    private final FabricaPaciente fabricaPaciente;
    private final ServicioCrearPaciente servicioCrearPaciente;

    public ManejadorCrearPaciente(FabricaPaciente fabricaPaciente, ServicioCrearPaciente servicioCrearPaciente) {
        this.fabricaPaciente = fabricaPaciente;
        this.servicioCrearPaciente = servicioCrearPaciente;
    }

    @Override
    public ComandoRespuesta<Long> ejecutar(ComandoPaciente comandoPaciente) {
        Paciente paciente = this.fabricaPaciente.crear(comandoPaciente);
        return new ComandoRespuesta<>(this.servicioCrearPaciente.ejecutar(paciente), "Paciente creado exitosamente");
    }
}
