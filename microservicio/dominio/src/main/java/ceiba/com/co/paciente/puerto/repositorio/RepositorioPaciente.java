package ceiba.com.co.paciente.puerto.repositorio;

import ceiba.com.co.paciente.modelo.entidad.Paciente;
import java.util.Optional;

public interface RepositorioPaciente {

    Long guardar(Paciente paciente);
    Optional<Paciente> obtener(Long numeroDocumento);
    void actualizar(Paciente paciente);
    void eliminar(Long numeroDocumento);
    boolean existeConNumeroDocumento(Long numeroDocumento);
    boolean existeConCorreoElectronico(String correoElectronico);
}
