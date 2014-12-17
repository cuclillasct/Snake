package p2.model_impl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.color.ColorSpace;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableColumn;

import org.json.JSONException;

import p2.basic.ParamException;
import p2.examples.Ej1_UnaVentana;
import p2.examples.Juego_0;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.Player;

public class InitWindow extends JFrame implements ActionListener {

	
	public static final String [] 
    		clavesVistas = {"Juego Manual", "Juego Automático","Salir"};
    /**
	 * 
	 */
	private static final long serialVersionUID = -276938680268304313L;

	String claveSeleccionada = null;
	
	String fotogramaCara = "snake.png";
	String fotogramaTitle = "title.png";
	PanelGrafica title;
	
	JFrame frame;
	
	Thread thread;
	
	// Timer ......................................................
		int ticks;
	    Timer timer;
	    int tick = 200;

	JLabel author;
	    
	public InitWindow() throws UnsupportedAudioFileException, IOException, LineUnavailableException{
		
       super("Ventana de Inicio");
    
       frame = this;
       
       thread = new Thread(new Runnable() {
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
       thread.start();
       
       // Fijamos tamaño de la ventana.       
       setSize (400,620);
       setContentPane(new JLabel(new ImageIcon("resources/back1.jpg")));
       setLayout(new BoxLayout(getContentPane(), JFrame.EXIT_ON_CLOSE));
       
       author = new JLabel();
       author.setText("Jorge Mendoza Saucedo ©Copyright 2014");
       author.setPreferredSize(new Dimension(400,20));
       
       // Creamos los dos paneles.
       title = new PanelGrafica();
       PanelBotones butt = new PanelBotones();
       
       title.setPreferredSize(new Dimension(400, 400));
       butt.setPreferredSize(new Dimension(400, 200));
       
       // Añadimos paneles a la ventana (a su contentPane).
       getContentPane().add(title);
       getContentPane().add(butt);
       
       // Hacemos la ventana visible.
       setVisible(true);  
       
       // Forzamos a que la aplicación termine al cerrar la ventana.     
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);     
       
    // Create and start timer.
		timer = new Timer(tick, this);
		timer.start();
    }
	
	/**
	    * Clase interna: 
	    * Utiliza layout por defecto.
	    * 
	    */
	    class PanelGrafica extends JPanel {

	         JLabel lb;
	         
	         // Creamos la imagen a partir de un fichero.
	         String path = "resources/"+fotogramaTitle;
	         String path2 = "resources/"+fotogramaCara;
	         ImageIcon imi = new ImageIcon(path);
	         ImageIcon imi2 = new ImageIcon(path2);
	         Image cara = imi2.getImage();
	         Image tit = imi.getImage();
	     
		     public PanelGrafica(){
		    	setBackground(new Color(200,200,200,0));
	         } 
	         
	         public void paintComponent(Graphics g){
	        	 super.paintComponent(g);
	        	 path2 = "resources/"+fotogramaCara;
	        	 imi2 = new ImageIcon(path2);
	        	 cara = imi2.getImage();
	        	 path = "resources/"+fotogramaTitle;
	        	 imi = new ImageIcon(path);
	        	 tit = imi.getImage();
	        	 g.drawImage(cara, (this.getWidth()/2)-cara.getWidth(this)/2, 20, this); 
	             g.drawImage(tit, this.getWidth()/2-tit.getWidth(this)/2, cara.getHeight(this), this); 
	             
	         }
	               	     	    
		}
	    
	    /**
		    * Clase interna: 
		    * Utiliza layout por defecto.
		    * 
		    */
		    class PanelBotones extends JPanel implements ActionListener{

		         JLabel lb;
		         
		         JButton btControl [] = new JButton[3];
		     
			     public PanelBotones(){
			    	 
			    	 setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			    	 
			    	 setBackground(new Color(200,200,200,0));
			    	 
			    	 for (int i = 0; i < btControl.length; i++){
		        	        btControl[i] = new JButton(clavesVistas[i]);
		        	        btControl[i].addActionListener(this);
		        	        btControl[i].setAlignmentX(CENTER_ALIGNMENT);
		        	        btControl[i].setMargin(new Insets(10, 15, 10, 15));
		 	            add(btControl[i]); 
		 	           add(Box.createVerticalGlue());
		 	        }
	        	     author.setAlignmentX(CENTER_ALIGNMENT);
	        	     author.setForeground(Color.lightGray);
			    	 add(author);  	 
		         } 
		         

				@Override
				public void actionPerformed(ActionEvent arg0) {
					int level = 0;
					Object[] opt = {1,2,3,4,5,6,7,8,9,10};
					claveSeleccionada = arg0.getActionCommand();
					System.out.println(claveSeleccionada);
					switch (claveSeleccionada) {
					case "Juego Manual":
						level = (int) JOptionPane.showInputDialog(this,
							    "Elija un nivel de dificultad. Del 1 al 10.", // Cuando acaba el juego, se suman 3puntos/snakelink
							    "Dificultad de la partida",
							    JOptionPane.QUESTION_MESSAGE,
							    null,     //do not use a custom Icon
							    opt,  //the titles of buttons
							    opt[0]);
						timer.stop();
						thread.stop();
						try {
							new Game_0(15, 15, level > 0 ? level : 1, false, SnakeAutonomous0.BUSCA_FRUTAS_Y_ESQUIVA_OBSTACULOS);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						frame.dispose();
						break;
					case "Juego Automático":
						level = (int) JOptionPane.showInputDialog(this,
							    "Elija un nivel de dificultad. Del 1 al 10.", // Cuando acaba el juego, se suman 3puntos/snakelink
							    "Dificultad de la partida",
							    JOptionPane.QUESTION_MESSAGE,
							    null,     //do not use a custom Icon
							    opt,  //the titles of buttons
							    opt[0]);
						timer.stop();
						thread.stop();
						Object[] options = {"Sólo busca fruta",
			            "Busca fruta y esquiva obstáculos"};
						int n = JOptionPane.showOptionDialog(this,
							    "Elegido el nivel: " + (level > 0 ? level : 1) + ". Hay dos formas distintas de inteligencia para la Serpiente, elija una.", // Cuando acaba el juego, se suman 3puntos/snakelink
							    "Partida inteligente",
							    JOptionPane.YES_NO_OPTION,
							    JOptionPane.INFORMATION_MESSAGE,
							    null,     //do not use a custom Icon
							    options,  //the titles of buttons
							    options[0]);
						if (n == JOptionPane.YES_OPTION) {
							timer.stop();
							thread.stop();
							frame.dispose();
							try {
								new Game_0(15,15,level > 0 ? level : 1,true,SnakeAutonomous0.BUSCA_FRUTAS);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else{
							timer.stop();
							thread.stop();
							frame.dispose();
							try {
								new Game_0(15,15,level > 0 ? level : 1,true,SnakeAutonomous0.BUSCA_FRUTAS_Y_ESQUIVA_OBSTACULOS);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
						break;
					case "Salir":
						frame.dispose();
						System.exit(1);
						break;
					default:
						break;
					}
				}                	     	    
			}
    
		    
    public static void main(String [] args) throws UnsupportedAudioFileException, IOException, LineUnavailableException{
    	InitWindow gui = new InitWindow();
    }


	@Override
	public void actionPerformed(ActionEvent arg0) {
		
		if (ticks%4 == 0){
			timer.setDelay(tick);
			ticks = 0;	
		}

		// Añadir link cada 5 ticks de reloj.
		switch (ticks) {
		case 0:
			fotogramaCara = "snake.png";
			fotogramaTitle = "title.png";
			title.repaint();
			frame.repaint();
			break;
		case 1:
			fotogramaCara = "snake2.png";
			fotogramaTitle = "title2.png";
			title.repaint();
			frame.repaint();
			break;
		case 2:
			fotogramaCara = "snake3.png";
			fotogramaTitle = "title3.png";
			title.repaint();
			frame.repaint();
			break;
		case 3:
			fotogramaCara = "snake2.png";
			fotogramaTitle = "title4.png";
			title.repaint();
			frame.repaint();
			break;
		default:
			break;
		}
		
		// Incrementar ticks.
		ticks++;
	}
}