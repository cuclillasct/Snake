package p2.model_impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import p2.basic.Coordinate;
import p2.basic.IAutonomousObject;
import p2.basic.IGame;
import p2.basic.IGameCharacter;
import p2.basic.IGameConstants;
import p2.basic.IGameObject;
import p2.basic.IJSONizable;
import p2.basic.IView;
import p2.basic.NoMobileObjectException;
import p2.basic.ParamException;
import p2.basic.tooMuchShiftException;
import p2.views_impl.GamePanel;
import p2.views_impl.VImage;
import utilidades.Utilidades;


public class Game_0 extends JFrame implements IGame, KeyListener, ActionListener {
	
	public static final String PATH = "C:/Users/Jorge/Desktop/game.json";
	
	// Identifiers ................................................
	String ownerId = "Unknown";
	String gameId = "Unknown";
	
	// Board size .................................................
	int nRows = 20;
	int nCols = 20;
	int edge = 30;
	
	// Timer ......................................................
	int ticks;
    Timer timer;
    int tick = 200;
    
    // Modes of operation .........................................
    // Auto mode active: game calls snake algorithm to obtain next coordinate.
    // Auto mode inactive: snake is commanded with the arrow keys.
    boolean autoModeActive = false;
    int estrat = 1;
    boolean isStopped;
    
    // Next coordinate for snake. Update in key listener in manual mode.
    int currentDirection = IGameConstants.Right;

    // Game objects.....................................................
    ConcurrentLinkedQueue<IGameObject> objects = new ConcurrentLinkedQueue<>();
    SnakeAutonomous0 aSnake;
	
    // GUI Elements......................................................
    GamePanel panelJuego;
    JPanel buttons;
    JButton incSpd, decSpd;
    
    // Control Game mode and game speed (in menu bar).
    boolean wantSave = false;
    JMenuBar mBar;
    JMenu mFile;
    JMenuItem itSaveToFile, itLoadFromFile, itRestartGame;
    
    // Game info
    InfoPanel pnInfo;
    int nPoints = 0, nBugs = 0;
    
    int lvl = 1;
    
    JFrame frame;
    
    Thread thread;
    Thread music;
    
    public Game_0(int rows, int cols, int level, boolean autoMode, int estrategia) throws IOException {
    	
    	frame = this;

    	autoModeActive = autoMode;
    	estrat = estrategia;
    	lvl = level;
    	
    	music = new Thread(new Runnable() {
    	    public void run() {
    	    	File file = new File(System.getProperty("user.dir")+"/resources/8bit.mp3");
	             FileInputStream fis;
				try {
					fis = new FileInputStream(file);
					BufferedInputStream bis = new BufferedInputStream(fis);
					Player player = new Player(bis);
		      		player.play();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (JavaLayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
    	});
    	music.start();
    	
        nRows = rows; nCols = cols;
    	
    	// WINDOW ELEMENTS .......................................    	
        panelJuego = new GamePanel(rows, cols, edge);
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelJuego);  
//        getContentPane().add(new JLabel("    "), BorderLayout.WEST);  
//        getContentPane().add(new JLabel("    "), BorderLayout.EAST);  
//        getContentPane().add(new JLabel("    "), BorderLayout.NORTH);  
        
        buttons = new JPanel();
        buttons.setLayout(new GridLayout(2,1));
        incSpd = new JButton("+ Speed");
        incSpd.addActionListener(this);
        decSpd = new JButton("- Speed");
        decSpd.addActionListener(this);
        buttons.add(incSpd, BorderLayout.NORTH);
        buttons.add(decSpd, null);
        getContentPane().add(buttons, BorderLayout.EAST);
                
        buildMenuBar();
        pnInfo = new InfoPanel();
        
        JPanel sP = new JPanel();
        sP.setLayout(new BorderLayout());
        sP.add(pnInfo, BorderLayout.NORTH);
        getContentPane().add(sP, BorderLayout.SOUTH);  
        
        addKeyListener(this);
        System.out.println(this.getFocusableWindowState());
        this.setFocusable(true);
          
        // Fijamos tamaño de la ventana.       
        setSize (96 + edge * (nRows+2)+6, 87 + edge * (nCols+2)+6);
        // Hacemos la ventana visible.
        setVisible(true);    
        //setResizable(false);
                
        // Forzamos a que la aplicación termine al cerrar la ventana.     
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        
    	// GAME OBJECTS  .......................................    
        fillRandom(lvl);
        aSnake = new SnakeAutonomous0("link", "link", 1, new Coordinate(0,0));
        aSnake.setEstrategia(estrategia);
        objects.add(aSnake);
        
        // Create and start timer.
		timer = new Timer(tick, this);
		timer.start();
    }
    
    public void initFromFile (String path) throws IOException, JSONException, ParamException{
    	JSONObject jObj = new JSONObject(Utilidades.leerFichero(path));
    	objects.clear();
    	currentDirection = jObj.getInt(DirectionLabel);
    	nRows = jObj.getInt(RowsLabel);
    	nCols = jObj.getInt(ColsLabel);
    	aSnake = new SnakeAutonomous0(jObj.getJSONObject(SnakeLabel));
    	autoModeActive = jObj.getBoolean(AutoLabel);
    	tick = jObj.getInt(TickLabel);
    	timer = new Timer (tick ,this);
    	ticks = jObj.getInt(TicksLabel);
    	JSONArray jarr = jObj.getJSONArray(ObjectsLabel);
    	int auxN = 1;
		for (int i = 0; i < jarr.length(); i++){
			switch (jarr.getJSONObject(i).getString(IJSONizable.TypeLabel)) {
			case "p2.model_impl.SnakeAutonomous0":
				aSnake = new SnakeAutonomous0(jarr.getJSONObject(i));
				objects.add(aSnake);
				System.out.println("Serpiente cargada");	
				break;
			case "p2.model_impl.BugAutonomous0":
				objects.add(new BugAutonomous0(jarr.getJSONObject(i)));
				System.out.println("Bicho cargado");	
				break;
			case "p2.model_impl.Fruit":
				objects.add(new Fruit(jarr.getJSONObject(i)));
				break;
			case "p2.model_impl.Obstacle":
				objects.add(new Obstacle(jarr.getJSONObject(i)));
				break;
			case "p2.model_impl.SnakeLink":
				SnakeLink snkLink = new SnakeLink(jarr.getJSONObject(i));
				objects.add(aSnake.getLinks().get(auxN++));
				System.out.println("Enlace cargado");
				break;
			default:
				break;
			}
		}
		panelJuego.updateGame(toCharMatrix());
		timer.start();
	}
    
    ////////////////////////////////////////////////////////////////////////////////////
    // BUILD MENUS.
    private void buildMenuBar(){
    		
    	// Reiniciando
    	itRestartGame = new JMenuItem("Restart Game");
    	itRestartGame.addActionListener(
    			new ActionListener(){
    				public void actionPerformed(ActionEvent ae){
    					System.out.println("restarting game");
    					timer.stop();
    					music.stop();
    					frame.dispose();
						try {
							new Game_0(nRows,nCols, lvl, autoModeActive, estrat);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				}
    			}
    	);
    	
    	// Saving to a file ...
    	itSaveToFile = new JMenuItem("Save to File");
    	itSaveToFile.addActionListener(
    			new ActionListener(){
    				public void actionPerformed(ActionEvent ae){
    					System.out.println("saving to file ...");
    					wantSave = true;
    				}
    			}
    	);
    	
    	// Loading from a file
    	itLoadFromFile = new JMenuItem("Load from File");
    	itLoadFromFile.addActionListener(
    			new ActionListener(){
    				public void actionPerformed(ActionEvent ae){
    					timer.stop();
    					System.out.println("loading from file ...");	
    					try {
							initFromFile(PATH);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (JSONException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ParamException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
    				}
    			}
    	);
    	
    	mFile = new JMenu("Game Options");
    	mFile.add(itRestartGame);
    	mFile.add(itSaveToFile);
    	mFile.add(itLoadFromFile);
    	
    	mBar = new JMenuBar();
    	mBar.add(mFile);
    	setJMenuBar(mBar);  	
    }
    
    ////////////////////////////////////////////////////////////////////////////////////
    // BUILD INFO PANEL    
    class InfoPanel extends JPanel {

        JLabel lbPoints,lbFruits,lbBugs,lbBugsE;
        
    
	     public InfoPanel(){
	    	lbPoints = new JLabel("Points: "+ nPoints + "    ");
	     	add(lbPoints);
	     	lbFruits = new JLabel("Fruits: "+ getFruit().size() + "    ");
	     	add(lbFruits);
	     	lbBugs = new JLabel("Bugs in Game: "+ getBug().size() + "    ");
	     	add(lbBugs);
	     	lbBugsE = new JLabel("Bugs Eated: "+ nBugs);
	     	add(lbBugsE);
        } 
        
        public void paintComponent(Graphics g){
        	super.paintComponent(g);
        	lbPoints.setText("Points: "+ nPoints + "    ");
        	lbPoints.repaint();
        	lbFruits.setText("Fruits: "+ getFruit().size() + "    ");
        	lbFruits.repaint();
        	lbBugs.setText("Bugs in Game: "+ getBug().size() + "    ");
        	lbBugs.repaint();
        	lbBugsE.setText("Bugs Eated: "+ nBugs);
        	lbBugsE.repaint();
        }
              	     	    
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
			nextCoord = new Coordinate(obj.getCoordinate().getColumn(), obj.getCoordinate().getRow()-1);
			break;
		case IGameConstants.Down:
			nextCoord = new Coordinate(obj.getCoordinate().getColumn(), obj.getCoordinate().getRow()+1);
			break;
		case IGameConstants.Left:
			nextCoord = new Coordinate(obj.getCoordinate().getColumn()-1, obj.getCoordinate().getRow());
			break;
		case IGameConstants.Right:
			nextCoord = new Coordinate(obj.getCoordinate().getColumn()+1, obj.getCoordinate().getRow());
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
	 * @throws tooMuchShiftException 
	 * @throws NoMobileObjectException 
	 */
	public boolean updateObjectPosition(IGameObject obj, Coordinate target) throws NoMobileObjectException, tooMuchShiftException {
		switch (currentDirection) {
		case IGameConstants.Up:
			if (obj.getCoordinate().getRow() > 0) {
				obj.setCoordinate(target);
				return true;
			}
		break;
		case IGameConstants.Down:
			if (obj.getCoordinate().getRow() < nRows-1) {
				obj.setCoordinate(target);
				return true;
			}
		break;
		case IGameConstants.Right:
			if (obj.getCoordinate().getColumn() < nCols-1) {
				obj.setCoordinate(target);
				return true;
			}
		break;
		case IGameConstants.Left:
			if (obj.getCoordinate().getColumn() > 0) {
				obj.setCoordinate(target);
				System.out.println(target.getColumn());
				return true;
			}
		break;
		default:
			return false;
		}		
		return false;
	}
	
	/**
	 * Process object position and update the game consequently.
	 * @param obj object to be processed.
	 * @return true if game can continue.
	 */
	public boolean processObjectPosition(IGameObject obj) {	
		if (obj instanceof Snake) { // Serpiente
			for(IGameObject go : objects){
	   	    	if(go.getCoordinate().getRow()==obj.getCoordinate().getRow() && go.getCoordinate().getColumn()==obj.getCoordinate().getColumn()) {
	   	    		if (go instanceof Obstacle) { //Si es obstaculo pierde el juego
	   	    			return false;
					} else if(go instanceof Fruit){//Si es fruta, desaparece y añade tantos eslabones como el valor de la fruta
						processFruit((Fruit) go);
						return true;
					} else if(go instanceof Bug){//Si es bicho, desaparece y quita tantos eslabones como el valor del bicho
						processBug((BugAutonomous0) go);
						break;
					} else if(go instanceof SnakeLink){//Si es su cola, SE COME LA COLA
						processLink((SnakeLink) go);
						break;
					}
	   	    	}
			}
		} else if(obj instanceof Bug){ // Bicho
			for(IGameObject go : objects){
	   	    	if(go.getCoordinate().getRow()==obj.getCoordinate().getRow() && go.getCoordinate().getColumn()==obj.getCoordinate().getColumn()) {
	   	    		if (go instanceof Obstacle) { //Si es obstaculo el bicho muere
	   	    			objects.remove(obj);
	   	    			return true;
					} else if(go instanceof Fruit){//Si es fruta, desaparece la fruta
						obj.setValue(obj.getValue()+1);
						go.setValue(go.getValue() > 0 ? go.getValue() - 1 : go.getValue());
						return true;
					} else if(go instanceof SnakeLink){//Si es un SnakeLink, quita un eslabon
						thread = new Thread(new Runnable() {
				    	    public void run() {
				    	    	File file = new File(System.getProperty("user.dir")+"/resources/bug.mp3");
					             FileInputStream fis;
								try {
									fis = new FileInputStream(file);
									BufferedInputStream bis = new BufferedInputStream(fis);
									Player player = new Player(bis);
						      		player.play();
								} catch (FileNotFoundException e1) {
									// TODO Auto-generated catch block
									e1.printStackTrace();
								} catch (JavaLayerException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
				    	    }
				    	});
						thread.start();
						SnakeLink lnk = aSnake.removeLink();
						objects.remove(lnk);
						return true;
					}
	   	    	}
			}
		}
		return true; //solo puede ser bicho o snake
	}

	private void processLink(SnakeLink go) {
		int indx = aSnake.getLinks().size()-1;
		for (int i = aSnake.getLinks().indexOf(go); i < indx; i++) {
			SnakeLink lnk = aSnake.removeLink();
			objects.remove(lnk);
		}
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
    	
    	for (int col = 1; col < nCols - 1; col++){
        	// The first row is free.
    		for (int row = 1; row < nRows - 1; row ++){
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
		ArrayList<Obstacle> obs = new ArrayList<Obstacle>();
		for (IGameObject gObject : objects) {
			if(gObject instanceof Obstacle){
				obs.add((Obstacle) gObject);
			}
		}
		return obs;
	}

	/**
	 * Get the fruits of the board.
	 * @return fruits of the board
	 */
	public ArrayList<Fruit> getFruit(){
		ArrayList<Fruit> fruits = new ArrayList<Fruit>();
		for (IGameObject gObject : objects) {
			if(gObject instanceof Fruit){
				fruits.add((Fruit) gObject);
			}
		}
		return fruits;
	}

	/**
	 * Get the bugs of the board.
	 * @return bugs of the board
	 */
	public ArrayList<BugAutonomous0> getBug(){
		ArrayList<BugAutonomous0> bugs = new ArrayList<BugAutonomous0>();
		for (IGameObject gObject : objects) {
			if(gObject instanceof BugAutonomous0){
				bugs.add((BugAutonomous0) gObject);
			}
		}
		return bugs;
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
			else if (obj instanceof Snake) {
				board[obj.getCoordinate().getColumn()][obj.getCoordinate().getRow()] = IGameConstants.SnakeHead;
			}
		}
		return board;
	}
	
	// Actions to be taken when the snake eats a fruit.
	private void processFruit(Fruit f){
		thread = new Thread(new Runnable() {
    	    public void run() {
    	    	File file = new File(System.getProperty("user.dir")+"/resources/fruit.mp3");
	             FileInputStream fis;
				try {
					fis = new FileInputStream(file);
					BufferedInputStream bis = new BufferedInputStream(fis);
					Player player = new Player(bis);
		      		player.play();
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (JavaLayerException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    	    }
    	});
		thread.start();
		for (int i = 0; i < f.getValue()+1; i++) {
			SnakeLink lnk = aSnake.addLink();
			objects.add(lnk);
		}
		aSnake.setNoObjective();
		nPoints += f.getValue();
		objects.remove(f);
	}
	
	// Actions to be taken when the snake eats a bug.
	private void processBug(Bug b){
		for (int i = 0; i < b.getValue()+1; i++) {
			SnakeLink lnk = aSnake.removeLink();
			objects.remove(lnk);
		}
		nPoints += 3*b.getValue(); //Comerse un bicho da 3puntos*los que tenga el bicho (los puntos que ganarias si mantienes los eslabones hasta el final)
		nBugs += 1;
		objects.remove(b);
	}
		
	// Actions to be taken to add a bug to the game.
	private BugAutonomous0 spawnBug(){
		return new BugAutonomous0("bug", "bug", 0, new Coordinate(BugAutonomous0.randInt(0, nCols - 1), BugAutonomous0.randInt(0, nRows - 1)));
	}
	

	
	
    /*********************************************************************************************
     * MANEJADORES DE ENTRADAS DE TECLADO: cambio de dirección en modo manual.
     */
	@Override
	public void keyPressed(KeyEvent arg0) {	
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		int keyCode = arg0.getKeyCode();
		switch(keyCode){
		case IGameConstants.UpKey: currentDirection = IGameConstants.Up; aSnake.setDirection(IGameConstants.Up);
			break;
		case IGameConstants.DownKey:currentDirection = IGameConstants.Down; aSnake.setDirection(IGameConstants.Down);
			break;
		case IGameConstants.LeftKey:currentDirection = IGameConstants.Left; aSnake.setDirection(IGameConstants.Left);
			break;
		case IGameConstants.RightKey:currentDirection = IGameConstants.Right; aSnake.setDirection(IGameConstants.Right);
			break;
		case IGameConstants.SpaceKey:
			if (timer.isRunning()) {
				timer.stop();
			} else {
				timer.start();
			}
			default:break;
		}

	}

	@Override
	public void keyTyped(KeyEvent arg0) {}
	
	
    /*********************************************************************************************
     * MANEJADOR DE TEMPORIZADOR: avance del juego.
     */
	
	public void actionPerformed(ActionEvent arg0) {
		
		if(arg0.getActionCommand() != null){
			switch (arg0.getActionCommand()) {
			case "+ Speed":	
				System.out.println("+ Speed");
				tick -= (tick>19)?20:0;
				timer.setDelay(tick);
				break;
			case "- Speed":
				System.out.println("- Speed");
				tick += 20;
				timer.setDelay(tick);
				break;
			default:
				break;
			}
		}else{
			// Recuperar foco. Necesario para que lleguen
						// eventos de teclado.
						requestFocus();
						
						if (wantSave) {
							timer.stop();
							saveToFile(PATH);
						}
						
						// Añadir link cada 10 ticks de reloj.
						if (ticks%10 == 0){
							SnakeLink lnk = aSnake.addLink();
							objects.add(lnk);	
					        nPoints += 1;
						}
						
						// Añadir bug cada 50 ticks de reloj.		
						if (ticks%50 == 0){
					        objects.add(spawnBug());
						}
						
						// Incrementar velocidad y reiniciar cuenta de
						// ticks cada 100 ticks.
						if (ticks%100 == 0){
							int minimoTick = 80;
							int aumentoTick = 10;
							tick -= (tick > minimoTick)?aumentoTick:0;
							timer.setDelay(tick);
							ticks = 0;	
						}
					
						// Incrementar ticks.
						ticks++;
						
						// Update bugs
						ArrayList<BugAutonomous0> bugs = getBug();
						for (BugAutonomous0 bug : bugs){
								Coordinate next = bug.getNextCoordinate(toCharMatrix());
//								Coordinate next = getNextCoordinate(bug, currentDirection);//
								try {
									updateObjectPosition(bug, next);
								} catch (NoMobileObjectException | tooMuchShiftException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
								processObjectPosition(bug);
						}
						
						// Update snake
						Coordinate nextSnakeCoord = null;
						if (autoModeActive) {
							nextSnakeCoord = aSnake.getNextCoordinate(toCharMatrix());
							currentDirection = aSnake.getDirection(toCharMatrix());
						}
						else{
							nextSnakeCoord = getNextCoordinate(aSnake, currentDirection);
						}
						try {
							if(updateObjectPosition(aSnake, nextSnakeCoord)){ //En cada movimiento, SONIDO MOVE
								
								thread = new Thread(new Runnable() {
						    	    public void run() {
						    	    	File file = new File(System.getProperty("user.dir")+"/resources/move2.mp3");
							             FileInputStream fis;
										try {
											fis = new FileInputStream(file);
											BufferedInputStream bis = new BufferedInputStream(fis);
											Player player = new Player(bis);
								      		player.play();
										} catch (FileNotFoundException e1) {
											// TODO Auto-generated catch block
											e1.printStackTrace();
										} catch (JavaLayerException e) {
											// TODO Auto-generated catch block
											e.printStackTrace();
										}
						    	    }
						    	});
								thread.start();
							}
						} catch (NoMobileObjectException | tooMuchShiftException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if(!processObjectPosition(aSnake)){
							timer.stop();
							//custom title, error icon
							Object[] options = {"Yes, I want to play again!",
				            "No, thanks."};
							int n = JOptionPane.showOptionDialog(panelJuego,
								    "SCORE: "+ (nPoints + aSnake.getLinks().size()*3) +"  PLAY AGAIN?", // Cuando acaba el juego, se suman 3puntos/snakelink
								    "GAME OVER",
								    JOptionPane.YES_NO_OPTION,
								    JOptionPane.ERROR_MESSAGE,
								    null,     //do not use a custom Icon
								    options,  //the titles of buttons
								    options[0]);
							if (n == JOptionPane.YES_OPTION) {
								timer.stop();
								music.stop();
								frame.dispose();
								try {
									new Game_0(nRows,nCols, lvl,autoModeActive,estrat);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else if(n == JOptionPane.CLOSED_OPTION){
								this.dispose();
								System.exit(1);
							}else{
								this.dispose();
								System.exit(1);
							}
						}	

						// Actualizar gráficos del juego.
						panelJuego.updateGame(toCharMatrix());
						
						if(getFruit().size() == 0){
							timer.stop();
							//custom title, error icon
							Object[] options = {"Yes, I want to play again!",
				            "No, thanks."};
							int n = JOptionPane.showOptionDialog(panelJuego,
								    "SCORE: "+ (nPoints + aSnake.getLinks().size()*3) +"  PLAY AGAIN?", // Cuando acaba el juego, se suman 3puntos/snakelink
								    "W",
								    JOptionPane.YES_NO_OPTION,
								    JOptionPane.INFORMATION_MESSAGE,
								    null,     //do not use a custom Icon
								    options,  //the titles of buttons
								    options[0]);
							if (n == JOptionPane.YES_OPTION) {
								timer.stop();
								music.stop();
								frame.dispose();
								try {
									new Game_0(nRows,nCols, lvl,autoModeActive,estrat);
								} catch (IOException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else if(n == JOptionPane.CLOSED_OPTION){
								this.dispose();
								System.exit(1);
							}else{
								this.dispose();
								System.exit(1);
							}
						}
		}
	}
	
	//////////////////////////////////////////////////////////////////////////////

	@Override
	public JSONObject toJSONObject() {
		JSONObject jObj = new JSONObject();
		JSONArray objctsArray = new JSONArray();
		try {
			jObj.put(DirectionLabel, currentDirection);
			jObj.put(SnakeLabel, aSnake.toJSONObject());
			Iterator<IGameObject> it = objects.iterator();int i = 0;
			while (it.hasNext()) {
				objctsArray.put(i, it.next().toJSONObject());i++;
			}
			jObj.put(ObjectsLabel, objctsArray);
			jObj.put(TickLabel, tick);
			jObj.put(TicksLabel, ticks);
			jObj.put(AutoLabel, autoModeActive);
			jObj.put(RowsLabel, nRows);
			jObj.put(ColsLabel, nCols);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObj;
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
		return nRows;
	}

	@Override
	public int getNumberOfColumns() {
		// TODO Auto-generated method stub
		return nCols;
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
		JSONObject json = toJSONObject();
		try {
			Utilidades.escribirEnFichero(json.toString(), "C:/Users/Jorge/Desktop/game.json");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.dispose();
		System.exit(1);
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
	
	public static void main(String [] args) throws IOException, JSONException, ParamException{
        Game_0 gui = new Game_0(15, 15, 3, false, SnakeAutonomous0.BUSCA_FRUTAS_Y_ESQUIVA_OBSTACULOS);
     }

}
