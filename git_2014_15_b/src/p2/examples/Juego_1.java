package p2.examples;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

import org.json.JSONException;

import p2.basic.Coordinate;
import p2.basic.IGameObject;
import p2.basic.IView;
import p2.basic.ParamException;
import p2.model_impl.Fruit;
import p2.model_impl.Obstacle;
import p2.model_impl.SnakeLink;
import p2.views_impl.VImage;
import p2.views_impl.VSquare;

public class Juego_1 extends JFrame implements ActionListener {
    // Paneles.
    Tablero_1 panelJuego;
    
    // Game clock
    Timer timer;
    int periodo = 200;
    int ticks = 0;
    
    // Game objects
    SnakeLink aLink;
    
    
    // Game views
    IView vObstacle, vLink, vFood;
    
    // View Dictionary.
    Hashtable <IGameObject, IView> tViews = new Hashtable<IGameObject, IView>();
    

    public Juego_1() throws IOException, JSONException, ParamException{
    	
    	// WINDOW ELEMENTS .......................................    	
        // Añadiendo paneles.
        panelJuego = new Tablero_1();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelJuego);        
        
        System.out.println(this.getFocusableWindowState());
        this.setFocusable(true);
          
        // Fijamos tamaño de la ventana.       
        setSize (600, 900);
        // Hacemos la ventana visible.
        setVisible(true);    
                
        // Forzamos a que la aplicación termine al cerrar la ventana.     
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        
    	// GAME OBJECTS AND VIEWS ELEMENTS .......................................    
        
        // views
   	    vObstacle = new VSquare("obstacle");
   	    vFood = new VImage("fruit", "resources/pastel1.jpg");
   	    vLink = new VImage("link", "resources/raton.jpg");  
        
   	    // game objects creation and association with views.
        aLink = new SnakeLink("link", "link", 1, new Coordinate(0,0));
        tViews.put(aLink, vLink);
        for (int i = 0; i < 3; i++){
        	tViews.put(new Obstacle("obs_" + i, "obs", 0, new Coordinate(i+2, i+4)), vObstacle);
        	tViews.put(new Fruit("fruit_" + i, "fruit", 0, new Coordinate(i+2, i+7)), vFood);        	
        }   
        
        // Create and start timer.
		timer = new Timer(periodo, this);
		timer.start();

    }
	
    /*********************************************************************************************
     * MANEJADOR DE TEMPORIZADOR
     */
    public static final int Up    = 1;
    public static final int Down  = 2;
    public static final int Rigth = 3;
    public static final int Left  = 4;
    
    int currentDirection = Rigth;
	int nextCol = 0, nextRow = 0;
    
	public void actionPerformed(ActionEvent arg0) {
		
		if (currentDirection == Rigth) {
			nextCol = aLink.getCoordinate().getColumn() + 1;
			if (nextCol < 9) {
				aLink.incColumn();
			}
			else{
				currentDirection = Down;
			}
		}
		if (currentDirection == Down) {
			nextRow = aLink.getCoordinate().getRow() + 1;
			if (nextRow < 9) {
				aLink.incRow();
			}
			else{
				currentDirection = Left;
			}
		}
		if (currentDirection == Left) {
			nextCol = aLink.getCoordinate().getColumn() - 1;
			if (nextCol > 0) {
				aLink.decColumn();
			}
			else{
				currentDirection = Up;
			}
		}
		if (currentDirection == Up) {
			nextRow = aLink.getCoordinate().getRow() -1;
			if (nextRow > 0) {
				aLink.decRow();
			}
			else{
				currentDirection = Rigth;
			}
		}
		panelJuego.updateGame();
		
	}
    
	
	
    /*********************************************************************************************
     * PANEL DEL TABLERO DE JUEGO.
     */	
	class Tablero_1 extends JPanel {
				
	    int lado = 60;
		
        public void paintComponent(Graphics g){
       	    super.paintComponent(g);
       	    for(Enumeration<IGameObject> en = tViews.keys(); en.hasMoreElements();){
       	    	IGameObject go = en.nextElement();
       	    	IView vi = tViews.get(go);
       	    	vi.setSize(lado);
       	    	vi.draw(g, go.getCoordinate().getColumn() * lado, go.getCoordinate().getRow() * lado);
       	    }
       	    drawGrid(g);
        }  
        
        public void updateGame(){
        	repaint();
        }
        

        // Pinta cuadricula.
        private void drawGrid(Graphics g){
        	Color c = g.getColor();
        	g.setColor(Color.gray);
        	int w = 0, h = 0;
        	int i = 0;
        	while(w <= this.getWidth()){
        		w = lado * i;
        		g.drawLine(w, 0, w, this.getHeight());
        		i++;
        	}
        	i = 0;
        	while(h <= this.getHeight()){
        		h = lado * i;
        		g.drawLine(0, h, this.getWidth(), h);
        		i++;
        	}
        	g.setColor(c);
        }
	}
	
	
    public static void main(String [] args) throws IOException, JSONException, ParamException{
        Juego_1 gui = new Juego_1();
     }

}
