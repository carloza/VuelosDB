#-------------------------------------------------------------------------
# Creación base de datos
CREATE DATABASE vuelos;

USE vuelos;

#-------------------------------------------------------------------------
# Creación Tablas para las entidades

CREATE TABLE ubicaciones (
	pais VARCHAR(45) NOT NULL,
	estado VARCHAR(45) NOT NULL,
	ciudad VARCHAR(45) NOT NULL,
	huso SMALLINT UNSIGNED NOT NULL CHECK(huso BETWEEN -12 AND 12),
	
	CONSTRAINT pk_ubicaciones PRIMARY KEY (pais, estado, ciudad)
)ENGINE=InnoDB;

CREATE TABLE aeropuertos (
	codigo VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	pais VARCHAR(45) NOT NULL,
	estado VARCHAR(45) NOT NULL,
	ciudad VARCHAR(45) NOT NULL,

	CONSTRAINT pk_aeropuerto PRIMARY KEY (codigo),
	CONSTRAINT fk_aeropuerto_ubicacion FOREIGN KEY (pais, estado, ciudad) REFERENCES ubicaciones(pais, estado, ciudad)
)ENGINE = InnoDB;

CREATE TABLE vuelos_programados (
	numero VARCHAR(45) NOT NULL,
	aeropuerto_salida VARCHAR(45) NOT NULL,
	aeropuerto_llegada VARCHAR(45) NOT NULL,

	CONSTRAINT pk_vuelos_programados PRIMARY KEY (numero),
	CONSTRAINT fk_vuelos_programados_aeropuerto_salida FOREIGN KEY (aeropuerto_salida) REFERENCES aeropuertos(codigo),
	CONSTRAINT fk_vuelos_programados_aeropuerto_llegada FOREIGN KEY(aeropuerto_llegada) REFERENCES aeropuertos(codigo)
)ENGINE = InnoDB;

CREATE TABLE modelos_avion (
	modelo VARCHAR(45) NOT NULL,
	fabricante VARCHAR(45) NOT NULL,
	cabinas SMALLINT UNSIGNED NOT NULL,
	cant_asientos SMALLINT UNSIGNED NOT NULL,

	CONSTRAINT pk_modelo_avion PRIMARY KEY (modelo)
)ENGINE = InnoDB;

CREATE TABLE salidas (
	vuelo VARCHAR(45) NOT NULL,
	dia VARCHAR(2) NOT NULL CHECK (dia IN ('Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa')), #### va NOT NULL?
	hora_sale TIME NOT NULL,
	hora_llega TIME NOT NULL,
	modelo_avion VARCHAR(45) NOT NULL,

	CONSTRAINT pk_salidas PRIMARY KEY (vuelo, dia),
	CONSTRAINT fk_salida_vuelo FOREIGN KEY (vuelo) REFERENCES vuelos_programados(numero),
	CONSTRAINT fk_salida_modelos_avion FOREIGN KEY (modelo_avion) REFERENCES  modelos_avion(modelo)
)ENGINE = InnoDB;

CREATE TABLE instancias_vuelo(
	vuelo VARCHAR(45) NOT NULL,
	fecha DATE NOT NULL,
	dia VARCHAR(2) NOT NULL CHECK (dia IN ('Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa')),
	estado VARCHAR(45) NOT NULL,

	CONSTRAINT pk_instacias_vuelo PRIMARY KEY (vuelo, fecha),
	CONSTRAINT fk_vuelo_dia_instancia_vuelo FOREIGN KEY (vuelo, dia) REFERENCES salidas(vuelo, dia)
)ENGINE = InnoDB;

CREATE TABLE clases (
	nombre VARCHAR(45) NOT NULL,
	porcentaje DECIMAL NOT NULL CHECK(porcentaje BETWEEN 0.00 AND 0.99),

	CONSTRAINT pk_clases PRIMARY KEY (nombre)
)ENGINE = InnoDB;

CREATE TABLE comodidades (
	codigo INT UNSIGNED NOT NULL,
	descripcion VARCHAR(45) NOT NULL,

	CONSTRAINT pk_comodidades PRIMARY KEY (codigo)
)ENGINE = InnoDB;

CREATE TABLE pasajeros (
	doc_tipo VARCHAR(45) NOT NULL,
	doc_nro INT UNSIGNED NOT NULL,
	apellido VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,
	nacionalidad VARCHAR(45) NOT NULL,

	CONSTRAINT PK_pasajeros PRIMARY KEY (doc_tipo, doc_nro)
)ENGINE = InnoDB;

CREATE TABLE empleados (
	legajo INT UNSIGNED NOT NULL,
	password VARCHAR(32) NOT NULL,
	doc_tipo VARCHAR(45) NOT NULL,
	doc_nro INT UNSIGNED NOT NULL,
	apellido VARCHAR(45) NOT NULL,
	nombre VARCHAR(45) NOT NULL,
	direccion VARCHAR(45) NOT NULL,
	telefono VARCHAR(45) NOT NULL,

	CONSTRAINT pk_empleados PRIMARY KEY (legajo)
)ENGINE = InnoDB;

CREATE TABLE reservas(
	numero INT UNSIGNED NOT NULL,
	fecha DATE NOT NULL,
	vencimiento DATE NOT NULL,
	estado VARCHAR(45) NOT NULL,
	doc_tipo VARCHAR(45) NOT NULL,
	doc_nro INT UNSIGNED NOT NULL,
	legajo INT UNSIGNED NOT NULL,

	CONSTRAINT pk_reservas  PRIMARY KEY (numero),
	CONSTRAINT fk_doc_tipo_numero_reservas FOREIGN KEY (doc_tipo, doc_nro) REFERENCES pasajeros(doc_tipo, doc_nro),
	CONSTRAINT fk_legajo_reservas FOREIGN KEY (legajo) REFERENCES empleados(legajo)
)ENGINE = InnoDB;

#-------------------------------------------------------------------------
# Creación Tablas para las relaciones


CREATE TABLE brinda(
	vuelo VARCHAR(45) NOT NULL,
	dia VARCHAR(2) NOT NULL CHECK (dia IN ('Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'Sa')),
	clase VARCHAR(45) NOT NULL,
	precio DECIMAL(7,2) NOT NULL,
	cant_asientos SMALLINT UNSIGNED NOT NULL,

	CONSTRAINT pk_brinda PRIMARY KEY (vuelo, dia, clase),
	CONSTRAINT fk_vuelo_dia_brinda FOREIGN KEY (vuelo, dia) REFERENCES salidas(vuelo, dia),
	CONSTRAINT fk_clase_brinda FOREIGN KEY (clase) REFERENCES clases(nombre)
)ENGINE = InnoDB;

CREATE TABLE posee (
	clase VARCHAR(45) NOT NULL,
	comodidad INT UNSIGNED NOT NULL,

	CONSTRAINT pk_posee PRIMARY KEY (clase, comodidad),
	CONSTRAINT fk_clase_posee FOREIGN KEY (clase) REFERENCES clases(nombre),
	CONSTRAINT fk_comodidad_posee FOREIGN KEY (comodidad) REFERENCES comodidades(codigo)
)ENGINE = InnoDB;

CREATE TABLE reserva_vuelo_clase (
	numero INT UNSIGNED NOT NULL,
	vuelo VARCHAR(45) NOT NULL,
	fecha_vuelo DATE NOT NULL,
	clase VARCHAR(45) NOT NULL,

	CONSTRAINT pk_reserva_vuelo_clase PRIMARY KEY (numero, vuelo, fecha_vuelo),
	CONSTRAINT fk_numero_reserva_vuelo_clase FOREIGN KEY (numero) REFERENCES reservas(numero),
	CONSTRAINT fk_vuelo_fecha_vuelo_reserva_vuelo_clase FOREIGN KEY (vuelo, fecha_vuelo) REFERENCES instancias_vuelo(vuelo, fecha),
	CONSTRAINT fk_clase_reserva_vuelo_clase FOREIGN KEY (clase) REFERENCES clases(nombre)
)ENGINE = InnoDB;


#-------------------------------------------------------------------------
# Creación de vistas 
# 
   #FALTA: CANT ASIENTOS DISPONIBLES.

   CREATE VIEW vuelos_disponibles AS 
   SELECT
		v_p.numero,m_a.modelo,i_v.fecha,s.dia,s.hora_sale,s.hora_llega,
		TIMEDIFF(s.hora_llega,s.hora_sale) AS tiempo_vuelo_estimado,
		v_p.aeropuerto_salida,
		a_s.nombre AS nombre_salida ,a_s.ciudad AS ciudad_salida,a_s.estado AS estado_salida,a_s.pais AS pais_salida,
		v_p.aeropuerto_llegada,
		a_l.nombre AS nombre_llegada,a_l.ciudad AS ciudad_llegada,a_l.estado AS estado_llegada,a_l.pais AS pais_llegada,
		b.precio
   FROM 
		(vuelos_programados as v_p JOIN aeropuertos as a_s ON v_p.aeropuerto_salida = a_s.codigo JOIN aeropuertos as a_l ON v_p.aeropuerto_llegada = a_l.codigo JOIN salidas as s JOIN instancias_vuelo as i_v JOIN brinda as b JOIN modelos_avion as m_a);


#-------------------------------------------------------------------------
# Creación de usuarios y otorgamiento de privilegios

# Usuario: admin
# Acceso total a la base de datos vuelos.
 
   CREATE USER 'admin'@'localhost' IDENTIFIED BY 'admin';
   GRANT ALL PRIVILEGES ON vuelos.* TO 'admin'@'localhost' WITH GRANT OPTION;


# Usuario: empleado
# Permiso de lectura: todas las tablas.
# Permiso para insertar,modificar y actualizar: tablas reservas, pasajeros y reserva_vuelo_clase.

	DROP USER ''@'localhost';
    CREATE USER 'empleado'@'localhost' IDENTIFIED BY 'empleado'; 
    GRANT SELECT ON vuelos.* TO 'empleado'@'localhost';
	GRANT INSERT, DELETE, UPDATE on vuelos.reservas TO 'empleado'@'localhost';
	GRANT INSERT, DELETE, UPDATE on vuelos.pasajeros TO 'empleado'@'localhost';
	GRANT INSERT, DELETE, UPDATE on vuelos.reserva_vuelo_clase TO 'empleado'@'localhost';

	
# Usuario: cliente
# Permiso de lectura: vista 'vuelos_disponibles'>
	CREATE USER 'cliente'@'localhost' IDENTIFIED BY 'cliente'; 
    GRANT SELECT ON vuelos.vuelos_disponibles TO 'cliente'@'localhost';
	


