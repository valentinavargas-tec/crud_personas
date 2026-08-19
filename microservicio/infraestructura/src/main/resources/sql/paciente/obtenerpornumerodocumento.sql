SELECT numero_documento, tipo_documento, nombre, apellido, fecha_nacimiento, telefono, correo_electronico, eps, genero
FROM pacientes
WHERE numero_documento = :numeroDocumento;
