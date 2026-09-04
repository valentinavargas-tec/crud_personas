package ceiba.com.co.doctor.consulta;

public record DtoDoctor(

        String numeroDocumento,
        String nombre,
        String apellido,
        String tarjetaProfesional,
        String especialidad,
        String correoInstitucional,
        boolean habilitado
) {
}
