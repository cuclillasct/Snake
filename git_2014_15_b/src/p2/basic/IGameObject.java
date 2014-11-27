package p2.basic;

/**
 * Modela un elemento del juego
 * @author lsi-japf
 *
 */
public interface IGameObject extends IJSONizable {

	/**
	 * Obtiene el identificador del elemento.
	 * Todos los elementos del juego tienen un identificador único.
	 * @return identificador del elemento.
	 */
	public String getId();
	
	/**
	 * Devuelve una descripción textual del elemento.
	 * @return descripción textual del elemento.
	 */
	public String getDescription();
	
	/**
	 * Devuelve el valor asociado al elemento.
	 * @return valor asociado al elemento
	 */
	public int getValue();
	
	/**
	 * Fija el valor asociado al elemento.
	 * @param value valor asociado al elemento.
	 */
	public void setValue(int value);
	
	/**
	 * Devuelve la coordenada del tablero en la que se
	 * encuentra el elemento.
	 * @return coordenada del tablero en la que se
	 * encuentra el elemento
	 */
	public Coordinate getCoordinate();
	
	/**
	 * Fija la coordenada del tablero en la que se
	 * encuentra el elemento.
	 * @param coord coordenada del tablero en la que se
	 * coloca al elemento
	 * @throws NoMobileObjectException se lanza si el objeto no puede moverse de una 
	 * coordenada a otra.
	 * @throws tooMuchShiftException se lanza si la amplitud del movimiento es mayor 
	 * que la permitida para ese elemento.
	 */
	public void setCoordinate(Coordinate coord) throws NoMobileObjectException, tooMuchShiftException;
}
