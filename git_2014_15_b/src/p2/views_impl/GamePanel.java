package p2.views_impl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.io.IOException;
import java.util.Hashtable;

import javax.swing.JPanel;

import p2.basic.IGameConstants;
import p2.basic.IView;

/**
 * Models the game drawing panel.
 * @author lsi
 *
 */
public class GamePanel extends JPanel  {
		
    // Board size
	private int cols;
	private int rows;
	private int edge;
	
	// Dictionary that associate keys with views.
	private Hashtable<Character, IView> tViews = new Hashtable<>();
	
	// Use to code the board elements.
	// It is updated by the game by invoking the method updateGame.
	char[][] board;
	
	// Views associated to the different game objects.
	// the students can add others.
	IView vObstacle, vLink, vGrape, vBug, vHead;

	/**
	 * Construct a game panel.
	 * @param cols Number of columns of the game panel
	 * @param rows Number of rows of the game panel
	 * @param edge Size in pixel of the game cells' edge
	 * @throws IOException
	 */
	public GamePanel(int cols, int rows, int edge) throws IOException{
    	this.cols = cols;
    	this.rows = rows;
    	this.edge = edge;
    	
    	setSize(new Dimension(cols*edge, rows*edge));
    	
    	vObstacle = new VSquare("obstacle");
    	vGrape = new VImage("food", "resources/cupcake.png");
    	vLink = new VCircle();  
    	vBug  = new VImage("bug", "resources/bug.jpg");  
    	vHead  = new VImage("head", "resources/snake.png");  
    	
    	tViews.put(IGameConstants.Bug, vBug);
    	tViews.put(IGameConstants.Fruit, vGrape);
    	tViews.put(IGameConstants.Obstacle, vObstacle);
    	tViews.put(IGameConstants.SnakeLink, vLink);
    	tViews.put(IGameConstants.SnakeHead, vHead);
    }
	
	/**
	 * Updates the component drawings according to the 
	 * matrix previosly passed by the game. 
	 */
    public void paintComponent(Graphics g){
    	super.paintComponent(g);
    	if (board != null) {
    		for (int i = 0; i < board.length; i++){
    			for(int j = 0; j < board[i].length; j++){
    				if (board[i][j] != IGameConstants.Free) {
    					IView vi = tViews.get(new Character(board[i][j]));
    					if (vi != null){
    						vi.setSize(edge-3);
    						vi.draw(g, i * edge, j * edge);
    					}
    				}
    			}
    		}
    	}
    	drawGrid(g);
    }  
    
    /**
     * Updates the game grid and repaint the component accordingly.
     * It is invoked by the game when the game state changes.
     * @param board representation of the game objects in the board.
     */
    public void updateGame(char [][] board){
    	this.board = board;
    	repaint();
    }
    
    // Draw  grid.
    private void drawGrid(Graphics g){
    	Color c = g.getColor();
    	g.setColor(Color.gray);
    	int w = 0, h = 0;
    	int i = 0;
    	while(i < cols){
    		w = edge * i;
    		g.drawLine(w, 0, w, rows*edge);
    		i++;
    	}
    	i = 0;
    	while(i < rows){
    		h = edge * i;
    		g.drawLine(0, h, cols*edge, h);
    		i++;
    	}
    	g.setColor(Color.BLUE);
    	g.drawRect(0, 0, cols*edge, rows*edge);
    	g.setColor(c);
    }
}
