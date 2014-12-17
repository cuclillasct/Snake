package p2.basic;

import java.util.ArrayList;

/**
 * Modela el juego (una partida del juego)
 * @author lsi-japf
 *
 */
public interface IGame extends IJSONizable {

	public static final String DirectionLabel = "Direction";
	public static final String SnakeLabel = "Snake";
	public static final String ObjectsLabel = "Objects";
	public static final String AutoLabel = "ModeAuto";
	public static final String TickLabel = "TimerTick";
	public static final String TicksLabel = "Ticks";
	public static final String RowsLabel = "Rows";
	public static final String ColsLabel = "Cols";

	/**
	 * Devuelve el identificador del juego (en realidad de la partida en curso).
	 * @return identificador del juego (en realidad de la partida en curso).
	 */
	public String getId();
	
	/**
	 * DEvuelve el id del jugador que ha iniciado la partida
	 * @return id del jugador que ha iniciado la partida
	 */
	public String getOwnerId();
	
	/**
	 * Devuelve al personaje del juego.
	 * @return personaje del juego.
	 */
	public IGameCharacter getCharacter();
	
	/**
	 * Devuelve un array con los elementos del juego, excepto al personaje
	 * del juego y a aquellos elementos de los que está formado.
	 * Se aconseja que las implementaciones devuelvan copias de los
	 * elementos del jego y no referencias a las elementos originales.
	 * 
	 * @return array con los elementos del tablero.
	 */
	public ArrayList<IGameObject> getItems();
	
	/**
	 * Devuelve el elemento del juego que está en la
	 * coordenada (fila, columna) especificada. 
	 * Se aconseja que las implementaciones devuelvan copias de los
	 * elementos del juego y no referencias a las elementos originales.
	 * 
	 * @param coord lugar del tablero en el que se mira.
	 * 
	 * @return elemento del juego que hay en coord. devuelve null si en esa
	 * coordenada no hay ningún elemento.
	 */
	public IGameObject getItem(Coordinate coord);
	
	
	/**
	 * Devuelve el número de filas del tablero.
	 * @return número de filas del tablero.
	 */
	public int getNumberOfRows();
	
	/**
	 * Devuelve el número de columnas del tablero.
	 * @return número de columnas del tablero.
	 */
	public int getNumberOfColumns();
	

	/**
	 * Imprime el tablero en consola. Pensado para depuración.
	 */
	public void printBoard();
	
	/**
	 * Salva el juego en una base de datos
	 * @param connURL identificador de la base de datos.
	 */
	public void saveToBD(String connURL);
	
	/**
	 * Salva el juego en diferentes ficheros en un fichero determinado.
	 * @param directory directorio donde salvar el juego.
	 */
	public void saveToFile(String file);
	
	
}
