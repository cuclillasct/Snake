package p3.basic;

/**
 * Modela a los observadores de los futuros
 * @author Juan �ngel
 *
 */
public interface IObservadorFuturo {
	
	/**
	 * Informa al observador de que el resultado est�
	 * listo.
	 * @param idFuturo identificador del futuro.
	 */
	public void done(String idFuturo);
}
