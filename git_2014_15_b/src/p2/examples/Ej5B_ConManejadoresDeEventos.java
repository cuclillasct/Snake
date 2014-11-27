package p2.examples;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
   Como GUI_6 de la práctica 5, pero además:
      - Se añade una etiqueta en la parte inferior de la 
        ventana de la aplicación.
      - El Panel de la imagen maneja los eventos de ratón que se producen
        sobre el mismo, escribiendo en la etiqueta el nombre del método 
        que se ejecuta en respuesta al evento de ratón y las 
        coordenadas en las que se ha producido el evento.
   
   @author TIC-LSI 
   @version LFP 2008/09 - Práctica 6
 */

public class Ej5B_ConManejadoresDeEventos extends JFrame{

    // PANELES.
    // Tablero de Juego (panelImagen) y su dibujo (miImagen)
    PanelImagen2 panelImagen;
    Image miImagen;
    
    // Paño con barras de deslizamiento en el que se incluye
    // el tablero de juego.
    JScrollPane marcoImagen;
       
    // Panel en el que se incluye el paño con barras de deslizamiento.
    JPanel panelExterno;
    
    // Etiqueta informativa de la posición del ratón.
    JLabel infoLabel;
    
 
    public Ej5B_ConManejadoresDeEventos(){

       super("Eventos de Ratón");
       
       infoLabel = new JLabel("Waiting for mouse event");
       infoLabel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)); 
       infoLabel.setPreferredSize(new Dimension(150,50));
       
       // Construimos y añadimos paneles.
       construirPaneles();
         
       // Fijamos tamaño de la ventana.       
       setSize (600,600);
       setResizable(false);

       
       // Hacemos la ventana visible.
       setVisible(true);  
         
       // Forzamos a que la aplicación termine al cerrar la ventana.     
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);              
    }

    private void construirPaneles(){

       // Creamos el dibujo del tablero a partir de un fichero.
       String path = "resources/PUERTO.jpg"; 
       ImageIcon imi = new ImageIcon(path);   
       miImagen = imi.getImage();

       // Creamos el tablero de juego.
       panelImagen = new PanelImagen2(miImagen); 
       panelImagen.setInfoLabel(infoLabel);   
     
       // Creamos el paño de deslizamiento en el que incluimos el
       // tablero de juego.
       // Las dimensiones de este paño delimitan la parte del tablero
       // de juego que es visible en la ventana.
       JScrollPane marcoImagen = new JScrollPane(panelImagen);
       marcoImagen.setPreferredSize(new Dimension(450, 450));
       marcoImagen.setViewportBorder(
                BorderFactory.createLineBorder(Color.black));
       marcoImagen.getVerticalScrollBar().setUnitIncrement(10);
       marcoImagen.getHorizontalScrollBar().setUnitIncrement(10);
   
       // Creamos el panel del tablero.
       // Le ponemos un borde y le asignamos una dimensión.
       panelExterno = new JPanel();
       panelExterno.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));  
       panelExterno.setPreferredSize(new Dimension(550, 550));
       panelExterno.add(marcoImagen);   
                        
       // Añadimos panel del tablero y rellenos a la ventana principal.
       getContentPane().add(panelExterno);   
       getContentPane().add(infoLabel, BorderLayout.SOUTH);           
    }
    
      
    public static void main(String [] args){
       Ej5B_ConManejadoresDeEventos gui = new Ej5B_ConManejadoresDeEventos();
    }
}

/**
   Panel con una imagen que maneja los eventos de ratón que se producen sobre 
   el mismo.
   
  */     
class PanelImagen2 extends JPanel implements MouseListener, MouseMotionListener {

   private Image miImagen;
   private JLabel infoLabel;
   
   public PanelImagen2(Image miImagen){
	   
      this.miImagen = miImagen;
      
      // Se fijan las dimensiones del tablero de acuerdo
      // con las dimensiones del dibujo que contiene.
      int xd = miImagen.getWidth(this);
      int yd = miImagen.getHeight(this);         
      setPreferredSize(new Dimension(xd, yd));   
      
      addMouseListener(this);
      addMouseMotionListener(this);

   }
   
   public void paintComponent(Graphics g) {
   	   super.paintComponent(g);           
         g.drawImage(miImagen, 0, 0, this);
   }
   
   public void setInfoLabel(JLabel infoLabel){
	   this.infoLabel = infoLabel;
   }   
   
   public void mouseClicked(MouseEvent e){
   	  if (infoLabel != null)
	        infoLabel.setText("mouseClicked en x = " + e.getX() + " y = " + e.getY());
	     else
	        System.out.println("mouseClicked en x = " + e.getX() + " y = " + e.getY());
   }
   
   public void mousePressed(MouseEvent e){
   	  if (infoLabel != null)
	        infoLabel.setText("mousePressed en x = " + e.getX() + " y = " + e.getY());
	     else
   	     System.out.println("mousePressed en x = " + e.getX() + " y = " + e.getY());	   
   }   

   public void mouseReleased(MouseEvent e){
   	  if (infoLabel != null)
	        infoLabel.setText("mouseReleased en x = " + e.getX() + " y = " + e.getY());
	     else
	        System.out.println("mouseReleased en x = " + e.getX() + " y = " + e.getY());
   } 
   
   public void mouseEntered(MouseEvent e){
   	  if (infoLabel != null)
	        infoLabel.setText("mouseEntered en x = " + e.getX() + " y = " + e.getY());
	     else
           System.out.println("MouseEntered en x = " + e.getX() + " y = " + e.getY());
   } 
   
   public void mouseExited(MouseEvent e){
   	  if (infoLabel != null)
	        infoLabel.setText("mouseExited");
	     else	   
           System.out.println("mouseExited");   
   }   
   
   public void mouseDragged(MouseEvent e){
   	  if (infoLabel != null)
	        infoLabel.setText("mouseDragged: [" + e.getX() + ", " + e.getY() + "]");
	     else
           System.out.println("mouseDragged en x = " + e.getX() + " y = " + e.getY());
   } 
   
   public void mouseMoved(MouseEvent e){
   	  if (infoLabel != null)
	        infoLabel.setText("mouseMoved: [" + e.getX() + ", " + e.getY() + "]");
	     else
           System.out.println("mouseMoved: [" + e.getX() + ", " + e.getY() + "]");   
   }     
}




