package p2.examples;
import java.awt.*;
import javax.swing.*;

/**
   - Incluye un panel con:
       - Una imagen.
       - Dos barras de deslizamiento.
   - Este panel se modela como una clase externa.
   
   @author TIC-LSI 
   @version LFP 2005/06 - Práctica 5
 */

public class Ej5_ImagenConScroll extends JFrame{

    /**
	 * 
	 */
	private static final long serialVersionUID = -3812697885436205970L;

	// Imagen a dibujar:
    Image miImagen;
        
    // Panel donde incluir la imagen.
    PanelImagen2 panelImagen;
   
    // Paño con barras de deslizamiento en el que se incluye
    // el panel de la imagen:
    // imagen INCLUIDA EN panel INCLUIDO EN marco.
    JScrollPane marcoImagen;
       
 
    public Ej5_ImagenConScroll(){

       super("Imagen con Scroll");
       
       // Construimos y añadimos paneles.
       construirPaneles();
         
       // Fijamos tamaño de la ventana.       
       setSize (400,400);
       
       // Hacemos la ventana visible.
       setVisible(true);  
         
       // Forzamos a que la aplicación termine al cerrar la ventana.     
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);              
    }

    private void construirPaneles(){

       // Creamos la imagen a partir de un fichero.    
       String path = "resources/PUERTO.jpg";    
       ImageIcon imi = new ImageIcon(path); 
       miImagen = imi.getImage();

       // Creamos el panel que va a contener la imagen.
       panelImagen = new PanelImagen2(miImagen);    
     
       // Creamos el marco en el que incluiremos el
       // panel que incluye a la imagen.
       // Las dimensiones de este marco delimitan la parte de la imagen
       // que es visible en la ventana.
       JScrollPane marcoImagen = new JScrollPane(panelImagen);
       marcoImagen.setPreferredSize(new Dimension(450, 450));
       marcoImagen.setViewportBorder(
                BorderFactory.createLineBorder(Color.black));
       marcoImagen.getVerticalScrollBar().setUnitIncrement(10);
       marcoImagen.getHorizontalScrollBar().setUnitIncrement(10);
               
       // Lo añadimos a la ventana principal.
       getContentPane().add(marcoImagen);           
    }
    
      
    public static void main(String [] args){
       Ej5_ImagenConScroll gui = new Ej5_ImagenConScroll();
    }
}

/**
  * Modela el Panel de la Imagen.
  * 
  */     
class PanelImagen extends JPanel {

   private Image miImagen;
   
   public PanelImagen(Image miImagen){
	   
      this.miImagen = miImagen;
      
      // Se fijan las dimensiones del tablero de acuerdo
      // con las dimensiones del dibujo que contiene.
	  // ¡ Muy importante para que las barras de deslizamiento
	  //   aparezcan de forma consistente !
      int xd = miImagen.getWidth(this);
      int yd = miImagen.getHeight(this);  

      // Mire que pasa si descomenta estas líneas.
      // xd = xd / 2;
      // yd = yd / 2;	
	    
      // Mire que pasa si descomenta estas líneas.
      // xd = xd * 3;
      // yd = yd * 3;	
	           
      setPreferredSize(new Dimension(xd, yd));   
   }
   
   public void paintComponent(Graphics g) {
       super.paintComponent(g);           
       g.drawImage(miImagen, 0, 0, this);
       g.drawRect(0, 0, 100, 100); 
   }
}




