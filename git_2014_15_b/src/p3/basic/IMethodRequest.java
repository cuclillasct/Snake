package p3.basic;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Representa una llamada a un m�todo en el objeto activo.
 * Proporciona guardas para chequear si puede ejecutarse.
 * @author TIC-LSI
 *
 */
public interface IMethodRequest {
	/**
	 * Ejecuta la petici�n de servicio.
	 * @throws SQLException
	 */
	void execute();
	
}
