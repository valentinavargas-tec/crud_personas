package ceiba.com.co.puerto.repositorio;

import ceiba.com.co.modelo.entidad.Persona;

public interface RepositorioPersona {

    Long guardar(Persona persona);
    Persona obtener(Long cedula);
    void actualizar(Persona persona);
    void eliminar(Long cedula);
    boolean existeConCedula(Long cedula);
    boolean existeConEmail(String email);
}
