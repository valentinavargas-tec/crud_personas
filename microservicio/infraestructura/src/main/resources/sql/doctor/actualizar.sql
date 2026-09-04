UPDATE doctores
SET nombre              = :nombre,
    apellido            = :apellido,
    especialidad        = :especialidad,
    correo_institucional = :correoInstitucional
WHERE numero_documento  = :numeroDocumento;
