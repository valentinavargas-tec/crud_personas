package ceiba.com.co.comando.manejador;

import ceiba.com.co.ComandoRespuesta;
import ceiba.com.co.manejador.ManejadorComandoRespuesta;
import ceiba.com.co.comando.ComandoPersona;
import ceiba.com.co.comando.fabrica.FabricaPersona;
import ceiba.com.co.modelo.entidad.Persona;
import ceiba.com.co.servicio.ServicioCrearPersona;
import org.springframework.stereotype.Component;

@Component
public class ManejadorCrearPersona implements ManejadorComandoRespuesta<ComandoPersona, ComandoRespuesta<Long>> {

    private final FabricaPersona fabricaPersona;
    private final ServicioCrearPersona servicioCrearPersona;

    public ManejadorCrearPersona(FabricaPersona fabricaPersona, ServicioCrearPersona servicioCrearPersona) {
        this.fabricaPersona = fabricaPersona;
        this.servicioCrearPersona = servicioCrearPersona;
    }

    @Override
    public ComandoRespuesta<Long> ejecutar(ComandoPersona comandoPersona) {
        Persona persona = this.fabricaPersona.crear(comandoPersona);
        return new ComandoRespuesta<>(this.servicioCrearPersona.ejecutar(persona));
    }
}
