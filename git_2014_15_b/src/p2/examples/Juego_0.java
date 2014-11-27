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
import p2.basic.tooMuchShiftException;
import p2.model_impl.Fruit;
import p2.model_impl.Obstacle;
import p2.model_impl.SnakeLink;
import p2.views_impl.VCircle;
import p2.views_impl.VImage;
import p2.views_impl.VSquare;


public class Juego_0 extends JFrame implements KeyListener, ActionListener{
	
    // Paneles.
    Tablero_0 panelJuego;
    
    // Game clock
    Timer timer;
    int periodo = 100;
    int ticks = 100;
    
    // Game object
    ArrayList<SnakeLink> snake = new ArrayList<SnakeLink>();
    SnakeLink aLink, aLink2, aLink3;
    
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
        setSize (375, 398);
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
   	    aLink = new SnakeLink("link", "link", 1, new Coordinate(2,0));
   	    aLink2 = new SnakeLink("link2", "link", 1, new Coordinate(1,0));		
        aLink3 = new SnakeLink("link3", "link", 1, new Coordinate(0,0));
        snake.add(aLink);
   	    snake.add(aLink2);
   	    snake.add(aLink3);
        tViews.put(aLink, vLink);
        tViews.put(aLink2, vLink);
        tViews.put(aLink3, vLink);
        for (int i = 0; i < 4; i++){
        	tViews.put(new Obstacle("obs_" + i, "obs", 0, new Coordinate(i+2, i+4)), vObstacle);
        	tViews.put(new Fruit("fruit_" + i, "fruit", 0, new Coordinate(i+2, i+7)), vFood);  
        } 
        
        // Create and start timer.
     	timer = new Timer(periodo*ticks, this);
     	timer.start();

    }
    
    /*********************************************************************************************
     * Herramientas¿?
     */
    public void syncModelWithView(){
    	
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
		boolean seMueve = true;
		switch(keyCode){
		case UpKey: 
			for(Enumeration<IGameObject> en = tViews.keys(); en.hasMoreElements();){
       	    	IGameObject go = en.nextElement();
       	    	if(go.getCoordinate().getRow()==aLink.getCoordinate().getRow()-1 && go.getCoordinate().getColumn()==aLink.getCoordinate().getColumn() ) {
       	    		if (go.getId().contains("obs_")) { //Si es obstaculo no se mueve
       	    			seMueve=false;
           	    		break;
					} else if(go.getId().contains("fruit_")){ //Si es fruta, desaparece y añade tantos eslabones como el valor de la fruta
						for (int i = 0; i < go.getValue()+1; i++) {
							SnakeLink aNewLink = new SnakeLink("link"+(snake.size()+1), "link", 1, snake.get(snake.size()-1).getCoordinate());
							try {
								IView vNewLink = new VImage("link", "resources/raton.jpg");
								tViews.put(aNewLink, vNewLink);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							snake.add(aNewLink);
						}
						tViews.remove(go);
						break;
					}
       	    	}
       	    }
			if (aLink.getCoordinate().getRow() > 0 && seMueve) {
				try {
					for (int i = snake.size()-1; i >= 0 ; i--) {
						if(i == 0){
							aLink.decRow();
						}else{
							snake.get(i).setCoordinate(snake.get(i-1).getCoordinate());
						}
					}
				} catch (tooMuchShiftException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			break;
		case DownKey:
			for(Enumeration<IGameObject> en = tViews.keys(); en.hasMoreElements();){
       	    	IGameObject go = en.nextElement();
       	    	if(go.getCoordinate().getRow()==aLink.getCoordinate().getRow()+1 && go.getCoordinate().getColumn()==aLink.getCoordinate().getColumn() ) {
       	    		if (go.getId().contains("obs_")) { //Si es obstaculo no se mueve
       	    			seMueve=false;
           	    		break;
					} else if(go.getId().contains("fruit_")){//Si es fruta, desaparece y añade tantos eslabones como el valor de la fruta
						for (int i = 0; i < go.getValue()+1; i++) {
							SnakeLink aNewLink = new SnakeLink("link"+(snake.size()+1), "link", 1, snake.get(snake.size()-1).getCoordinate());
							try {
								IView vNewLink = new VImage("link", "resources/raton.jpg");
								tViews.put(aNewLink, vNewLink);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							snake.add(aNewLink);
						}
						tViews.remove(go);
						break;
					}
       	    	}
       	    }
			if (aLink.getCoordinate().getRow() < 11 && seMueve) {
				try {
					for (int i = snake.size()-1; i >= 0 ; i--) {
						if(i == 0){
							aLink.incRow();
						}else{
							snake.get(i).setCoordinate(snake.get(i-1).getCoordinate());
						}
					}
				} catch (tooMuchShiftException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}
		break;
		case LeftKey:
			for(Enumeration<IGameObject> en = tViews.keys(); en.hasMoreElements();){
       	    	IGameObject go = en.nextElement();
       	    	if(go.getCoordinate().getRow()==aLink.getCoordinate().getRow() && go.getCoordinate().getColumn()==aLink.getCoordinate().getColumn()-1 ) {
       	    		if (go.getId().contains("obs_")) { //Si es obstaculo no se mueve
       	    			seMueve=false;
           	    		break;
					} else if(go.getId().contains("fruit_")){//Si es fruta, desaparece y añade tantos eslabones como el valor de la fruta
						for (int i = 0; i < go.getValue()+1; i++) {
							SnakeLink aNewLink = new SnakeLink("link"+(snake.size()+1), "link", 1, snake.get(snake.size()-1).getCoordinate());
							try {
								IView vNewLink = new VImage("link", "resources/raton.jpg");
								tViews.put(aNewLink, vNewLink);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							snake.add(aNewLink);
						}
						tViews.remove(go);
						break;
					}
       	    	}
       	    }
			if (aLink.getCoordinate().getColumn() > 0 && seMueve) {
				try {
					for (int i = snake.size()-1; i >= 0 ; i--) {
						if(i == 0){
							aLink.decColumn(); 
						}else{
							snake.get(i).setCoordinate(snake.get(i-1).getCoordinate());
						}
					}
				} catch (tooMuchShiftException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			break;
		case RightKey:
			for(Enumeration<IGameObject> en = tViews.keys(); en.hasMoreElements();){
       	    	IGameObject go = en.nextElement();
       	    	if(go.getCoordinate().getRow()==aLink.getCoordinate().getRow() && go.getCoordinate().getColumn()==aLink.getCoordinate().getColumn()+1 ) {
       	    		if (go.getId().contains("obs_")) { //Si es obstaculo no se mueve
       	    			seMueve=false;
           	    		break;
					} else if(go.getId().contains("fruit_")){//Si es fruta, desaparece y añade tantos eslabones como el valor de la fruta
						for (int i = 0; i < go.getValue()+1; i++) {
							SnakeLink aNewLink = new SnakeLink("link"+(snake.size()+1), "link", 1, snake.get(snake.size()-1).getCoordinate());
							try {
								IView vNewLink = new VImage("link", "resources/raton.jpg");
								tViews.put(aNewLink, vNewLink);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							snake.add(aNewLink);
						}
						tViews.remove(go);
						break;
					}
       	    	}
       	    }
			if (aLink.getCoordinate().getColumn() < 11 && seMueve) {
				try {
					for (int i = snake.size()-1; i >= 0 ; i--) {
						if(i == 0){
							aLink.incColumn();
						}else{
							snake.get(i).setCoordinate(snake.get(i-1).getCoordinate());
						}
					}
				} catch (tooMuchShiftException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
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
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		SnakeLink aNewLink = new SnakeLink("link"+(snake.size()+1), "link", 1, snake.get(snake.size()-1).getCoordinate());
		try {
			IView vNewLink = new VImage("link", "resources/raton.jpg");
			tViews.put(aNewLink, vNewLink);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		snake.add(aNewLink);
		panelJuego.updateGame();
	}
	
    /*********************************************************************************************
     * PANEL DEL TABLERO DE JUEGO.
     */	
	class Tablero_0 extends JPanel {
				
	    int lado = 30;
		
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
