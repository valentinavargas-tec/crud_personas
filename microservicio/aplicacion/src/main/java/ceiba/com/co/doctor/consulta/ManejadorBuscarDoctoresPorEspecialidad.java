package ceiba.com.co.doctor.consulta;

import ceiba.com.co.doctor.puerto.dao.DaoDoctor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManejadorBuscarDoctoresPorEspecialidad {

    private final DaoDoctor daoDoctor;

    public ManejadorBuscarDoctoresPorEspecialidad(DaoDoctor daoDoctor) {
        this.daoDoctor = daoDoctor;
    }

    public List<DtoDoctor> ejecutar(String especialidad) {
        return this.daoDoctor.buscarPorEspecialidad(especialidad);
    }
}
