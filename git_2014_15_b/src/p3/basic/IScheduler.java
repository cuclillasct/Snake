package p3.basic;

/**
 * Encola peticiones de servicio (method requests).
 * Si se cumplen las condiciones para su ejecución
 * las desencola y ejecuta. 
 * 
 * @author Juan Ángel
 *
 */
public interface IScheduler {
	
	/**
	 * Encola petición de servicio.
	 * Este método se ejecuta en el hilo del cliente, pero 
	 * normalmente su ejecución es muy rápida.
	 * 
	 * @param rq petición de servicio.
	 */
	public void putRequest(IMethodRequest rq);
	
	/**
	 * Despacha las peticiones de servicio pendientes a diferentes 
	 * sirvientes. 
	 * Puede funcionar como servidor iterativo o implementar otras políticas
	 * de concurrencia.
	 */
	public void dispatch();
	
	/**
	 * Detiene de forma ordenada al scheduler.
	 * Por ejemplo, no se encolan nuevas peticiones, pero
	 * se sirven las peticiones ya encoladas antes de detener al servidor.
	 * Por ejemplo, se termina de servir la petición actual y ya no se sirven más...
	 */
	public void stop();
	
	/**
	 * Devuelve true si el planificador está activo.
	 * @return true si el planificador está activo
	 */
	public boolean isActive();
}
