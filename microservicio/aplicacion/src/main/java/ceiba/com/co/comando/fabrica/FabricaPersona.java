package ceiba.com.co.comando.fabrica;

import ceiba.com.co.comando.ComandoPersona;
import ceiba.com.co.modelo.entidad.Email;
import ceiba.com.co.modelo.entidad.Nombre;
import ceiba.com.co.modelo.entidad.Persona;
import org.springframework.stereotype.Component;

@Component
public class FabricaPersona {

    public Persona crear(ComandoPersona comandoPersona) {
        return Persona.builder()
                .conCedula(comandoPersona.cedula())
                .conNombre(new Nombre(comandoPersona.nombre(), "El nombre es obligatorio", "El nombre contiene caracteres no permitidos"))
                .conApellido(new Nombre(comandoPersona.apellido(), "El apellido es obligatorio", "El apellido contiene caracteres no permitidos"))
                .conEmail(new Email(comandoPersona.email()))
                .conFechaNacimiento(comandoPersona.fechaNacimiento())
                .build();
    }
}
