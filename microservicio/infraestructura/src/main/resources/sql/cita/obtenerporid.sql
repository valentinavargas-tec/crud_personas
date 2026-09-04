SELECT id, paciente_documento, doctor_documento, fecha_hora, tipo_cita, estado, motivo, observaciones
FROM citas
WHERE id = :id;
