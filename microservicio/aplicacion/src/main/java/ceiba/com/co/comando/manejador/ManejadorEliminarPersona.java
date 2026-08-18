package ceiba.com.co.comando.manejador;

import ceiba.com.co.manejador.ManejadorComando;
import ceiba.com.co.comando.ComandoEliminarPersona;
import ceiba.com.co.servicio.ServicioEliminarPersona;
import org.springframework.stereotype.Component;

@Component
public class ManejadorEliminarPersona implements ManejadorComando<ComandoEliminarPersona> {

    private final ServicioEliminarPersona servicioEliminarPersona;

    public ManejadorEliminarPersona(ServicioEliminarPersona servicioEliminarPersona) {
        this.servicioEliminarPersona = servicioEliminarPersona;
    }

    @Override
    public void ejecutar(ComandoEliminarPersona comandoEliminarPersona) {
        this.servicioEliminarPersona.ejecutar(comandoEliminarPersona.cedula());
    }
}
