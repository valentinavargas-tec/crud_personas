SELECT id, paciente_documento AS pacienteDocumento, doctor_documento AS doctorDocumento, 
       fecha_hora AS fechaHora, tipo_cita AS tipoCita, estado, motivo
FROM citas
WHERE paciente_documento = :pacienteDocumento
ORDER BY fecha_hora DESC;
