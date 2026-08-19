package ceiba.com.co.paciente.consulta;

import ceiba.com.co.paciente.modelo.dto.CriteriosBusquedaPaciente;
import ceiba.com.co.paciente.modelo.dto.Pagina;
import ceiba.com.co.paciente.modelo.dto.PacienteDTO;
import ceiba.com.co.paciente.puerto.dao.DaoPaciente;
import org.springframework.stereotype.Component;

@Component
public class ManejadorBuscarPacientesPorCriterios {

    private final DaoPaciente daoPaciente;

    public ManejadorBuscarPacientesPorCriterios(DaoPaciente daoPaciente) {
        this.daoPaciente = daoPaciente;
    }

    public Pagina<PacienteDTO> ejecutar(CriteriosBusquedaPaciente criterios) {
        return this.daoPaciente.buscarPorCriterios(criterios);
    }
}
