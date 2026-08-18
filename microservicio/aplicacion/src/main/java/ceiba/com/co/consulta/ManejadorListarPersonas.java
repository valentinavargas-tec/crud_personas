package ceiba.com.co.consulta;

import ceiba.com.co.modelo.dto.PersonaDTO;
import ceiba.com.co.puerto.dao.DaoPersona;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManejadorListarPersonas {

    private final DaoPersona daoPersona;

    public ManejadorListarPersonas(DaoPersona daoPersona) {
        this.daoPersona = daoPersona;
    }

    public List<PersonaDTO> ejecutar() {
        return this.daoPersona.listar();
    }
}
