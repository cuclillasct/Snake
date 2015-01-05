package p3.basic;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Representa una llamada a un método en el objeto activo.
 * Proporciona guardas para chequear si puede ejecutarse.
 * @author TIC-LSI
 *
 */
public interface IMethodRequest {
	/**
	 * Ejecuta la petición de servicio.
	 * @throws SQLException
	 */
	void execute();
	
}
