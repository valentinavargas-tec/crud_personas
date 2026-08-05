package ceiba.com.co.consulta;

import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.modelo.dto.PersonaDTO;
import ceiba.com.co.puerto.dao.DaoPersona;
import org.springframework.stereotype.Component;

@Component
public class ManejadorBuscarPersonaPorCedula {

    private final DaoPersona daoPersona;

    public ManejadorBuscarPersonaPorCedula(DaoPersona daoPersona) {
        this.daoPersona = daoPersona;
    }

    public PersonaDTO ejecutar(Long cedula) {
        PersonaDTO persona = this.daoPersona.buscarPorCedula(cedula);
        if (persona == null) {
            throw new ExcepcionSinDatos("Persona no encontrada");
        }
        return persona;
    }
}

