package ceiba.com.co.doctor.puerto.repositorio;

import ceiba.com.co.doctor.modelo.entidad.Doctor;

import java.util.Optional;

public interface RepositorioDoctor {
    
    void guardar(Doctor doctor);
    void actualizar(Doctor doctor);
    void deshabilitar(String numeroDocumento);
    Optional<Doctor> obtenerPorNumeroDocumento(String numeroDocumento);
    boolean existePorCorreoInstitucional(String correoInstitucional);
    boolean existePorTarjetaProfesional(String tarjetaProfesional);
    boolean existeCorreoParaOtroDoctor(String correoInstitucional, String numeroDocumentoExcluido);
}
