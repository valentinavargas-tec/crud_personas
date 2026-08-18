package ceiba.com.co.servicio;

import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.puerto.repositorio.RepositorioPersona;

public class ServicioEliminarPersona {

    private final RepositorioPersona repositorioPersona;

    public ServicioEliminarPersona(RepositorioPersona repositorioPersona) {
        this.repositorioPersona = repositorioPersona;
    }

    public void ejecutar(Long cedula) {
        if (!this.repositorioPersona.existeConCedula(cedula)) {
            throw new ExcepcionSinDatos("No existe una persona con la cédula ingresada: " + cedula);
        }
        this.repositorioPersona.eliminar(cedula);
    }
}
