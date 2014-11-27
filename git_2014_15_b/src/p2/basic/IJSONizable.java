package p2.basic;

import org.json.JSONObject;

/**
 * 
 * Describe la interfaz de serialización de los objetos del juego.
 * @author lsi-japf
 *
 */
public interface IJSONizable {
	/**
	 * En todos los datos serializados deberá existir el par:
     * “obj_type” = class_name
     * El valor class_name es el nombre de la clase a la que pertenece el objeto serializado. 
     * Este campo será utilizado para saber (o comprobar) la clase del objeto a reconstruir (deserializar).
	 */
	public static final String TypeLabel = "obj_type";
	
	/**
	 * Devuelve una representación del objeto en formato JSON.
	 * @return representación del objeto en formato JSON
	 */
	public JSONObject toJSONObject();
	
	/**
	 * Devuelve el objeto JSON en formato texto.
	 * @return objeto JSON en formato texto.
	 */
	public String serialize();
}
