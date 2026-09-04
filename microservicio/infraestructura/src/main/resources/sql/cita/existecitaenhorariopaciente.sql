SELECT COUNT(1)
FROM citas
WHERE paciente_documento = :pacienteDocumento
  AND fecha_hora = :fechaHora
  AND estado NOT IN ('CANCELADA', 'COMPLETADA');
