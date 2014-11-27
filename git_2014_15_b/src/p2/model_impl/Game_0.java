package p2.model_impl;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.json.JSONException;
import org.json.JSONObject;

import p2.basic.Coordinate;
import p2.basic.IGame;
import p2.basic.IGameCharacter;
import p2.basic.IGameConstants;
import p2.basic.IGameObject;
import p2.basic.ParamException;
import p2.views_impl.GamePanel;


public class Game_0 extends JFrame implements IGame, KeyListener, ActionListener {
	
	// Identifiers ................................................
	String ownerId = "Unknown";
	String gameId = "Unknown";
	
	// Board size .................................................
	int nRows = 20;
	int nCols = 20;
	int edge = 40;
	
	// Timer ......................................................
	int ticks;
    Timer timer;
    int tick = 200;
    
    // Modes of operation .........................................
    // Auto mode active: game calls snake algorithm to obtain next coordinate.
    // Auto mode inactive: snake is commanded with the arrow keys.
    boolean autoModeActive;
    boolean isStopped;
    
    // Next coordinate for snake. Update in key listener in manual mode.
    int currentDirection = IGameConstants.Right;
    

    // Game objects.....................................................
    ConcurrentLinkedQueue<IGameObject> objects = new ConcurrentLinkedQueue<>();
    SnakeAutonomous0 aSnake;
	
    // GUI Elements......................................................
    GamePanel panelJuego;
    
    // Control Game mode and game speed (in menu bar).
    JMenuBar mBar;
    JMenu mFile;
    JMenuItem itSaveToFile, itLoadFromFile;
    
    // Game info
    JPanel pnInfo;
    JLabel lbPoints, lbFruits;
     
    
    public Game_0(int rows, int cols) throws IOException {
    	
        nRows = rows; nCols = cols;
    	
    	// WINDOW ELEMENTS .......................................    	
        panelJuego = new GamePanel(rows, cols, edge);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelJuego);  
        getContentPane().add(new JLabel("    "), BorderLayout.WEST);  
        getContentPane().add(new JLabel("    "), BorderLayout.EAST);  
        getContentPane().add(new JLabel("    "), BorderLayout.NORTH);  
        
        buildMenuBar();
        pnInfo = buildInfoPanel();
        
        JPanel sP = new JPanel();
        sP.setLayout(new BorderLayout());
        sP.add(pnInfo, BorderLayout.NORTH);
        getContentPane().add(sP, BorderLayout.SOUTH);  
        
        addKeyListener(this);
        System.out.println(this.getFocusableWindowState());
        this.setFocusable(true);
          
        // Fijamos tamaño de la ventana.       
        setSize (60 + edge * nRows, 200 + edge * nCols);
        // Hacemos la ventana visible.
        setVisible(true);    
        //setResizable(false);
                
        // Forzamos a que la aplicación termine al cerrar la ventana.     
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        
    	// GAME OBJECTS  .......................................    
        fillRandom(1);
        aSnake = new SnakeAutonomous0("link", "link", 0, new Coordinate(0,0));
        objects.add(aSnake);
  
        // Create and start timer.
		timer = new Timer(tick, this);
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    // BUILD MENUS.
    private void buildMenuBar(){
    		
    	// Saving to a file ...
    	itSaveToFile = new JMenuItem("Save to File ... ");
    	itSaveToFile.addActionListener(
    			new ActionListener(){
    				public void actionPerformed(ActionEvent ae){
    					System.out.println("saving to file ...");			
    				}
    			}
    	);
    	
    	// Loading from a file
    	itLoadFromFile = new JMenuItem("Load to File ... ");
    	itLoadFromFile.addActionListener(
    			new ActionListener(){
    				public void actionPerformed(ActionEvent ae){
    					System.out.println("loading from file ...");			
    				}
    			}
    	);
    	
    	mFile = new JMenu("File");
    	mFile.add(itSaveToFile);
    	mFile.add(itLoadFromFile);
    	
    	mBar = new JMenuBar();
    	mBar.add(mFile);
    	setJMenuBar(mBar);  	
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    // BUILD INFO PANEL
    private JPanel buildInfoPanel(){
    	JPanel p  = new JPanel();
    	p.setLayout(new GridLayout(1,3));
    	lbPoints = new JLabel("Points: ");
    	p.add(lbPoints);
    	lbFruits = new JLabel("Fruits: ");
    	p.add(lbFruits);
    	return p;
    }
    
    
    /////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////
    // GAME LOGIC.
    
    /**
     * Obtiene la coordenada en la que estaría el objeto si se moviera en una 
     * dirección determinada.
     * NO provoca que el objeto cambie su posición
     * @param obj objeto del juego
     * @param direction dirección de movimiento
     * @return coordenada en la que estaría el objeto si se moviera en la 
     * dirección determinada
     */
    public Coordinate getNextCoordinate(IGameObject obj, int direction){
    	
    	if (obj == null){
    		return null;
    	}
    	Coordinate nextCoord = obj.getCoordinate();
    	switch(direction){
		case IGameConstants.Up: 
			nextCoord = new Coordinate(aSnake.getCoordinate().getColumn(), aSnake.getCoordinate().getRow()-1);
			break;
		case IGameConstants.Down:
			nextCoord = new Coordinate(aSnake.getCoordinate().getColumn(), aSnake.getCoordinate().getRow()+1);
			break;
		case IGameConstants.Left:
			nextCoord = new Coordinate(aSnake.getCoordinate().getColumn()-1, aSnake.getCoordinate().getRow());
			break;
		case IGameConstants.Right:
			nextCoord = new Coordinate(aSnake.getCoordinate().getColumn()+1, aSnake.getCoordinate().getRow());
			break;			
		default:break;
		}
    	return nextCoord;
    }
    
    /**
     * Test if the argument can be moved to the target position, test board limits,
     * obstacles, etc.
     * @param obj object to move
     * @param target position where the object is to be moved.
     * @return true if the motion is possible.
     */
	public boolean testObjectPosition(IGameObject obj, Coordinate target){
		return true;
	}
	
	/**
	 * Move obj to the target position
	 * @param obj game object
	 * @param target postition where obj is to be placed
	 * @return true if the motion has been done.
	 */
	public boolean updateObjectPosition(IGameObject obj, Coordinate target) {
		return true;
	}
	
	/**
	 * Process object position and update the game consequently.
	 * @param obj object to be processed.
	 * @return true if game can continue.
	 */
	public boolean processObjectPosition(IGameObject obj) {	
		return true;
	}

	/**
	 * Generate obstacles and fruits randomly
	 * As greater the level as much the obstacles
	 * @param level level of difficulty
	 */
	public void fillRandom(int level){
    	
		objects.clear();
		
    	double pFruit = 0.05; // Probability of fruit in cell
    	double pObsta = 0.02 * level;
    	double selector = 0;
    	
    	pObsta = (pObsta <= 0.16)?pObsta:0.16;
    	
    	for (int col = 0; col < nCols; col++){
        	// The first row is free.
    		for (int row = 1; row < nRows; row ++){
    			selector = Math.random();
    			if (selector < pFruit) {
    				objects.add(new Fruit("fruit", "fruit", 5, new Coordinate(col, row))); 
    			}
    			else if (selector > pFruit && selector < (pFruit + pObsta)){
    				objects.add(new Obstacle("obs", "obs", 0, new Coordinate(col, row)));     				
    			}
    		}
    	}
    }
	
	/**
	 * Get the obstacles of the board.
	 * @return obstacles of the board
	 */
	public ArrayList<Obstacle> getObstacles(){
		return null;
	}

	/**
	 * Get the fruits of the board.
	 * @return fruits of the board
	 */
	public ArrayList<Fruit> getFruit(){
		return null;
	}

	/**
	 * Get the bugs of the board.
	 * @return bugs of the board
	 */
	public ArrayList<BugAutonomous0> getBug(){
		return null;
	}
	
	/**
	 * Gets all the game objects locates at a given coordinate.
	 * @param coord coordinate
	 * @return game objects locates at the coordinate
	 */
	public ArrayList<IGameObject> getObjectsAt(Coordinate coord) {
		
		ArrayList<IGameObject> list = new ArrayList<>();
		
		if (coord == null){
			return null;
		}
		for (IGameObject obj : objects){
			if (coord.compareTo(obj.getCoordinate()) == 0){
				list.add(obj);
			}
		}
		return list;
	}
	

	/**
	 * Returns a char representation of the board.
	 * @return char representation of the board.
	 */
	public char [][] toCharMatrix(){
		char [][] board = new char[nCols][nRows];
		
		for (int i = 0; i < board.length; i++){
			for (int j = 0; j < board[i].length; j++){
				board[i][j] = IGameConstants.Free;
			}
		}
		for (IGameObject obj : objects){
			if (obj instanceof Obstacle) 
				board[obj.getCoordinate().getColumn()][obj.getCoordinate().getRow()] = IGameConstants.Obstacle;
			else if (obj instanceof Fruit) 
				board[obj.getCoordinate().getColumn()][obj.getCoordinate().getRow()] = IGameConstants.Fruit;
			else if (obj instanceof Bug) 
				board[obj.getCoordinate().getColumn()][obj.getCoordinate().getRow()] = IGameConstants.Bug;
			else if (obj instanceof SnakeLink) 
				board[obj.getCoordinate().getColumn()][obj.getCoordinate().getRow()] = IGameConstants.SnakeLink;
			else if (obj instanceof Snake) 
				board[obj.getCoordinate().getColumn()][obj.getCoordinate().getRow()] = IGameConstants.SnakeHead;
		}
		return board;
	}
	
	// Actions to be taken when the snake eats a fruit.
	private void processFruit(Fruit f){
		
	}
	
	// Actions to be taken when the snake eats a bug.
	private void processBug(Bug b){
	}
		
	// Actions to be taken to add a bug to the game.
	private BugAutonomous0 spawnBug(){
		return null;
	}
	

	
	
    /*********************************************************************************************
     * MANEJADORES DE ENTRADAS DE TECLADO: cambio de dirección en modo manual.
     */
	@Override
	public void keyPressed(KeyEvent arg0) {	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		if (!autoModeActive) {
			int keyCode = arg0.getKeyCode();
			switch(keyCode){
			case IGameConstants.UpKey: currentDirection = IGameConstants.Up;
				break;
			case IGameConstants.DownKey:currentDirection = IGameConstants.Down;
				break;
			case IGameConstants.LeftKey:currentDirection = IGameConstants.Left;
				break;
			case IGameConstants.RightKey:currentDirection = IGameConstants.Right;
				break;			
			default:break;
			}
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	
    /*********************************************************************************************
     * MANEJADOR DE TEMPORIZADOR: avance del juego.
     */
	
	public void actionPerformed(ActionEvent arg0) {
		
		// Recuperar foco. Necesario para que lleguen
		// eventos de teclado.
		requestFocus();
        
		// Añadir link cada 10 ticks de reloj.
		if (ticks%10 == 0){
			SnakeLink lnk = aSnake.addLink();
			objects.add(lnk);			
		}
		
		// Añadir bug cada 50 ticks de reloj.		
		if (ticks%50 == 0){
	        objects.add(spawnBug());
		}
		
		// Incrementar velocidad y reiniciar cuenta de
		// ticks cada 100 ticks.
		if (ticks%100 == 0){
			tick -= (tick > 50)?50:0;
			timer.setDelay(tick);
			ticks = 0;	
		}
	
		// Incrementar ticks.
		ticks++;
		
		// Update bugs
		ArrayList<BugAutonomous0> bugs = getBug();
		for (BugAutonomous0 bug : bugs){
				Coordinate next = bug.getNextCoordinate(toCharMatrix());
				updateObjectPosition(bug, next);
				processObjectPosition(bug);
		}
		
		// Update snake
		Coordinate nextSnakeCoord = null;
		if (autoModeActive) {
			nextSnakeCoord = aSnake.getNextCoordinate(toCharMatrix());
		}
		else{
			nextSnakeCoord = getNextCoordinate(aSnake, currentDirection);
		}
		updateObjectPosition(aSnake, nextSnakeCoord);
		processObjectPosition(aSnake);
		
		// Actualizar gráficos del juego.
		panelJuego.updateGame(toCharMatrix());
	}
	

	
    public static void main(String [] args) throws IOException, JSONException, ParamException{
        Game_0 gui = new Game_0(20, 20);
     }
	
	//////////////////////////////////////////////////////////////////////////////

	@Override
	public JSONObject toJSONObject() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String serialize() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IGameCharacter getCharacter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<IGameObject> getItems() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getNumberOfRows() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNumberOfColumns() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void printBoard() {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveToBD(String connURL) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveToFile(String directory) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public IGameObject getItem(Coordinate coord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOwnerId() {
		// TODO Auto-generated method stub
		return null;
	}
}
