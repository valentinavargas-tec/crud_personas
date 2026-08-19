package ceiba.com.co.paciente.puerto.dao;

import ceiba.com.co.paciente.modelo.dto.CriteriosBusquedaPaciente;
import ceiba.com.co.paciente.modelo.dto.Pagina;
import ceiba.com.co.paciente.modelo.dto.PacienteDTO;
import java.util.List;

public interface DaoPaciente {

    List<PacienteDTO> listar();
    PacienteDTO buscarPorNumeroDocumento(Long numeroDocumento);
    Pagina<PacienteDTO> buscarPorCriterios(CriteriosBusquedaPaciente criterios);
}
