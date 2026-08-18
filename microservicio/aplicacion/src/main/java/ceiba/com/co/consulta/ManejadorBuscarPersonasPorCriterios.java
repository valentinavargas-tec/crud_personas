package ceiba.com.co.consulta;

import ceiba.com.co.modelo.dto.CriteriosBusquedaPersona;
import ceiba.com.co.modelo.dto.Pagina;
import ceiba.com.co.modelo.dto.PersonaDTO;
import ceiba.com.co.puerto.dao.DaoPersona;
import org.springframework.stereotype.Component;

@Component
public class ManejadorBuscarPersonasPorCriterios {

    private final DaoPersona daoPersona;

    public ManejadorBuscarPersonasPorCriterios(DaoPersona daoPersona) {
        this.daoPersona = daoPersona;
    }

    public Pagina<PersonaDTO> ejecutar(CriteriosBusquedaPersona criterios) {
        return this.daoPersona.buscarPorCriterios(criterios);
    }
}
