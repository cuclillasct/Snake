package p2.examples;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
    - Crea una ventana visible.
    - A�ade dos paneles modelados como clases internas. 
    - En uno de ellos coloca una imagen.
    - En el otro 6 botones colocados seg�n un GridLayout.
    - Se utilizan diferentes gestores de dise�o. 
  
   @author TIC-LSI 
 */
 
public class Ej4_PanelConImagen extends JFrame {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = -8924852465850235207L;
	// PANELES.
    PanelGrafica     panelGrafica;
    PanelGenerador   panelGenerador;

    
    public Ej4_PanelConImagen(){

       super("Panel e Imagen");
       
       // Construimos y a�adimos men�s.
       // construirMenus();
       
       // Construimos y a�adimos paneles.
       construirPaneles();
         
       // Forzamos a que la aplicaci�n termine al cerrar la ventana.     
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
       // Fijamos tama�o de la ventana.       
       setSize (800,640);
       // Hacemos la ventana visible.
       setVisible(true);                       
    }


    private void construirPaneles(){
    
       // Creamos los dos paneles.
       panelGrafica =   new PanelGrafica();
       panelGenerador = new PanelGenerador();
             
       // Creamos bordes y se los a�adimos a los paneles.
       panelGrafica.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
       panelGenerador.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
              
       // A�adimos paneles a la ventana (a su contentPane).
       getContentPane().add(panelGrafica);
       getContentPane().add(panelGenerador, BorderLayout.EAST);
    }
    
    
    public static void main(String [] args){
       Ej4_PanelConImagen gui = new Ej4_PanelConImagen();
    }
    

   /**
    * Clase interna: 
    * Utiliza layout por defecto.
    * 
    */
    class PanelGrafica extends JPanel{

         JLabel lb;
         
         // Creamos la imagen a partir de un fichero.
         String path = "resources/PUERTO.jpg";  
         ImageIcon imi = new ImageIcon(path);
         Image miImagen = imi.getImage();
     
	     public PanelGrafica(){
       	     lb = new JLabel("Aqu� puede ir una imagen");
             add(lb);  
         } 
         
         public void paintComponent(Graphics g){
        	 super.paintComponent(g);
             g.drawImage(miImagen, 0, 0, this);         
         }                	     	    
	}

   /**
    * Clase interna: 
    * Utiliza GridLayout como gestor de dise�o (LayoutManager).
    * 
    */     
    class PanelGenerador extends JPanel{
	 
        JButton btControl [] = new JButton[6];
	 
	    public PanelGenerador(){
            setLayout(new GridLayout(3, 2));
            for (int i = 0; i < btControl.length; i++){
       	        btControl[i] = new JButton("bot�n_" + i);
	            add(btControl[i]); 
	        }
		}	   	              	    	    
	 }
        
}