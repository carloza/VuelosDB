package sql_conn;

import java.sql.*;

public class vuelos_db {

	public static Connection vuelos_db_connection;
	
	public static void connection_Vuelos_DB(String user, String password) throws SQLException {
		String servidor = "localhost:3306";
		String database = "vuelos";
		String url = "jdbc:mysql://"+servidor+"/"+database+"?serverTimezone=UTC";
		vuelos_db_connection = java.sql.DriverManager.getConnection(url,user,password);
		System.out.println("Conexion establecida");
	}

	public static Connection get_Connection_Vuelos_DB() {
		return vuelos_db_connection;
	}
	
	public static void close_Connection() {
		if (vuelos_db_connection != null)
			try {
				vuelos_db_connection.close();
				vuelos_db_connection = null;
				System.out.println("Conexion finalizada");
			} catch (SQLException e) {
				System.out.println("Error al finalizar conexion:" + e.getMessage());
			}
	}
	
}
