package p3.basic;

import java.util.Date;

import com.mysql.jdbc.Connection;

/**
 * Modela un servidor as�ncrono de acceso a la base de datos reparaciones.
 * Los m�todos toman una referencia del objeto en donde se almacenar�
 * el resultado.
 * 
 * @author LSI.
 *
 */
public interface IServidorReparaciones {
	
	/**
	 * Obtiene la reparaci�n cuyo id se especifica si existe en la 
	 * base de datos.
	 * 
	 * @param id Identificador de la reparaci�n.
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
	 * Da de alta una nueva reparaci�n.
	 * 
	 * @param conn conexi�n con la base de datos.
	 * @param id identificador de la reparaci�n.
	 * @param mat matricula del coche.
	 * @param taller identificador del taller.
	 * @param inicio fecha de inicio de la reparaci�n.
	 */
	void nuevaReparcacion(String id, String mat, String taller, Date inicio, IFuturo f);
	
	/**
	 * Da por terminada una reparaci�n.
	 * 
	 * @param conn conexi�n con la base de datos.
	 * @param id identificador de la reparaci�n.
	 * @param fin fecha de fin de la reparaci�n.
	 * @param precio precio de la reparaci�m.
	 */
	void cerrarReparacion(String id, Date fin, int precio, IFuturo f);
	
}
