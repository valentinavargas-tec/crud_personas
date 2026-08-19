UPDATE pacientes
SET nombre = :nombre,
    apellido = :apellido,
    fecha_nacimiento = :fechaNacimiento,
    telefono = :telefono,
    correo_electronico = :correoElectronico,
    eps = :eps,
    genero = :genero
WHERE numero_documento = :numeroDocumento;
