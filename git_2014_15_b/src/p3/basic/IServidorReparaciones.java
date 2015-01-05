package p3.basic;

import java.util.Date;

import com.mysql.jdbc.Connection;

/**
 * Modela un servidor asíncrono de acceso a la base de datos reparaciones.
 * Los métodos toman una referencia del objeto en donde se almacenará
 * el resultado.
 * 
 * @author LSI.
 *
 */
public interface IServidorReparaciones {
	
	/**
	 * Obtiene la reparación cuyo id se especifica si existe en la 
	 * base de datos.
	 * 
	 * @param id Identificador de la reparación.
	 * @param f Objeto donde se guarda el resultado.
	 */
	void getReparacionPorId(String id, IFuturo f);
	
	/**
	 * Obtiene las reparaciones realizadas por un taller determinado.
	 * @param idTaller Identificador del taller
	 * @param f Objeto donde se guarda el resultado.
	 */
	void getReparacionPorTaller(String idTaller, IFuturo f);

	/**
	 * Obtiene las reparaciones realizadas entre dos fechas (ambas inclusive).
	 * @param fi fecha inicio periodo
	 * @param fs fecha fin periodo
	 * @param f Objeto donde se guarda el resultado.
	 */
	void getReparacionPorFechas(Date fi, Date fs, IFuturo f);
	
	/**
	 * Obtienen todas las reparaciones efectuadas a un coche determinado
	 * @param matricula mattricula del coche
	 * @param f Objeto donde se guarda el resultado.
	 */
	void getReparacionPorCoche(String matricula, IFuturo f);

	/**
	 * Da de alta una nueva reparación.
	 * 
	 * @param conn conexión con la base de datos.
	 * @param id identificador de la reparación.
	 * @param mat matricula del coche.
	 * @param taller identificador del taller.
	 * @param inicio fecha de inicio de la reparación.
	 */
	void nuevaReparcacion(String id, String mat, String taller, Date inicio, IFuturo f);
	
	/**
	 * Da por terminada una reparación.
	 * 
	 * @param conn conexión con la base de datos.
	 * @param id identificador de la reparación.
	 * @param fin fecha de fin de la reparación.
	 * @param precio precio de la reparacióm.
	 */
	void cerrarReparacion(String id, Date fin, int precio, IFuturo f);
	
}
