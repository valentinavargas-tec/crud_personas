INSERT INTO pacientes (
    numero_documento, tipo_documento, nombre, apellido, fecha_nacimiento, telefono, correo_electronico, eps, genero
) VALUES (
    :numeroDocumento, :tipoDocumento, :nombre, :apellido, :fechaNacimiento, :telefono, :correoElectronico, :eps, :genero
);
