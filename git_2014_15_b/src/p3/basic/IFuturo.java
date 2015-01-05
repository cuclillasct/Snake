package p3.basic;

/**
 * Modela el lugar donde el servidor escribe la respuesta 
 * al servicio que posteriormente recoger� al cliente.
 * 
 * @author Juan �ngel
 *
 */
public interface IFuturo {
	
	/**
	 * Devuelve el resultado almacenado en el objeto futuro.
	 * 
	 * @return resultado almacenado en el objeto futuro
	 */
	public String getResult();
	
	/**
	 * Devuelve true si el servicio ha terminado de 
	 * actualizar el resultado almacenado en el futuro
	 * 
	 * @return true si el servicio ha terminado.
	 */
	public boolean isDone();
	
	/**
	 * Almacena un resultado en el futuro.
	 * 
	 * @param result resultado a almacenar en el futuro.
	 */
	public void setResult(String result);
	
	/**
	 * Devuelve el identificador del futuro. 
	 * Se recomienda que cada futuro tenga un id �nico.
	 * 
	 * @return identificador del futuro.
	 */
	public String getId();
	
	/**
	 * M�todo de suscripci�n. Los observadores de los futuros
	 * son notificados cuando el resultado est� listo
	 * para ser recogido.
	 * 
	 * @param obs observador del futuro.
	 */
	public void attach(IObservadorFuturo obs);
}
