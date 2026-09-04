SELECT COUNT(*)
FROM doctores
WHERE correo_institucional = :correoInstitucional
  AND numero_documento != :numeroDocumento;
