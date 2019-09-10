CREATE DATABASE vuelos;

USE vuelos;

CREATE TABLE ubicaciones (
	pais VARCHAR(45) NOT NULL,
	estado VARCHAR(45) NOT NULL,
	ciudad VARCHAR(45) NOT NULL,
	huso SMALLINT UNSIGNED NOT NULL CHECK(huso BETWEEN -12 AND 12),

	CONSTRAINT pk_ubicaiones PRIMARY KEY (pais, estado, ciudad)
)

CREATE TABLE aeropuetos (
	codigo VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	pais VARCHAR(45) NOT NULL,
	estado VARCHAR(45) NOT NULL,
	ciudad VARCHAR(45) NOT NULL,

	CONSTRAINT pk_aeropuerto PRIMARY KEY (codigo),
	CONSTRAINT fk_aeropuerto_ubicacion FOREIGN KEY (pais, estado, ciudad) REFERENCES ubicaciones(pais, estado, ciudad)
)

CREATE TABLE vuelos_programados (
	numero VARCHAR(45) NOT NULL,
	aeropueto_salida VARCHAR(45) NOT NULL,
	aeropueto_llegada NOT NULL,

	CONSTRAINT pk_vuelos_programados PRIMARY KEY (numero),
	CONSTRAINT fk_vuelos_programados_aeropuerto_salida FOREIGN KEY (aeropueto_salida) REFERENCES aeropuetos(codigo),
	CONSTRAINT fk_vuelos_programados_aeropuerto_llegada FOREIGN KEY (aeropueto_llegada) REFERENCES aeropuetos(codigo)
)ENGINE = InnoDB;

CREATE TABLE modelo_avion (
	modelo VARCHAR(45) NOT NULL,
	fabricante VARCHAR(45) NOT NULL,
	cabinas SMALLINT UNSIGNED NOT NULL,
	cant_asientos SMALLINT UNSIGNED NOT NULL,

	CONSTRAINT pk_modelo_avion PRIMARY KEY (modelo)
)ENGINE = InnoDB;

CREATE TABLE instacias_vuelo(
	vuelo VARCHAR(45) NOT NULL,
	fecha DATE NOT NULL,
	dia VARCHAR(2) NOT NULL CHECK (dia IN ('Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa')),
	estado VARCHAR(45) NOT NULL,

	CONSTRAINT pk_instacias_vuelo PRIMARY KEY (vuelo, fecha),
	CONSTRAINT fk_vuelo_dia_instancia_vuelo FOREIGN KEY (vuelo, dia) REFERENCES salidas(vuelo, dia)
)


CREATE TABLE salidas (
	vuelo VARCHAR(45) NOT NULL,
	dia VARCHAR(2) NOT NULL CHECK (dia IN ('Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa')), #### va NOT NULL?
	hora_sale TIME NOT NULL,
	hora_llegada TIME NOT NULL,
	modelo_avion  VARCHAR(45) NOT NULL,

	CONSTRAINT pk_salidas PRIMARY KEY (vuelo, dia),
	CONSTRAINT fk_salida_vuelo FOREIGN KEY (vuelo) REFERENCES vuelos_programados(numero),
	CONSTRAINT fk_salida_modelos_avion FOREIGN KEY (modelo_avion) REFERENCES  modelo_avion(modelo)
)ENGINE = InnoDB;

CREATE TABLE clases (
	nombre VARCHAR(45) NOT NULL,
	porcentaje DECIMAL NOT NULL CHECK(porcentaje BETWEEN 0.00 AND 0.99),

	CONSTRAINT pk_clases PRIMARY KEY (nombre)
)

CREATE TABLE comodidades (
	codigo INT UNSIGNED NOT NULL,
	descripcion VARCHAR(45) NOT NULL,

	CONSTRAINT pk_comodidades PRIMARY KEY (codigo)
)

CREATE TABLE pasajeros (
	doc_tipo VARCHAR(45) NOT NULL,
	doc_numero INT UNSIGNED NOT NULL,
	apellido VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	nacionalidad VARCHAR(45) NOT NULL,

	CONSTRAINT PK_pasajeros PRIMARY KEY (doc_tipo, doc_numero)
)

CREATE TABLE empleados (
	legajo INT UNSIGNED NOT NULL,
	password VARCHAR(32) NOT NULL,
	doc_tipo VARCHAR(45) NOT NULL,
	doc_numero INT UNSIGNED NOT NULL,
	apellido VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,

	CONSTRAINT pk_empleados PRIMARY KEY (legajo)
)

CREATE TABLE reservas(
	numero INT UNSIGNED NOT NULL,
	fecha DATE NOT NULL,
	vencimiento DATE NOT NULL,
	estado VARCHAR(45) NOT NULL,
	doc_tipo VARCHAR(45) NOT NULL,
	doc_numero INT UNSIGNED NOT NULL,
	legajo INT UNSIGNED NOT NULL,

	CONSTRAINT pk_reservas  PRIMARY KEY (numero),
	CONSTRAINT fk_doc_tipo_numero_reservas FOREIGN KEY (doc_tipo, doc_numero) REFERENCES pasajeros(doc_tipo, doc_numero),
	CONSTRAINT fk_legajo_reservas PRIMARY KEY (legajo) REFERENCES empleados(legajo)
)

CREATE TABLE brinda(
	vuelo VARCHAR(45) NOT NULL,
	dia VARCHAR(2) NOT NULL CHECK (dia IN ('Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa')),
	clase VARCHAR(45) NOT NULL,
	precio DECIMAL(7,2) NOT NULL,
	cant_asientos SMALLINT UNSIGNED NOT NULL,

	CONSTRAINT pk_brinda PRIMARY KEY (vuelo, dia, clase),
	CONSTRAINT fk_vuelo_dia_brinda FOREIGN KEY (vuelo, dia) REFERENCES salidas(vuelo, dia),
	CONSTRAINT fk_clase_brinda FOREIGN KEY (clase) REFERENCES clases(nombre)
)

CREATE TABLE posee (
	clase VARCHAR(45) NOT NULL,
	comodidad INT UNSIGNED NOT NULL,

	CONSTRAINT pk_posee PRIMARY KEY (clase, comodidad),
	CONSTRAINT fk_clase_posee FOREIGN KEY (clase) REFERENCES clases(nombre),
	CONSTRAINT fk_comodidad_posee FOREIGN KEY (comodidad) REFERENCES comodidad(codigo)
)

CREATE TABLE reserva_vuelo_clase (
	numero INT UNSIGNED NOT NULL,
	vuelo VARCHAR(45) NOT NULL,
	fecha_vuelo DATE NOT NULL,
	clase VARCHAR(45) NOT NULL,

	CONSTRAINT pk_reserva_vuelo_clase PRIMARY KEY (numero, vuelo, fecha_vuelo),
	CONSTRAINT fk_numero_reserva_vuelo_clase FOREIGN KEY (numero) REFERENCES reservas(numero),
	CONSTRAINT fk_vuelo_fecha_vuelo_reserva_vuelo_clase FOREIGN KEY (vuelo, fecha_vuelo) REFERENCES instacias_vuelo(vuelo, fecha),
	CONSTRAINT fk_clase_reserva_vuelo_clase FOREIGN KEY (clase) REFERENCES clases(nombre)
)