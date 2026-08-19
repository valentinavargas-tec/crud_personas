package ceiba.com.co.paciente.consulta;

import ceiba.com.co.excepcion.ExcepcionSinDatos;
import ceiba.com.co.paciente.modelo.dto.PacienteDTO;
import ceiba.com.co.paciente.puerto.dao.DaoPaciente;
import org.springframework.stereotype.Component;

@Component
public class ManejadorBuscarPacientePorNumeroDocumento {

    private final DaoPaciente daoPaciente;

    public ManejadorBuscarPacientePorNumeroDocumento(DaoPaciente daoPaciente) {
        this.daoPaciente = daoPaciente;
    }

    public PacienteDTO ejecutar(Long numeroDocumento) {
        PacienteDTO paciente = this.daoPaciente.buscarPorNumeroDocumento(numeroDocumento);
        if (paciente == null) {
            throw new ExcepcionSinDatos("Paciente no encontrado");
        }
        return paciente;
    }
}
