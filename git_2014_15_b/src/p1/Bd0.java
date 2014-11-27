package p1;

import java.sql.Connection;
import java.sql.ResultSet;
import utilidades.Teclado;
import utilidades.Utilidades;


/**
 * Primer ejemplo de acceso a bases de datos desde Java.
 * Asume el uso de la BD Concesionario.
 * Muestra:
 *    - Conexión a la base de datos.
 *    - Envío a la BD de consultas leídas desde teclado.
 *    - recogida de datos de la consulta, que se muestran por consola.
 *    
 * @author LSI-JAPF
 *
 */
public class Bd0 {
	
	/*
	 Conexión a la base de datos  
	*/
    static Connection connection;
    //static final String connURL = "jdbc:mysql://localhost/concesionario?user=root";
    static final String connURL = "jdbc:mysql://localhost/concesionario?user=alumno&password=lsi";

    
    /*
     Tablas de la Base de Datos. Se asume base de datos Concesionario.
     */
    static String [] tableNames = {"clientes", "coches", "concesionarios", "distribucion", "marcas", "marco", "ventas"};
    static int currentTable = 0;
    
    /*
     Modelado de la consulta.
     */
    static String currentQuery = "SELECT dni, nombre FROM  clientes WHERE ciudad IN ('Madrid')";
    
	
	
	public static String solicitarCmd(){
		System.out.println("-----------------------------------------");
		System.out.print("Tablas Concesionario: ");
		for (int i = 0; i < tableNames.length - 1; i++) System.out.print(tableNames[i] + ", ");
		System.out.println(tableNames[tableNames.length - 1]);
		System.out.println("-----------------------------------------");
		System.out.println();

		System.out.println("Introduzca comando: ");
		System.out.println();
		System.out.println("Formatos: ");
		System.out.println("Mostrar tabla:        MTB;nombre_tabla.");
		System.out.println("Enviar consulta:      ECS;consulta.");
		System.out.println("Salir:                END.");
		System.out.println();
		System.out.print("> ");
	
		return Teclado.readString();
	}
    

	public static void main(String[] args) {
		
		// Conexión a la base de datos.
		connection = Utilidades.connect(connURL);
		
		/* 
		   Bucle de la aplicación.
				Se pide comando.
				Se decodifica y envía a la BD
				Se obtiene el resultado y se imprime.
		 * */

		String cmd = solicitarCmd();
		while (!cmd.equalsIgnoreCase("end")){
						
			String [] campos = cmd.split(";");
			if (campos == null){
				System.out.println("Bd0 ---> Error: Cmd nulo.");
			}
			else if (campos[0].equalsIgnoreCase("MTB")){
				System.out.println("Bd0 --->  Mostrando tabla: " + campos[1]);
				ResultSet rs = Utilidades.getTable(connection, campos[1]);
				Utilidades.printResultSet(rs, 12);
			}
			else if (campos[0].equalsIgnoreCase("ECS")){
				System.out.println("Bd0 --->  Mostrando resultado consulta: " + campos[1]);
				ResultSet rs = Utilidades.getTableFromQuery(connection, campos[1]);
				Utilidades.printResultSet(rs, 12);
			}
			else {
				System.out.println("Bd0 --->  Comando desconocido " + cmd );
			}
			cmd = solicitarCmd();
		}
		System.out.println("Exit application.");
		
		// Desconexión a la base de datos.		
		Utilidades.disconnect(connection);
	}

}
