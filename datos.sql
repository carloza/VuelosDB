#-------------------------------------------------------------------------
# Carga de datos de Prueba

use vuelos;

INSERT INTO ubicaciones VALUES ("Argentina", "Buenos Aires", "Bahia Blanca", -3);
INSERT INTO ubicaciones VALUES ("Argentina", "Buenos Aires", "Buenos Aires", -3);
INSERT INTO ubicaciones VALUES ("Brasil", "Amazonas", "Manaos", -4);
INSERT INTO ubicaciones VALUES ("Brasil", "Rio de Janeiro", "Rio de Janeiro", -3);
INSERT INTO ubicaciones VALUES ("Uruguay", "Montevideo", "Montevideo", -3);
INSERT INTO ubicaciones VALUES ("Francia", "Paris", "Paris", 2);
INSERT INTO ubicaciones VALUES ("Portugal", "Lisboa", "Lisboa", 0);
INSERT INTO ubicaciones VALUES ("Estados Unidos", "Nueva York", "Nueva York", -4);
INSERT INTO ubicaciones VALUES ("Estados Unidos", "Nueva York", "Nueva York", -4); #PRUEBAERROR

INSERT INTO aeropuertos VALUES ("GIG", "Aeropuerto Internacional de Galeao", "552130046050","Av. Vinte de Janeiro", "Brasil", "Rio de Janeiro", "Rio de Janeiro");
INSERT INTO aeropuertos VALUES ("AEP", "Aeroparque Internacional Jorge Newbery", "1154806111","Av. Costanera Rafael Obligado s/n, C1425 CABA", "Argentina", "Buenos Aires", "Buenos Aires");
INSERT INTO aeropuertos VALUES ("EZE", "Aeropuerto Internacional Ezeiza", "01154806111","AU Tte. Gral. Pablo Riccheri Km 335", "Argentina", "Buenos Aires", "Buenos Aires");
INSERT INTO aeropuertos VALUES ("BHI", "Aeropuerto Bahia Blanca", "542914860312","Ex Ruta 3 Norte KM 675", "Argentina", "Buenos Aires", "Bahia Blanca");
INSERT INTO aeropuertos VALUES ("MVD", "Aeropuerto Internacional de Carrasco", "59826040329","Cap. Juan Antonio Artigas", "Uruguay", "Montevideo", "Montevideo");
INSERT INTO aeropuertos VALUES ("ORY", "Aeropuerto de Paris-Orly", "33892563950","94390 Orly, Francia", "Francia", "Paris", "Paris");
INSERT INTO aeropuertos VALUES ("JFK", "Aeropuerto Internacional John F. Kennedy", "17182444444","Queens, Nueva York 11430, EE. UU.", "Estados Unidos", "Nueva York", "Nueva York");
INSERT INTO aeropuertos VALUES ("MAO", "Aeropuerto Internacional de Manaos", "559236521210","Av. Santos Dumont, 1350","Brasil", "Amazonas", "Manaos");
INSERT INTO aeropuertos VALUES ("LIS", "Aeropuerto de Lisboa", "351218413500","Alameda das Comunidades Portuguesas, 1700-111","Portugal", "Lisboa", "Lisboa");

INSERT INTO vuelos_programados VALUES ("1001", "EZE", "BHI");
INSERT INTO vuelos_programados VALUES ("1002", "EZE", "GIG");
INSERT INTO vuelos_programados VALUES ("2001", "BHI", "EZE");
INSERT INTO vuelos_programados VALUES ("3254", "GIG", "LIS");
INSERT INTO vuelos_programados VALUES ("3999", "MAO", "LIS");
INSERT INTO vuelos_programados VALUES ("4951", "ORY", "JFK");
INSERT INTO vuelos_programados VALUES ("4952", "ORY", "MVD");
INSERT INTO vuelos_programados VALUES ("4953", "ORY", "EZE");
INSERT INTO vuelos_programados VALUES ("5231", "LIS", "ORY");
INSERT INTO vuelos_programados VALUES ("7051", "GIG", "MVD");
INSERT INTO vuelos_programados VALUES ("7720", "ORY", "LIS");


INSERT INTO clases VALUES ("Economica", 0.3);
INSERT INTO clases VALUES ("Economica Plus", 0.3);
INSERT INTO clases VALUES ("Primera", 0.05);
INSERT INTO clases VALUES ("Ejecutiva", 0.1);

INSERT INTO modelos_avion VALUES ("737-800", "Boeing", 2, 126);
INSERT INTO modelos_avion VALUES ("A330", "Airbus", 3, 410);
INSERT INTO modelos_avion VALUES ("190", "Embraer", 1, 100);
INSERT INTO modelos_avion VALUES ("A320", "Airbus", 1, 150);
INSERT INTO modelos_avion VALUES ("717", "Boeing", 3, 300);
INSERT INTO modelos_avion VALUES ("777", "Boeing", 4, 550);
INSERT INTO modelos_avion VALUES ("CRJ700", "Jet Regional", 1, 78);


INSERT INTO salidas VALUES ("1001", 'Lu', '09:00:00', '09:50:00',"737-800");
INSERT INTO salidas VALUES ("1001", 'Ma', '09:00:00', '09:50:00',"737-800");

INSERT INTO salidas VALUES ("1002", 'Do', '07:00:00', '16:00:00',"717");
INSERT INTO salidas VALUES ("2001", 'Lu', '11:00:00', '13:00:00',"190");
INSERT INTO salidas VALUES ("3999", 'Mi', '07:00:00', '16:00:00',"777");
INSERT INTO salidas VALUES ("4951", 'Vi', '10:00:00', '18:20:00',"A330");
INSERT INTO salidas VALUES ("5231", 'Mi', '17:00:00', '19:00:00',"717");
INSERT INTO salidas VALUES ("5231", 'Mi', '17:00:00', '19:00:00',"717"); #PRUEBAERROR
INSERT INTO salidas VALUES ("7051", 'Ma', '06:00:00', '07:00:00',"CRJ700");

INSERT INTO brinda VALUES ("2001", 'Ma', "Economica", 1000.00, 1);
INSERT INTO brinda VALUES ("1001", 'Lu', "Economica Plus", 3000.00, 20);
INSERT INTO brinda VALUES ("1001", 'Lu', "Primera", 9000.51, 46);
INSERT INTO brinda VALUES ("2001", 'Lu', "Primera", 59000.51, 100);
INSERT INTO brinda VALUES ("3999", 'Mi', "Economica", 29000.51, 300);
INSERT INTO brinda VALUES ("3999", 'Mi', "Economica Plus", 35000.51, 100);
INSERT INTO brinda VALUES ("3999", 'Mi', "Ejecutiva", 51253.60, 100);
INSERT INTO brinda VALUES ("3999", 'Mi', "Primera", 84526.66, 50);
INSERT INTO brinda VALUES ("4951", 'Vi', "Economica", 33340.20, 300);
INSERT INTO brinda VALUES ("4951", 'Vi', "Ejecutiva", 52130.30, 93);
INSERT INTO brinda VALUES ("4951", 'Vi', "Primera", 80520.00, 17);
INSERT INTO brinda VALUES ("7051", 'Ma', "Primera", 15000.00, 78);

INSERT INTO comodidades VALUES (1, "Bebidas");
INSERT INTO comodidades VALUES (2, "Pantalla interactiva");
INSERT INTO comodidades VALUES (3, "Desayuno");
INSERT INTO comodidades VALUES (4, "Cena");
INSERT INTO comodidades VALUES (5, "Wi-Fi");

INSERT INTO posee VALUES ("Economica", 1);
INSERT INTO posee VALUES ("Economica", 2);
INSERT INTO posee VALUES ("Economica Plus", 1);
INSERT INTO posee VALUES ("Economica Plus", 2);
INSERT INTO posee VALUES ("Economica Plus", 5);
INSERT INTO posee VALUES ("Ejecutiva", 1);
INSERT INTO posee VALUES ("Ejecutiva", 2);
INSERT INTO posee VALUES ("Ejecutiva", 3);
INSERT INTO posee VALUES ("Ejecutiva", 5);
INSERT INTO posee VALUES ("Primera", 1);
INSERT INTO posee VALUES ("Primera", 2);
INSERT INTO posee VALUES ("Primera", 3);
INSERT INTO posee VALUES ("Primera", 4);
INSERT INTO posee VALUES ("Primera", 5);

INSERT INTO pasajeros VALUES ("DNI", 12358179, "Martinez", "Carlos","Av. Alem 3000",54291524212,"Argentina");
INSERT INTO pasajeros VALUES ("DNI", 12358179, "Martinez", "Carlos","Av. Alem 3000",54291524212,"Argentina"); #pruebafalla
INSERT INTO pasajeros VALUES ("DNI", 32358449, "Perez", "Julieta","Rodriguez 391",54291524213,"Argentina");
INSERT INTO pasajeros VALUES ("DNI", 22358180, "Martinez", "Carlos","Av. Alem 3000",54291524213,"Argentina");
INSERT INTO pasajeros VALUES ("DNI", 42000123, "Gonzalez", "Esteban","Av. Gral. Paz 1234",5411454545,"Chile");

INSERT INTO empleados VALUES (105721, md5("empleado105721"), "DNI", 22358180, "Martinez", "Carlos","Av. Alem 3000",54229152423);
INSERT INTO empleados VALUES (105757, md5("empleado105757"), "DNI", 25865925, "Larreta", "Alberto","Estomba 300",54112524213);

/* LOS SIGUIENTES DATOS PUEDEN OMITIRSE A PARTIR DE LA INCOPORACION DEL TRIGGER QUE SE ENCUENTRA EN EL ARCHIVO "trigger_ad.sql". Leer comentarios en el mismo respecto a su comportamiento e incorporacion. 

INSERT INTO asientos_reservados VALUES (0,'Primera','2019-09-09',1001);
INSERT INTO asientos_reservados VALUES (0,'Primera','2019-09-16',2001);
INSERT INTO asientos_reservados VALUES (0,'Economica','2019-09-09',1001);
INSERT INTO asientos_reservados VALUES (0,'Primera','2019-09-09',1001);

*/




