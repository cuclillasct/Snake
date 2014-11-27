
package p2.basic;

/**
 * Models the interface of an autonomous object.
 * @author lsi
 *
 */
public interface IAutonomousObject extends IGameObject {
	
	/**
	 * Determine the direction in which the object want to be moved.
	 * It does not cause the motion of the object, because
	 * only the game can change the object position 
	 * @param board representation of the game objects in the game board.
	 * @return direction in which the object want to be moved.
	 */
	public int getDirection(char[][] board);
	
	/**
	 * Determine the position in which the object want to be located.
	 * It does not cause the motion of the object, because
	 * only the game can change the object position 
	 * @param board representation of the game objects in the game board.
	 * @return position in which the object want to be located.
	 */
	public Coordinate getNextCoordinate(char[][] board);
}
