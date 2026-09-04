package ceiba.com.co.doctor.consulta;

import ceiba.com.co.doctor.puerto.dao.DaoDoctor;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import org.springframework.stereotype.Component;

@Component
public class ManejadorConsultarDoctor {

    private final DaoDoctor daoDoctor;

    public ManejadorConsultarDoctor(DaoDoctor daoDoctor) {
        this.daoDoctor = daoDoctor;
    }

    public DtoDoctor ejecutar(String numeroDocumento) {
        return this.daoDoctor
                .buscarPorNumeroDocumento(numeroDocumento)
                .orElseThrow(() -> new ExcepcionSinDatos(
                        String.format("No existe un doctor con el número de documento %s", numeroDocumento)));
    }
}
