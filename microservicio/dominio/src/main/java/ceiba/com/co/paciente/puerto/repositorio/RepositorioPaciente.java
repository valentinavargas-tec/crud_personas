package ceiba.com.co.paciente.puerto.repositorio;

import ceiba.com.co.paciente.modelo.entidad.Paciente;

public interface RepositorioPaciente {

    Long guardar(Paciente paciente);
    Paciente obtener(Long numeroDocumento);
    void actualizar(Paciente paciente);
    void eliminar(Long numeroDocumento);
    boolean existeConNumeroDocumento(Long numeroDocumento);
    boolean existeConCorreoElectronico(String correoElectronico);
}
