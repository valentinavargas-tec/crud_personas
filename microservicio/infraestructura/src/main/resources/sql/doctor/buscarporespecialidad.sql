SELECT numero_documento,
       nombre,
       apellido,
       tarjeta_profesional,
       especialidad,
       correo_institucional,
       habilitado
FROM doctores
WHERE UPPER(especialidad) = UPPER(:especialidad)
  AND habilitado = true;
