SELECT COUNT(1)
FROM citas
WHERE doctor_documento = :doctorDocumento
  AND fecha_hora = :fechaHora
  AND id <> :idCita
  AND estado NOT IN ('CANCELADA', 'COMPLETADA');
