package utilidades;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.Vector;
import java.sql.Connection;


public class Utilidades {
	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// BASES DE DATOS. CONEXIÖN Y CONSULTAS.
	
	/**
	 * Conexión a una base datos.
	 * @param dbName	URL que identifica a la base de datos.
	 * @return conexión a la base de datos.
	 */
	public static Connection connect(String dbName) {
		
		Connection connection = null;
		
		System.out.println("Utilidades.connect: connecting to DB: " + dbName);
		try {
			connection = DriverManager.getConnection(dbName);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		
		return connection;
	}
	
	/**
	 * Cierra una conexión existente con una base de datos.
	 * @param connection Conexión a cerrar.
	 */
	public static void disconnect(Connection connection){
		System.out.println("Utilidades.diconnect");
		try {
			connection.close();
		}
		catch(SQLException e){
			e.printStackTrace();
		}
	}
    
	/**
	 * Devuelve el contenido de una tabla de la base de datos.
	 * @param connection Conexión a la base de datos
	 * @param tableName Nombre de la tabla
	 * @return contenidos de la tabla.
	 * @throws SQLException 
	 */
	public static ResultSet getTable(Connection connection, String tableName) throws SQLException{
		return getTableFromQuery(connection, "SELECT * FROM " + tableName);
	}
	

	/**
	 * Devuelve los resultados de una consulta SELECT
	 * @param connection Conexión a la base de datos.
	 * @param query consulta
	 * @return resultados de la consulta.
	 * @throws SQLException 
	 */
	public static ResultSet getTableFromQuery(Connection connection, String query) throws SQLException {	
		
		Statement statement = null;
		ResultSet resultSet = null;
		
		// Debug:
		// System.out.println("Utilidades.geTableFromQuery: " + query);
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);
		}
		catch(SQLException e){
			e.getMessage();
			throw e;
		}
		return resultSet;
	}
	
	/**
	 * Inserta datos en una bsade de datos
	 * @param connection conexión
	 * @param iqueries array de consultas INSERT
	 * @throws SQLException
	 */
	public static void insertData(Connection connection, String [] iqueries) throws SQLException {
		if (iqueries != null && iqueries.length > 0) {
			Statement statement = connection.createStatement();
			for (int i = 0; i < iqueries.length; i++) {
				if (iqueries[i] != null) statement.executeUpdate(iqueries[i]);
			}
		}
		
	}
	
	/**
	 * Inserta datos en una bsade de datos
	 * @param connection conexión
	 * @param iqueries array de consultas INSERT
	 * @throws SQLException
	 */
	public static void updateData(Connection connection, String [] iqueries) throws SQLException {
		if (iqueries != null && iqueries.length > 0) {
			Statement statement = connection.createStatement();
			for (int i = 0; i < iqueries.length; i++) {
				if (iqueries[i] != null) statement.executeUpdate(iqueries[i]);
			}
		}
		
	}
	
	public static void updateData(Connection connection, String iquery) throws SQLException {
		if (iquery != null) {
			Statement statement = connection.createStatement();
			statement.executeUpdate(iquery);
		}
	}	
	
	
	/////////////////////////////////////////////////////////////////////////////////////////
	// BASES DE DATOS. PROCESAMIENTO DEL RESULTADO DE LAS CONSULTAS SELECT.
	
	/**
	 * Imprime en consola, en forma de tabla, los contenidos de un ResultSet.
	 * @param rs result set
	 * @param longitudCampos ancho de los campos de la tabla en número de caracteres.
	 */
	public static void printResultSet(ResultSet rs, int longitudCampos) {
		
		System.out.println("---------------------------------------------------");
		System.out.println("TABLA ");
 		System.out.println("---------------------------------------------------");
		
 		try {
 			if (rs != null){
 				
 				longitudCampos = (longitudCampos > 0 && longitudCampos < 30)?longitudCampos:30;
 				
 				boolean masFilas = rs.next();
 				ResultSetMetaData rsmd = rs.getMetaData();

 				if (!masFilas){
 					System.out.println("displayResultSet: Tabla vacía.");
 				}
 				else 
 				{	
 					// Imprimimos cabeceras:
 					int nC = rsmd.getColumnCount();
 					for(int i = 1; i <= nC; ++i){
 						String dato = formatLongitud(rsmd.getColumnName(i), longitudCampos);
 						System.out.print(dato);
 					}
 					System.out.println();

 					// Imprimimos datos, fila por fila:
 					String dato = null;
 					do {
 						Vector<Object> datosFila = Utilidades.getNextRow(rs);
 						for (int i = 0; i < datosFila.size(); i++){
 							if (datosFila.elementAt(i) != null) {
 								dato = formatLongitud(datosFila.elementAt(i).toString(), longitudCampos);
 							}
 							System.out.print(dato);
 						}
 						System.out.println();
 					} while (rs.next());
 				}
 			} // rs != null
 			else
 			{
					System.out.println("displayResultSet: Tabla ES NULL.");
 			}
 		}
 		catch(SQLException e){
 			e.printStackTrace();
 		}
 		System.out.println("---------------------------------------------------");
	}
	
	
	/**
	 * Convierte un result set en un vector de Strings, donde cada String contiene una fila de la
	 * tabla de resultados, con las columnas separadas por el separador indicado como argumento.
	 * @param rs result set
	 * @param fieldSeparator separador de columnas
	 * @return vector conteniendo las filas del result set.
	 */
	public static Vector<String> getResultStrings(ResultSet rs, String fieldSeparator) {
		
		Vector<String> filas = new Vector<>();

 		try {
 			if (rs != null){ 	
 				//rs.first();
 				rs.beforeFirst();
 				boolean masFilas = rs.next();
 				ResultSetMetaData rsmd = rs.getMetaData();
 				
 				if (masFilas){
 					// fill filas, row by row:
 					do {
 						Vector<Object> datosFila = Utilidades.getNextRow(rs);
 						StringBuffer sb = new StringBuffer();
 						for (int i = 0; i < datosFila.size(); i++){
 							if (datosFila.elementAt(i) != null) {
 								sb.append(datosFila.elementAt(i).toString() + (i < (datosFila.size()-1)?fieldSeparator:""));
 							}
 							else{
 								sb.append("NULL" + (i < (datosFila.size()-1)?fieldSeparator:""));
 							}
 						}
 						filas.add(sb.toString());
 					} while (rs.next());
 				}
 			}
 		}
 		catch(SQLException e){
 			e.printStackTrace();
 		}
 		return filas;
	}	
	
	/**
	 * Obtiene un vector de objetos con los elementos de la fila del
	 * result set a la que actualmente apunta el cursor interno del ResultSet.
	 * ASUME LOS TIPOS DE DATOS DE LA BASE DE DATOS reparaciones.
	 * @param rs resul set
	 * @return fila a la que apunta el cursor interno del result set.
	 */
	public static Vector<Object> getNextRow(ResultSet rs)  {
		
		if (rs == null) {
			return null;
		}
	
		Vector<Object> fila = new Vector<Object>();
		try{
			ResultSetMetaData rsmd = rs.getMetaData();
			int nC = rsmd.getColumnCount();
	
			for(int i = 1; i <= nC; ++i){
				switch(rsmd.getColumnType(i)){
				case Types.CHAR:
					fila.addElement(rs.getString(i));
					break;
				case Types.VARCHAR:
					fila.addElement(rs.getString(i));
					break;
				case Types.TIME:
					fila.addElement(rs.getString(i));
					break;	
				case Types.DATE:
					fila.addElement(rs.getString(i));
					break;	
				case Types.INTEGER:
					fila.addElement(new Long(rs.getLong(i)));
					break;	
				case Types.BIGINT:
					fila.addElement(new Long(rs.getLong(i)));
					break;	
				case Types.DOUBLE:
					fila.addElement(new Double(rs.getDouble(i)));
					break;
				default:
					fila.addElement(rs.getObject(i));
					System.out.println("El tipo de la columna " + i + " es " + rsmd.getColumnTypeName(i) + ".");
				}
			}	
		} 
		catch(SQLException e){
			e.printStackTrace();
		}
	
		return fila;
	}

	/////////////////////////////////////////////////////////////////////////////////////////
	// FICHEROS.
	
	/**
	 * Lee los contenidos de un fichero de TEXTO.
	 * @param fichero ruta del fichero
	 * @return contenido del fichero
	 */
	public static String leerFichero(String fichero) {
		
		String contents = new String();
		String line = null;
	    BufferedReader fileReader = null;
	    StringWriter stwt = new StringWriter(); 
		try{
	        fileReader = new BufferedReader(new FileReader(fichero));
	        
	        while((line = fileReader.readLine()) != null){
	        	stwt.write(line + "\n");
	        }
	        fileReader.close();
	        contents = stwt.toString();
	        stwt.close();
		} catch(IOException ioe){
			//ioe.printStackTrace();
			contents = null;
		} 
		return contents;
	}
	

	/////////////////////////////////////////////////////////////////////////////////////////
	// UTILIDADES PARA EL TRATAMIENTO DE STRINGS Y COLECCIONES DE STRINGS.

	/**
	 * Toma un string y devuelve otro de una longitud mayor, añadiendo 
	 * blancos al inicio.
	 * 
	 * @param s string original
	 * @param lon longitud deseada para el string generado.
	 * @return string generado.
	 */
	public static String formatLongitud(String s, int lon){
		
		String dato = s;
		
		if (s != null) {
			if (lon - dato.length() >= 0){
				char blancos [] = new char[lon - dato.length()];
				for (int j = 0; j < blancos.length; j++) blancos[j] = ' ';
				dato = dato.concat(new String(blancos));
			}
			else {
				dato = dato.substring(0, lon);
			}
		}
		return dato;
	}

	/**
	 * Imprime un vector de strings por consola, cada string en una línea diferente.
	 * @param filas vector de strings
	 */
	public static void printVector(Vector<String> filas){
		if (filas != null){
			int i = 1;
			for(String f: filas)
				System.out.println("R"+i+" --->  "+ f);
		}
		else{
			System.out.println("printVector: argument is null");
		}
	}
	
	/**
	 * Elimina los duplicados de un vector de strings.
	 * @param filas vector de entrada.
	 * @return nuevo vector como el original, pero sin duplicados.
	 */
	public static Vector<String> eliminarDuplicados(Vector<String> filas){
		
		Vector<String> target = new Vector<>();
		
		if (filas != null){
			for (String s: filas){
				if (!target.contains(s)){
					target.add(s);
				}
			}
		}
		return target;
	}
	
	/**
	 * Toma un vector y elimina del mismo su elemento más pequeño
	 * @param v vector de entrada
	 * @return valor eliminado
	 */
	public static String extraerMinimo(Vector<String> v){		
		String min = null;
		if (v!= null){
			min = v.firstElement();
			for (String s : v){
				if (s.compareTo(min) < 0){
					min = s;
				}
			}
			v.remove(min);
		}
		return min;
	}
	
	/**
	 * Toma un vector de strings como argumento y devuelve un array de strings con los
	 * elementos del vector ordenados.
	 * @param filas vector de entrada
	 * @return array de salida.
	 */
	public static String [] ordenar (Vector<String> filas){

		String sarray[] = null;

		if (filas != null){
			Vector<String> target = new Vector<>();
			target.addAll(filas);
			
			sarray = new String[filas.size()];
			int index = 0;
			while (!target.isEmpty()){
				sarray[index++] = extraerMinimo(target);
			}
		}
		return sarray;
	}

	/**
	 * Crea un directorio si no existe.
	 * @param pathname nombre del directorio.
	 */
	public static void crearDirectorio(String pathname){
		File directorio = new File(pathname);

		if (!directorio.exists()) {
			System.out.println("creating directory: " + pathname);
			boolean result = false;

			try{
				directorio.mkdir();
				result = true;
			} catch(SecurityException se){
				se.printStackTrace();
			}        
			if(result) {    
				System.out.println("DIR created");  
			}
		}
	}
	
	/**
	 * Escribe un string en un fichero de texto.
	 * @param s 
	 * @param fichero fiechero destino
	 */
	public static void escribirEnFichero(String s, String fichero) throws FileNotFoundException {
		
		if (s == null || fichero == null){
			return;
		}
		
		PrintWriter streamWriter = null;
		
	    try {
			streamWriter = new PrintWriter(new FileOutputStream (fichero));
			streamWriter.println(s);
			streamWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * Escribe un array de strings en un fichero de texto, cada uno en una línea diferente.
	 * @param s array de strings
	 * @param fichero fichero destino.
	 */
	public static void escribirEnFichero(String [] s, String fichero) {
				
		if (s == null || fichero == null){
			return;
		}
		
		PrintWriter streamWriter = null;
		
	    try {
			streamWriter = new PrintWriter(new FileOutputStream (fichero));
			for (int i = 0; i < s.length; i++){
				streamWriter.println(s[i]);
			}
			streamWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Number of rows, number of matches.
	 * @param f1
	 * @param f2
	 * @return
	 * @throws Exception
	 */
	public static Vector<String> compareFilesRowByRow(String f1, String f2) {
		
		String ss1 = leerFichero(f1);
		String ss2 = leerFichero(f2);
		
		String s1 [] = null;
		String s2 [] = null;
		
		Vector<String> res = new Vector<>();
		int matches = 0;

		if (ss1 == null){
			res.add("Reference file " + f1 + " CANNOT BE READ: FAILED");
		}
		else if (ss2 == null) {
			res.add("Assessed file " + f2 + " CANNOT BE READ: FAILED");
		}
		else {
			s1 = ss1.split("\n");
			s2 = ss2.split("\n");
			res.add("Rows f1: " + s1.length + "\tRows f2: " + s2.length + " \t");
			for (int i = 0; i < s1.length; i++){
				boolean match = false;
				for (int j = 0; j < s2.length &(!match); j++){
					if (s1[i].equals(s2[j])){
						match = true;
						matches++;
						res.add("T");
					}
				}
				if (!match){
					res.add("F");
				}
			}
			res.add(" ----> TOTAL matches:" + matches + " OF " + s1.length);
		}
		return res;
		
	}
}
