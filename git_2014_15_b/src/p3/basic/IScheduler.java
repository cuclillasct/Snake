package p3.basic;

/**
 * Encola peticiones de servicio (method requests).
 * Si se cumplen las condiciones para su ejecuci�n
 * las desencola y ejecuta. 
 * 
 * @author Juan �ngel
 *
 */
public interface IScheduler {
	
	/**
	 * Encola petici�n de servicio.
	 * Este m�todo se ejecuta en el hilo del cliente, pero 
	 * normalmente su ejecuci�n es muy r�pida.
	 * 
	 * @param rq petici�n de servicio.
	 */
	public void putRequest(IMethodRequest rq);
	
	/**
	 * Despacha las peticiones de servicio pendientes a diferentes 
	 * sirvientes. 
	 * Puede funcionar como servidor iterativo o implementar otras pol�ticas
	 * de concurrencia.
	 */
	public void dispatch();
	
	/**
	 * Detiene de forma ordenada al scheduler.
	 * Por ejemplo, no se encolan nuevas peticiones, pero
	 * se sirven las peticiones ya encoladas antes de detener al servidor.
	 * Por ejemplo, se termina de servir la petici�n actual y ya no se sirven m�s...
	 */
	public void stop();
	
	/**
	 * Devuelve true si el planificador est� activo.
	 * @return true si el planificador est� activo
	 */
	public boolean isActive();
}
