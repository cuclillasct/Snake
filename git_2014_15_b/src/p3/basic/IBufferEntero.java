package p3.basic;

/**
  Define una intefaz común para todos los contenedores de valores 
  entero.

  @author  TIC-LSI
  @version CI 2008/09
*/
public interface IBufferEntero {
    
	/**
	 * Inserta un nuevo valor entero en el buffer.
	 * @param val valor insertado.
	 */
	public void set(int val);
	
	/**
	 * Devuelve un valor contenido en el buffer. El valor devuelto
	 * depende de la disciplina de cola utilizada en la implementación.
	 * @return valor devuelto. depende de la disciplina de cola utilizada 
	 *         en la implementación.
	 */
	public int get();
	
	public int capacidad();
}
