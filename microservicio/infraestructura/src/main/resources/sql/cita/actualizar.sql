UPDATE citas
SET doctor_documento = :doctorDocumento,
    fecha_hora = :fechaHora,
    tipo_cita = :tipoCita,
    estado = :estado,
    motivo = :motivo,
    observaciones = :observaciones
WHERE id = :id;
