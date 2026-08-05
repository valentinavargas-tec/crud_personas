package ceiba.com.co.comando.fabrica;

import ceiba.com.co.comando.ComandoPersona;
import ceiba.com.co.modelo.entidad.Persona;
import org.springframework.stereotype.Component;

@Component
public class FabricaPersona {

    public Persona crear(ComandoPersona comandoPersona) {
        return new Persona(
                comandoPersona.cedula(),
                comandoPersona.nombre(),
                comandoPersona.apellido(),
                comandoPersona.email(),
                comandoPersona.fechaNacimiento()
        );
    }
}
