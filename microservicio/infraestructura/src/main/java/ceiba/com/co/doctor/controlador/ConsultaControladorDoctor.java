package ceiba.com.co.doctor.controlador;

import ceiba.com.co.doctor.consulta.DtoDoctor;
import ceiba.com.co.doctor.consulta.ManejadorBuscarDoctoresPorEspecialidad;
import ceiba.com.co.doctor.consulta.ManejadorConsultarDoctor;
import ceiba.com.co.doctor.consulta.ManejadorListarDoctores;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/doctores")
public class ConsultaControladorDoctor {

    private final ManejadorConsultarDoctor manejadorConsultarDoctor;
    private final ManejadorListarDoctores manejadorListarDoctores;
    private final ManejadorBuscarDoctoresPorEspecialidad manejadorBuscarDoctoresPorEspecialidad;

    public ConsultaControladorDoctor(ManejadorConsultarDoctor manejadorConsultarDoctor, ManejadorListarDoctores manejadorListarDoctores, ManejadorBuscarDoctoresPorEspecialidad manejadorBuscarDoctoresPorEspecialidad) {
        this.manejadorConsultarDoctor = manejadorConsultarDoctor;
        this.manejadorListarDoctores = manejadorListarDoctores;
        this.manejadorBuscarDoctoresPorEspecialidad = manejadorBuscarDoctoresPorEspecialidad;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<DtoDoctor> listar() {
        return this.manejadorListarDoctores.ejecutar();
    }

    @GetMapping("/especialidad/{especialidad}")
    @ResponseStatus(HttpStatus.OK)
    public List<DtoDoctor> buscarPorEspecialidad(@PathVariable ("especialidad") String especialidad) {
        return this.manejadorBuscarDoctoresPorEspecialidad.ejecutar(especialidad);
    }

    @GetMapping("/{numeroDocumento}")
    @ResponseStatus(HttpStatus.OK)
    public DtoDoctor obtenerPorNumeroDocumento(@PathVariable ("numeroDocumento") String numeroDocumento) {
        return this.manejadorConsultarDoctor.ejecutar(numeroDocumento);
    }
}
