SELECT
    cedula,
    nombre,
    apellido,
    email,
    fecha_nacimiento
FROM
    personas
WHERE
    cedula = :cedula;
