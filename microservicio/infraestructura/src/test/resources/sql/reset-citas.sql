DELETE FROM citas;
DELETE FROM doctores;
DELETE FROM pacientes;

INSERT INTO pacientes(numero_documento, tipo_documento, nombre, apellido, fecha_nacimiento, telefono, correo_electronico, eps, genero)
VALUES (1024887449, 'CC', 'Juan', 'Perez', '1990-05-15', '3001234567', 'juan.perez@gmail.com', 'Sura', 'MASCULINO');

INSERT INTO pacientes(numero_documento, tipo_documento, nombre, apellido, fecha_nacimiento, telefono, correo_electronico, eps, genero)
VALUES (987654321, 'CC', 'Maria', 'Gomez', '1995-08-20', '3009876543', 'maria.gomez@gmail.com', 'Sanitas', 'FEMENINO');

INSERT INTO doctores (numero_documento, nombre, apellido, tarjeta_profesional, especialidad, correo_institucional, habilitado)
VALUES ('21754896', 'Ana', 'Torres', 'TP-001', 'CARDIOLOGIA', 'ana.torres@hospital.com', true);

INSERT INTO doctores (numero_documento, nombre, apellido, tarjeta_profesional, especialidad, correo_institucional, habilitado)
VALUES ('79123456', 'Carlos', 'Gomez', 'TP-002', 'CARDIOLOGIA', 'carlos.gomez@hospital.com', true);

INSERT INTO doctores (numero_documento, nombre, apellido, tarjeta_profesional, especialidad, correo_institucional, habilitado)
VALUES ('80234567', 'Luis', 'Ramos', 'TP-003', 'PEDIATRIA', 'luis.ramos@hospital.com', true);

INSERT INTO doctores (numero_documento, nombre, apellido, tarjeta_profesional, especialidad, correo_institucional, habilitado)
VALUES ('91234567', 'Maria', 'Castro', 'TP-004', 'CARDIOLOGIA', 'maria.castro@hospital.com', false);

INSERT INTO doctores (numero_documento, nombre, apellido, tarjeta_profesional, especialidad, correo_institucional, habilitado)
VALUES ('92345678', 'Pedro', 'Rios', 'TP-005', 'CARDIOLOGIA', 'pedro.rios@hospital.com', true);

INSERT INTO citas (id, paciente_documento, doctor_documento, fecha_hora, tipo_cita, estado, motivo, observaciones)
VALUES (1, 1024887449, '21754896', '2028-10-10 10:00:00', 'CONSULTA_GENERAL', 'PROGRAMADA', 'Consulta de rutina', NULL);

INSERT INTO citas (id, paciente_documento, doctor_documento, fecha_hora, tipo_cita, estado, motivo, observaciones)
VALUES (2, 1024887449, '21754896', '2028-10-15 14:00:00', 'CONSULTA_GENERAL', 'CANCELADA', 'Cita previa cancelada', NULL);

INSERT INTO citas (id, paciente_documento, doctor_documento, fecha_hora, tipo_cita, estado, motivo, observaciones)
VALUES (3, 1024887449, '80234567', '2028-10-20 16:00:00', 'ESPECIALIZADA', 'PROGRAMADA', 'Cita existente que genera conflicto', NULL);

INSERT INTO citas (id, paciente_documento, doctor_documento, fecha_hora, tipo_cita, estado, motivo, observaciones)
VALUES (4, 987654321, '92345678', '2028-10-10 10:00:00', 'CONSULTA_GENERAL', 'PROGRAMADA', 'Cita doctor pedro en horario de cita 1', NULL);
