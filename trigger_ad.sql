use vuelos; 

delimiter ! 

/*
Nos parecio util la incorporacion de este trigger, debido a que la nueva tabla que surge del proyecto 3, llamada "asientos_reservados", puede ser autocompletada luego de :
	- que se inserten filas en la tabla "salida" 
	- que se generan filas en la tabla "instancias_vuelo" mediante el trigger solicitado por el proyecto.

Una vez sucedido lo anterior, se inserta en la tabla "brinda" una fila. Luego de esto, podemos disparar un trigger que complete todos los asientos reservados para el vuelo en cuestion, los cuales estaran logicamente inicializados en cero, ya que no habra reservas realizadas. 

Los valores de la tabla "asientos_reservados" seran debidamente actualizados mediante el uso adecuado de los procedimientos almacenados solicitados por el proyecto.
*/
CREATE TRIGGER crear_asientos_reservados
AFTER INSERT ON brinda        
FOR EACH ROW
BEGIN	
	
	DECLARE fecha_obtenida DATE;
	DECLARE finished INTEGER DEFAULT 0;	
	
	DECLARE fechas 
		CURSOR FOR 
			SELECT iv.fecha 
			FROM instancias_vuelo AS iv 
			WHERE (
				(iv.vuelo = NEW.vuelo) and 
				(iv.dia = NEW.dia)
			);
			
	DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;
	
	OPEN fechas;

	FETCH fechas INTO fecha_obtenida;
	
	/*
	 POR CADA FECHA OBTENIDA EN CADA INSTANCIA DE VUELO, INSERTO UN ASIENTO RESERVADO INICIALIZADO EN CERO. 
	*/
	WHILE (finished = 0) DO
		INSERT INTO asientos_reservados VALUES (0,NEW.clase,fecha_obtenida,NEW.vuelo);
		FETCH fechas INTO fecha_obtenida;
	END WHILE;
	
	CLOSE fechas;
	
END; 
!

delimiter ;