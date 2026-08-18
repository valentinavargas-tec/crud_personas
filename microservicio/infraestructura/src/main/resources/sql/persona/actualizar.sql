UPDATE personas
SET
    nombre = :nombre,
    apellido = :apellido,
    email = :email,
    fecha_nacimiento = :fechaNacimiento
WHERE
    cedula = :cedula;
