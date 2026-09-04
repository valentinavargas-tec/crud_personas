package ceiba.com.co.doctor.comando.fabrica;

import ceiba.com.co.doctor.comando.ComandoDoctor;
import ceiba.com.co.doctor.modelo.entidad.Doctor;
import org.springframework.stereotype.Component;

@Component
public class FabricaDoctor {

    public Doctor crear(ComandoDoctor comandoDoctor) {
        return Doctor.builder()
                .conNumeroDocumento(comandoDoctor.getNumeroDocumento())
                .conNombre(comandoDoctor.getNombre())
                .conApellido(comandoDoctor.getApellido())
                .conTarjetaProfesional(comandoDoctor.getTarjetaProfesional())
                .conEspecialidad(comandoDoctor.getEspecialidad())
                .conCorreoInstitucional(comandoDoctor.getCorreoInstitucional())
                .build();
    }
}
