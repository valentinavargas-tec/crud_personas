package ceiba.com.co.paciente.comando.fabrica;

import ceiba.com.co.paciente.comando.ComandoPaciente;
import ceiba.com.co.paciente.modelo.entidad.Email;
import ceiba.com.co.paciente.modelo.entidad.Nombre;
import ceiba.com.co.paciente.modelo.entidad.Paciente;
import ceiba.com.co.paciente.modelo.entidad.TipoDocumento;
import ceiba.com.co.paciente.modelo.entidad.Genero;
import org.springframework.stereotype.Component;

@Component
public class FabricaPaciente {

    public Paciente crear(ComandoPaciente comandoPaciente) {
        return Paciente.builder()
                .conNumeroDocumento(comandoPaciente.numeroDocumento())
                .conTipoDocumento(comandoPaciente.tipoDocumento() != null ? TipoDocumento.valueOf(comandoPaciente.tipoDocumento().toUpperCase()) : null)
                .conNombre(new Nombre(comandoPaciente.nombre(), "El nombre es obligatorio", "El nombre contiene caracteres no permitidos"))
                .conApellido(new Nombre(comandoPaciente.apellido(), "El apellido es obligatorio", "El apellido contiene caracteres no permitidos"))
                .conFechaNacimiento(comandoPaciente.fechaNacimiento())
                .conTelefono(comandoPaciente.telefono())
                .conCorreoElectronico(new Email(comandoPaciente.correoElectronico()))
                .conEps(comandoPaciente.eps())
                .conGenero(comandoPaciente.genero() != null ? Genero.valueOf(comandoPaciente.genero().toUpperCase()) : null)
                .build();
    }
}
