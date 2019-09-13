#-------------------------------------------------------------------------
# Carga de datos de Prueba

INSERT INTO ubicaciones VALUES ("Argentina", "Buenos Aires", "Bahia Blanca", 3);
INSERT INTO ubicaciones VALUES ("Argentina", "Buenos Aires", "Buenos Aires", 3);
INSERT INTO ubicaciones VALUES ("Brasil", "Rio de Janeiro", "Rio de Janeiro", 3);
INSERT INTO ubicaciones VALUES ("Uruguay", "Montevideo", "Montevideo", 3);

INSERT INTO aeropuertos VALUES ("GIG", "Aeropuerto internacional de Galeao", "552130046050","Av. Vinte de Janeiro", "Brasil", "Rio de Janeiro", "Rio de Janeiro");
INSERT INTO aeropuertos VALUES ("EZE", "Aeropuerto Internacional Ezeiza", "01154806111","AU Tte. Gral. Pablo Riccheri Km 335", "Argentina", "Buenos Aires", "Buenos Aires");
INSERT INTO aeropuertos VALUES ("BHI", "Aeropuerto Bahia Blanca", "542914860312","Ex Ruta 3 Norte KM 675", "Argentina", "Buenos Aires", "Bahia Blanca");
INSERT INTO aeropuertos VALUES ("MVD", "Aeropuerto Internacional de Carrasco", "59826040329","Cap. Juan Antonio Artigas", "Uruguay", "Montevideo", "Montevideo");

INSERT INTO vuelos_programados VALUES ("1001", "EZE", "BHI");
INSERT INTO vuelos_programados VALUES ("1002", "BHI", "EZE");
INSERT INTO vuelos_programados VALUES ("2001", "EZE", "GIG");
INSERT INTO vuelos_programados VALUES ("2002", "EZE", "GIG");
INSERT INTO vuelos_programados VALUES ("2051", "GIG", "MVD");

INSERT INTO modelos_avion VALUES ("737-800", "Boeing", 2, 126);
INSERT INTO modelos_avion VALUES ("A330", "Airbus", 3, 410);
INSERT INTO modelos_avion VALUES ("190", "Embraer", 1, 100);

INSERT INTO salidas VALUES ("1001", 'Lu', '09:00:00', '09:50:00',"190");
INSERT INTO salidas VALUES ("2001", 'Lu', '11:00:00', '13:00:00',"737-800");
INSERT INTO salidas VALUES ("2051", 'Ma', '06:00:00', '07:00:00',"A330");

INSERT INTO instancias_vuelo VALUES ("1001", "2019/09/16", 'Lu', "A tiempo");
INSERT INTO instancias_vuelo VALUES ("2051", "2019/09/17", 'Ma', "Demorado");
INSERT INTO instancias_vuelo VALUES ("2001", "2019/09/16", 'Lu', "Cancelado");


INSERT INTO clases VALUES ("Economica", 0.8);
INSERT INTO clases VALUES ("Primera", 0.1);
INSERT INTO clases VALUES ("Ejecutiva", 0.2);

INSERT INTO comodidades VALUES (1, "Bebidas");
INSERT INTO comodidades VALUES (2, "Pantalla interactiva");
INSERT INTO comodidades VALUES (3, "Desayuno");
INSERT INTO comodidades VALUES (4, "Cena");

INSERT INTO pasajeros VALUES ("DNI", 12358179, "Martinez", "Carlos","Av. Alem 3000",291524212,"Argentina");
INSERT INTO pasajeros VALUES ("DNI", 32358449, "Perez", "Julieta","Rodriguez 391",291524213,"Argentina");
INSERT INTO pasajeros VALUES ("DNI", 22358180, "Martinez", "Carlos","Av. Alem 3000",291524213,"Argentina");

INSERT INTO empleados VALUES (105721, "empleado123", "DNI", 22358180, "Martinez", "Carlos","Av. Alem 3000",291524213);

INSERT INTO reservas VALUES (1, "2019/08/10", "2019/12/10", "Pagada", "DNI", 12358179,105721);
INSERT INTO reservas VALUES (2, "2019/08/02", "2020/02/10", "Pendiente de pago", "DNI", 32358449,105721);
INSERT INTO reservas VALUES (3, "2019/08/02", "2020/02/10", "Pendiente de pago", "DNI", 22358180,105721);

INSERT INTO brinda VALUES (1001, 'Lu', "Primera", 9000.51, 5);
INSERT INTO brinda VALUES (2051, 'Ma', "Primera", 15000.00, 50);
INSERT INTO brinda VALUES (1001, 'Lu', "Economica", 1000.00, 80);

INSERT INTO posee VALUES ("Economica", 1);
INSERT INTO posee VALUES ("Economica", 2);
INSERT INTO posee VALUES ("Ejecutiva", 1);
INSERT INTO posee VALUES ("Ejecutiva", 2);
INSERT INTO posee VALUES ("Ejecutiva", 3);
INSERT INTO posee VALUES ("Primera", 1);
INSERT INTO posee VALUES ("Primera", 2);
INSERT INTO posee VALUES ("Primera", 3);
INSERT INTO posee VALUES ("Primera", 4);

INSERT INTO reserva_vuelo_clase VALUES (1,1001,"2019/09/16","Primera");
INSERT INTO reserva_vuelo_clase VALUES (2,1001,"2019/09/16","Economica");
INSERT INTO reserva_vuelo_clase VALUES (3,1001,"2019/09/16","Economica");



