package ceiba.com.co.doctor.consulta;

import ceiba.com.co.doctor.puerto.dao.DaoDoctor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ManejadorListarDoctores {

    private final DaoDoctor daoDoctor;

    public ManejadorListarDoctores(DaoDoctor daoDoctor) {
        this.daoDoctor = daoDoctor;
    }

    public List<DtoDoctor> ejecutar() {
        return this.daoDoctor.listarTodos();
    }
}
