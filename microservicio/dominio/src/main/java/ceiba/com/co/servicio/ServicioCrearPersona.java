package ceiba.com.co.servicio;

import ceiba.com.co.excepcion.ExcepcionDuplicidad;
import ceiba.com.co.modelo.entidad.Persona;
import ceiba.com.co.puerto.repositorio.RepositorioPersona;

public class ServicioCrearPersona {

    private final RepositorioPersona repositorioPersona;

    public ServicioCrearPersona(RepositorioPersona repositorioPersona) {
        this.repositorioPersona = repositorioPersona;
    }

    public Long ejecutar(Persona persona) {
        if (this.repositorioPersona.existeConCedula(persona.getCedula())) {
            throw new ExcepcionDuplicidad("La cédula ya está registrada: " + persona.getCedula());
        }
        if (this.repositorioPersona.existeConEmail(persona.getEmail())) {
            throw new ExcepcionDuplicidad("El email ya está registrado: " + persona.getEmail());
        }
        return this.repositorioPersona.guardar(persona);
    }
}
