package p2.model_impl;

import org.json.JSONObject;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Vector;

import utilidades.Utilidades;

public class TestBD {
	
    static final String connURL = "jdbc:mysql://localhost/er_gusano?user=alumno&password=lsi";

	
	
	public static void testInsertSnake(String id_juego) throws SQLException{
		Snake snake = new Snake();
		for (int i = 0; i < 5; i++){
			snake.addLink();
			snake.incColumn();
		}
		System.out.println("testInsertSnake");		
		Connection conn = Utilidades.connect(connURL);
		ArrayList<SnakeLink> links = snake.getLinks();
		String iqueries [] = new String[links.size()];
		for (int i = 0; i < iqueries.length; i++){
			SnakeLink link = links.get(i);
			iqueries[i] = "INSERT INTO gusano (id_juego, columna, fila, posicion) VALUES (" +
							"'" + id_juego + "'," +
							link.getCoordinate().getColumn() + ", " +
							link.getCoordinate().getRow() + ", " +
							i + ");";		
			System.out.println("Preparing ---> " + iqueries[i]);
		}
		Utilidades.insertData(conn, iqueries);
		Utilidades.disconnect(conn);
		System.out.println("END testInsertSnake");		
		System.out.println("-------------------------------------------");		
	}
	
	public static Snake testLoadingSnake(String id_juego) throws SQLException{
		Connection conn = Utilidades.connect(connURL);
		ResultSet rs = Utilidades.getTableFromQuery(conn, 
                           "select * from gusano where id_juego = '" + id_juego + "';");
		Vector<String> links = Utilidades.getResultStrings(rs, ",");
		Utilidades.printVector(links);
		Utilidades.disconnect(conn);
		return null;
	}
	
	public static void main(String args []) throws SQLException{
		//testInsertSnake("J2");
		testLoadingSnake("J1");
	}

}
