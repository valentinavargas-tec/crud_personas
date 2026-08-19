DELETE FROM pacientes;

insert into pacientes(numero_documento, tipo_documento, nombre, apellido, fecha_nacimiento, telefono, correo_electronico, eps, genero) 
values(123456789, 'CC', 'Juan', 'Perez', '1990-05-15', '3001234567', 'juan.perez@gmail.com', 'Sura', 'MASCULINO');

insert into pacientes(numero_documento, tipo_documento, nombre, apellido, fecha_nacimiento, telefono, correo_electronico, eps, genero) 
values(987654321, 'CC', 'Maria', 'Gomez', '1985-08-20', '3007654321', 'maria.gomez@example.com', 'Sura', 'FEMENINO');

insert into pacientes(numero_documento, tipo_documento, nombre, apellido, fecha_nacimiento, telefono, correo_electronico, eps, genero) 
values(555555555, 'TI', 'Carlos', 'Alvarez', '2005-12-10', '3009998877', 'carlos.alvarez@example.com', 'Sura', 'MASCULINO');
