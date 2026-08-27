-- Esquema real segun el diagrama entidad-relacion: estado, cliente, tecnico,
-- tipo_servicio y orden. Corre UNA sola vez, cuando se crea el volumen de Oracle.
--
-- Igual que antes: nos conectamos como telco_user para que las tablas queden
-- en su esquema (si no, corren como SYS y la app nunca las encuentra: ORA-00942).
CONNECT telco_user/telco_pw123@//localhost/FREEPDB1

-- Orden de creacion importa: estado primero (todo lo demas la referencia),
-- luego los catalogos que dependen solo de estado, y "orden" al final porque
-- depende de los otros cuatro.

CREATE TABLE estado (
    id_estado       NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_estado   VARCHAR2(50) NOT NULL
);

CREATE TABLE cliente (
    id_cliente      NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    numero_iden     VARCHAR2(20)  NOT NULL,
    nombre_titular  VARCHAR2(150) NOT NULL,
    direccion       VARCHAR2(250),
    telefono        VARCHAR2(20),
    fk_estado       NUMBER NOT NULL,
    CONSTRAINT fk_cliente_estado FOREIGN KEY (fk_estado) REFERENCES estado (id_estado)
);

CREATE TABLE tecnico (
    id_tecnico      NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    no_tecnico      VARCHAR2(20)  NOT NULL,
    nombre          VARCHAR2(150) NOT NULL,
    fk_estado       NUMBER NOT NULL,
    CONSTRAINT fk_tecnico_estado FOREIGN KEY (fk_estado) REFERENCES estado (id_estado)
);

CREATE TABLE tipo_servicio (
    id_tipo_ser     NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    nombre_servicio VARCHAR2(100) NOT NULL,
    fk_estado       NUMBER NOT NULL,
    CONSTRAINT fk_tiposervicio_estado FOREIGN KEY (fk_estado) REFERENCES estado (id_estado)
);

CREATE TABLE orden (
    id_orden          NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    date_created      TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
    fk_estado         NUMBER NOT NULL,
    fk_cliente        NUMBER NOT NULL,
    fk_tecnico        NUMBER,              -- puede quedar sin asignar al crearse
    fk_tipo_servicio  NUMBER NOT NULL,
    CONSTRAINT fk_orden_estado        FOREIGN KEY (fk_estado)        REFERENCES estado (id_estado),
    CONSTRAINT fk_orden_cliente       FOREIGN KEY (fk_cliente)       REFERENCES cliente (id_cliente),
    CONSTRAINT fk_orden_tecnico       FOREIGN KEY (fk_tecnico)       REFERENCES tecnico (id_tecnico),
    CONSTRAINT fk_orden_tiposervicio  FOREIGN KEY (fk_tipo_servicio) REFERENCES tipo_servicio (id_tipo_ser)
);

CREATE TABLE historial (
    id_registro       NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    fk_orden          NUMBER NOT NULL,
    fk_estado_anterior    NUMBER,
    fk_estado_nuevo         NUMBER,
    date_created      TIMESTAMP DEFAULT SYSTIMESTAMP NOT NULL,
                       date_update      NOT NULL,
                       CONSTRAINT fk_historial_orden       FOREIGN KEY (fk_orden)        REFERENCES estado (id_orden),
                       CONSTRAINT fk_historial_estado_anterior        FOREIGN KEY (fk_estado_anterior)        REFERENCES estado (id_estado),
                       CONSTRAINT fk_historial_estado_nuevo       FOREIGN KEY (fk_estado_nuevo)       REFERENCES cliente (id_estado),
);

#COMENTARIO

INSERT INTO estado (nombre_estado) VALUES ('CREATED');
INSERT INTO estado (nombre_estado) VALUES ('VALIDATED');
INSERT INTO estado (nombre_estado) VALUES ('APPROVED');
INSERT INTO estado (nombre_estado) VALUES ('IN_PROGRESS');
INSERT INTO estado (nombre_estado) VALUES ('COMPLETED');
INSERT INTO estado (nombre_estado) VALUES ('CANCELLED');

INSERT INTO cliente (numero_iden, nombre_titular, direccion, telefono, fk_estado)
VALUES ('1234567890101', 'Cliente de Prueba', 'Zona 1, Guatemala', '50255551234', 1);

INSERT INTO tecnico (no_tecnico, nombre, fk_estado)
VALUES ('TEC-001', 'Tecnico de Prueba', 1);

INSERT INTO tipo_servicio (nombre_servicio, fk_estado)
VALUES ('INTERNET', 1);

INSERT INTO orden (fk_estado, fk_cliente, fk_tecnico, fk_tipo_servicio)
VALUES (2, 1, 1, 1);

INSERT INTO historial (fk_orden, fk_estado_anterior, fk_estado_nuevo)
VALUES (1, null, 2)

COMMIT;
