SELECT numero_documento,
       nombre,
       apellido,
       tarjeta_profesional,
       especialidad,
       correo_institucional,
       habilitado
FROM doctores
WHERE numero_documento = :numeroDocumento;
