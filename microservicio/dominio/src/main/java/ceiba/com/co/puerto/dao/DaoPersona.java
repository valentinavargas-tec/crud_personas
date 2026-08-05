package ceiba.com.co.puerto.dao;

import ceiba.com.co.modelo.dto.CriteriosBusquedaPersona;
import ceiba.com.co.modelo.dto.Pagina;
import ceiba.com.co.modelo.dto.PersonaDTO;
import java.util.List;

public interface DaoPersona {

    List<PersonaDTO> listar();
    PersonaDTO buscarPorCedula(Long cedula);
    Pagina<PersonaDTO> buscarPorCriterios(CriteriosBusquedaPersona criterios);
}
