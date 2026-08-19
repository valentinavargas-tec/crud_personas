package ceiba.com.co.paciente.comando.manejador;

import ceiba.com.co.manejador.ManejadorComando;
import ceiba.com.co.paciente.comando.ComandoEliminarPaciente;
import ceiba.com.co.paciente.servicio.ServicioEliminarPaciente;
import org.springframework.stereotype.Component;

@Component
public class ManejadorEliminarPaciente implements ManejadorComando<ComandoEliminarPaciente> {

    private final ServicioEliminarPaciente servicioEliminarPaciente;

    public ManejadorEliminarPaciente(ServicioEliminarPaciente servicioEliminarPaciente) {
        this.servicioEliminarPaciente = servicioEliminarPaciente;
    }

    @Override
    public void ejecutar(ComandoEliminarPaciente comandoEliminarPaciente) {
        this.servicioEliminarPaciente.ejecutar(comandoEliminarPaciente.numeroDocumento());
    }
}
