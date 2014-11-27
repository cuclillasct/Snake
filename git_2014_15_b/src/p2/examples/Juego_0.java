package p2.examples;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
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
import p2.views_impl.VCircle;
import p2.views_impl.VImage;
import p2.views_impl.VSquare;


public class Juego_0 extends JFrame implements KeyListener {
	
    // Paneles.
    Tablero_0 panelJuego;
    
    // Game object
    SnakeLink aLink;
    
    // Game views
    IView vObstacle, vLink, vFood;
    
    // View Dictionary.
    Hashtable <IGameObject, IView> tViews = new Hashtable<IGameObject, IView>();

    public Juego_0() throws IOException, JSONException, ParamException{
    	
    	// WINDOW ELEMENTS .......................................    	
        // Añadiendo paneles.
        panelJuego = new Tablero_0();
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panelJuego);        
        
        addKeyListener(this);
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
   	    vFood = new VImage("food", "resources/pastel1.jpg");
   	    vLink = new VImage("link", "resources/raton.jpg");  
        
   	    // game objects creation and association with views.
        aLink = new SnakeLink("link", "link", 1, new Coordinate(0,0));
        tViews.put(aLink, vLink);
        for (int i = 0; i < 4; i++){
        	tViews.put(new Obstacle("obs_" + i, "obs", 0, new Coordinate(i+2, i+4)), vObstacle);
        	tViews.put(new Fruit("fruit_" + i, "fruit", 0, new Coordinate(i+2, i+7)), vFood);  
        } 
    }
	
    /*********************************************************************************************
     * MANEJADORES DE ENTRADAS DE TECLADO
     */
    
    public final int UpKey =   38;
    public final int DownKey = 40;
    public final int LeftKey = 37;
    public final int RightKey = 39;
    
	@Override
	public void keyPressed(KeyEvent arg0) {
		System.out.println("keyPressed: " + arg0.getKeyCode());		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		System.out.println("keyReleased: " + arg0.getKeyCode());
		int keyCode = arg0.getKeyCode();
		switch(keyCode){
		case UpKey: 
			if (aLink.getCoordinate().getRow() > 0) {
				aLink.decRow();  
			}
			break;
		case DownKey:
		if (aLink.getCoordinate().getRow() < 9) {
			aLink.incRow();  
		}
		break;
		case LeftKey:
			if (aLink.getCoordinate().getColumn() > 0) {
				aLink.decColumn(); 
			}
			break;
		case RightKey:
			if (aLink.getCoordinate().getColumn() < 9) {
				aLink.incColumn(); 
			}
			break;			
		default:break;
		}
		panelJuego.updateGame();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		System.out.println("keyTyped: " + arg0.getKeyCode());
	}
	
	
    /*********************************************************************************************
     * PANEL DEL TABLERO DE JUEGO.
     */	
	class Tablero_0 extends JPanel {
				
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
        Juego_0 gui = new Juego_0();
     }

}
