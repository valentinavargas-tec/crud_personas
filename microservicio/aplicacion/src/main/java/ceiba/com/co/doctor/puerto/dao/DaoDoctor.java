package ceiba.com.co.doctor.puerto.dao;

import ceiba.com.co.doctor.consulta.DtoDoctor;
import java.util.List;
import java.util.Optional;

public interface DaoDoctor {

    List<DtoDoctor> listarTodos();
    List<DtoDoctor> buscarPorEspecialidad(String especialidad);
    Optional<DtoDoctor> buscarPorNumeroDocumento(String numeroDocumento);
}
