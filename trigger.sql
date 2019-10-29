use vuelos; 

delimiter ! 

CREATE PROCEDURE obtener_num_dia_sem
(IN dia_semana VARCHAR(2), OUT dia_num INT) 
BEGIN
	IF (dia_semana = 'Do') THEN
		SET dia_num = 1;
	ELSEIF (dia_semana = 'Lu') THEN
		SET dia_num = 2;
	ELSEIF (dia_semana = 'Ma') THEN
		SET dia_num = 3;
	ELSEIF (dia_semana = 'Mi') THEN
		SET dia_num = 4;
	ELSEIF (dia_semana = 'Ju') THEN
		SET dia_num = 5;
	ELSEIF (dia_semana = 'Vi') THEN
		SET dia_num = 6;
	ELSE
		SET dia_num = 7;
	END IF;
END; 
!

CREATE TRIGGER crear_instancias_vuelo
AFTER INSERT ON salidas        
FOR EACH ROW
BEGIN	
	SET @dia_semana_new = NEW.dia;
	SET @num_dia_new = 0;
	CALL obtener_num_dia_sem(@dia_semana_new, @num_dia_new);
	
	SET @dia_actual = (SELECT CURDATE());
	SET @num_dia_actual = (SELECT DAYOFWEEK(@dia_actual));
	
	SET @diferencia = (SELECT ABS(@num_dia_actual - @num_dia_new));
	SET @dia_a_insertar = (SELECT DATE_ADD(@dia_actual, INTERVAL @diferencia DAY));
	
	SET @fecha_limite = (SELECT DATE_ADD(@dia_actual,INTERVAL 1 YEAR));
	
	insertar_vuelos: LOOP
		IF ((SELECT DATEDIFF(@fecha_limite,@dia_a_insertar)) >= 0) 		
		THEN
			INSERT INTO instancias_vuelo VALUES (NEW.vuelo,@dia_a_insertar,@dia_semana_new,'A tiempo');
			SET @dia_a_insertar = (SELECT DATE_ADD(@dia_a_insertar, INTERVAL 7 DAY));
			ITERATE insertar_vuelos;
		ELSE
			LEAVE insertar_vuelos;
		END IF;
	END LOOP insertar_vuelos;
	
END; 
!

delimiter ;