package p2.basic;

import java.awt.Graphics;

/**
 * Models the view of the game objects.
 * All the views are expected to fit in a square of a given edge.
 * @author lsi-japf
 *
 */
public interface IView {
	
	/**
	 * Returns the identifier of the view.
	 * @return
	 */
	public String getId();
	
	/**
	 * Makes that the view draws itself in a given praphical
	 * context.
	 * @param g Graphical context
	 * @param x coordinate x in pixels.
	 * @param y coordinate y in pixels.
	 */
	public void draw(Graphics g, int x, int y);
	
	/**
	 * Returns the size in pixels of the edge of the square that 
	 * occupies the view.
	 * @return size in pixels of the edge of the square that 
	 * occupies the view.
	 */
	public int getSize();
	
	/**
	 * Set the size in pixels of the edge of the square that 
	 * occupies the view
	 * @param edge size in pixels of the edge of the square that 
	 * occupies the view.
	 */
	public void setSize(int edge);
}
