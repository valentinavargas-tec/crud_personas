package ceiba.com.co.paciente.consulta;

import ceiba.com.co.paciente.modelo.dto.PacienteDTO;
import ceiba.com.co.paciente.puerto.dao.DaoPaciente;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManejadorListarPacientes {

    private final DaoPaciente daoPaciente;

    public ManejadorListarPacientes(DaoPaciente daoPaciente) {
        this.daoPaciente = daoPaciente;
    }

    public List<PacienteDTO> ejecutar() {
        return this.daoPaciente.listar();
    }
}
