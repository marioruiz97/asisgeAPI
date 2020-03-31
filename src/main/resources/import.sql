INSERT INTO tipo_documento(nombre_tipo_documento) VALUES ('Cédula de Ciudadanía');
INSERT INTO tipo_documento(nombre_tipo_documento) VALUES ('NIT');

INSERT INTO estado_proyecto(descripcion, id_estado_anterior, nombre_estado) VALUES ( 'Estado de inicio de todo proyecto', null, 'Creado');
INSERT INTO estado_proyecto(descripcion, id_estado_anterior, nombre_estado) VALUES ( 'Estado siguiente al inicio del proyecto', 1, 'A punto de iniciar');
INSERT INTO estado_proyecto(descripcion, id_estado_anterior, nombre_estado) VALUES ( 'Estado a punto de inicio del proyecto, siguiente a creado', 2, 'Iniciado');
INSERT INTO estado_proyecto(descripcion, id_estado_anterior, nombre_estado) VALUES ( 'Estado de prueba, siguiente al inicio del proyecto', 3, 'Prueba');
INSERT INTO estado_proyecto(descripcion, id_estado_anterior, nombre_estado) VALUES ( 'Estado de un proyecto cuando es pospuesto', 1, 'Pospuesto');

INSERT INTO cliente(identificacion, nombre_comercial, razon_social, id_tipo_documento) VALUES ( '900876765', 'Asisge', 'Asistencia Gerencial Estrategica SAS', 2);
INSERT INTO usuario(apellido1, apellido2, contrasena, correo, estado, identificacion, nombre, telefono, id_tipo_documento) VALUES ('Ruiz', 'Bedoya', '123456', 'marioarb.97@gmail.com', true, '1017251545', 'Mario Andres', '3015465076', 1);

INSERT INTO cliente(identificacion, nombre_comercial, razon_social, id_tipo_documento) VALUES ( '900876761', 'Dian', 'Direccion de impuestos SAS', 2);
INSERT INTO usuario(apellido1, apellido2, contrasena, correo, estado, identificacion, nombre, telefono, id_tipo_documento) VALUES ('Ruiz', 'Bedoya', '123456', 'carito.93@gmail.com', true, '1017214122', 'Carito', '3046307654', 1);

INSERT INTO cliente(identificacion, nombre_comercial, razon_social, id_tipo_documento) VALUES ( '900876762', 'S4DS', 'solutions for dealers SAS', 2);
INSERT INTO usuario(apellido1, apellido2, contrasena, correo, estado, identificacion, nombre, telefono, id_tipo_documento) VALUES ('Bedoya', 'Jaramillo', '123456', 'gloriae.61@gmail.com', true, '43032118', 'Gloria', '3007855211', 1);

INSERT INTO cliente(identificacion, nombre_comercial, razon_social, id_tipo_documento) VALUES ( '900876763', 'Joyeria inter', 'Joyeria intercontinental dali SAS', 2);
INSERT INTO usuario(apellido1, apellido2, contrasena, correo, estado, identificacion, nombre, telefono, id_tipo_documento) VALUES ('Ruiz', 'Giraldo', '123456', 'jotica.65@gmail.com', true, '71665023', 'Juan Mario', '3006521212', 1);

INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (1, 1);
INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (1, 2);
INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (1, 3);
INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (1, 4);
INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (2, 1);
INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (3, 2);
INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (4, 3);
INSERT INTO usuario_cliente(id_usuario, id_cliente) VALUES (4, 4);


INSERT INTO proyecto( descripcion_general, fecha_cierre_proyecto, nombre_proyecto, id_cliente, estado_proyecto_id) VALUES ( 'Proyecto de prueba', '2020-04-01T14:30:00.000-0500', 'test project', 1, 1);
INSERT INTO miembro_proyecto(rol_proyecto, id_proyecto, id_usuario) VALUES ('admin', 1, 1);
INSERT INTO miembro_proyecto(rol_proyecto, id_proyecto, id_usuario) VALUES ('encargado cliente', 1, 2);
INSERT INTO miembro_proyecto(rol_proyecto, id_proyecto, id_usuario) VALUES ('asesor', 1, 3);
INSERT INTO miembro_proyecto(rol_proyecto, id_proyecto, id_usuario) VALUES ('reader', 1, 4);

INSERT INTO plan_de_trabajo(duracion, fecha_fin_estimada, fecha_fin_real, fecha_inicio, horas_mes, objetivo_plan, etapa_actual, id_proyecto)	VALUES (10, '2021-03-30T00:00.000-0500', '2021-03-30T00:00.000-0500', '2020-04-01T00:00.000-0500', 12, 'objetivo', null, 1);
INSERT INTO etapa_plan_trabajo(fecha_fin, fecha_inicio, nombre_etapa, id_plan_de_trabajo) VALUES ('2021-03-30T00:00.000-0500', '2020-04-03T00:00.000-0500', 'etapa inicial', 1);
UPDATE plan_de_trabajo SET etapa_actual = 1 WHERE id_plan_de_trabajo = 1;

INSERT INTO etapa_plan_trabajo(fecha_fin, fecha_inicio, nombre_etapa, id_plan_de_trabajo) VALUES ('2022-03-30T00:00.000-0500', '2020-12-01T00:00.000-0500', 'etapa 3', 1);
INSERT INTO etapa_plan_trabajo(fecha_fin, fecha_inicio, nombre_etapa, id_plan_de_trabajo) VALUES ('2022-04-30T00:00.000-0500', '2022-04-01T00:00.000-0500', 'etapa 4', 1);
INSERT INTO etapa_plan_trabajo(fecha_fin, fecha_inicio, nombre_etapa, id_plan_de_trabajo) VALUES ('2021-05-30T00:00.000-0500', '2020-04-03T00:00.000-0500', 'etapa 2', 1);
