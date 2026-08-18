package ceiba.com.co.servicio;

import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.modelo.entidad.Persona;
import ceiba.com.co.puerto.repositorio.RepositorioPersona;
import java.time.LocalDate;

public class ServicioActualizarPersona {

    private final RepositorioPersona repositorioPersona;

    public ServicioActualizarPersona(RepositorioPersona repositorioPersona) {
        this.repositorioPersona = repositorioPersona;
    }

    public void ejecutar(Long cedula, String nombre, String apellido, String email, LocalDate fechaNacimiento) {
        Persona personaExistente = this.repositorioPersona.obtener(cedula);
        if (personaExistente == null) {
            throw new ExcepcionSinDatos("No existe la persona que desea actualizar");
        }

        if (!personaExistente.getEmail().equals(email) && 
            this.repositorioPersona.existeConEmail(email)) {
            throw new ExcepcionDuplicidad("El email ya está registrado: " + email);
        }

        Persona personaActualizada = personaExistente.actualizarDatos(nombre, apellido, email, fechaNacimiento);
        this.repositorioPersona.actualizar(personaActualizada);
    }
}
