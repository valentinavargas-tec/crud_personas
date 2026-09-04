SELECT id, paciente_documento AS pacienteDocumento, doctor_documento AS doctorDocumento, 
       fecha_hora AS fechaHora, tipo_cita AS tipoCita, estado, motivo
FROM citas
WHERE doctor_documento = :doctorDocumento
  AND DATE(fecha_hora) = :fecha
ORDER BY fecha_hora ASC;
