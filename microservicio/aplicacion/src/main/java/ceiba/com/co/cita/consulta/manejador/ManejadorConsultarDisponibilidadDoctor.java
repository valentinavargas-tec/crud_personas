package ceiba.com.co.cita.consulta.manejador;

import ceiba.com.co.cita.consulta.DtoDisponibilidadDoctor;
import ceiba.com.co.cita.puerto.dao.DaoCita;
import ceiba.com.co.doctor.puerto.repositorio.RepositorioDoctor;
import ceiba.com.co.excepcion.ExcepcionSinDatos;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ManejadorConsultarDisponibilidadDoctor {

    private static final LocalTime HORA_INICIO = LocalTime.of(8, 0); // 08:00
    private static final LocalTime HORA_FIN = LocalTime.of(18, 0);   // 18:00
    
    private final DaoCita daoCita;
    private final RepositorioDoctor repositorioDoctor;

    public ManejadorConsultarDisponibilidadDoctor(DaoCita daoCita, RepositorioDoctor repositorioDoctor) {
        this.daoCita = daoCita;
        this.repositorioDoctor = repositorioDoctor;
    }

    public DtoDisponibilidadDoctor ejecutar(String doctorDocumento, LocalDate fecha) {
        if (this.repositorioDoctor.obtenerPorNumeroDocumento(doctorDocumento).isEmpty()) {
            throw new ExcepcionSinDatos(String.format("El doctor %s no existe.", doctorDocumento));
        }

        List<LocalTime> horasOcupadas = this.daoCita.buscarCitasDoctorPorFecha(doctorDocumento, fecha)
                .stream()
                .filter(c -> !c.estado().equals("CANCELADA") && !c.estado().equals("COMPLETADA"))
                .map(c -> c.fechaHora().toLocalTime())
                .collect(Collectors.toList());

        List<LocalTime> franjasLibres = new ArrayList<>();
        LocalTime horaActual = HORA_INICIO;
        
        while (horaActual.isBefore(HORA_FIN)) {
            if (!horasOcupadas.contains(horaActual)) {
                franjasLibres.add(horaActual);
            }
            horaActual = horaActual.plusHours(1);
        }

        return new DtoDisponibilidadDoctor(doctorDocumento, fecha, franjasLibres);
    }
}
