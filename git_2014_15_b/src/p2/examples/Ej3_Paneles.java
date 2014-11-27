package p2.examples;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
   - Crea una ventana visible
   - Añade una Barra de Menú con diversos elementos
   - Añade dos paneles con etiquetas identificativas. 
  
   @author TIC-LSI 
 */
 
public class Ej3_Paneles extends JFrame{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4915693836532900203L;

	// MENUS.
    // Barra del menú. 
    JMenuBar barraDelMenu;
    
    // Menús
    JMenu menu_1, menu_2;
    
    // Entradas de menú.
    JMenuItem item_1, item_2, item_3, item_4, item_5;
    
    // Tres botones
    JButton b1, b2, b3;
    
    // PANELES.
    JPanel panel_1, panel_2;
    
    // ETIQUETAS.
    JLabel label_1, label_2;
    
    

    public Ej3_Paneles(){

       super("Paneles");
       
       // Construimos y añadimos menús.
       construirMenus();
       
       // Construimos y añadimos paneles.
       construirPaneles();
        
       // Fijamos tamaño de la ventana.       
       setSize (400,240);
       
       // Hacemos la ventana visible.
       setVisible(true);     
       
       // Forzamos a que la aplicación termine al cerrar la ventana.     
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                  
    }


    private void construirMenus(){
        
       // Creamos Barra de Menú.   
       barraDelMenu = new JMenuBar();
       
       // Creamos Menús.
       menu_1 = new JMenu("Fichero");
       menu_2 = new JMenu("Generador");  
         
       // Creamos entradas de menús.
       item_1 = new JMenuItem("Salvar ...");       
       item_2 = new JMenuItem("Salvar como ...");  
       item_3 = new JMenuItem("Cargar");        
       item_4 = new JMenuItem("Gen. Aleatorio");         
       item_5 = new JMenuItem("Gen. Sinusoidal");                       
       
       // Creamos botones.    
       b1 = new JButton("Ayuda");

       // Añadimos elementos a barra de menú.
       barraDelMenu.add(menu_1);
       barraDelMenu.add(menu_2);
       barraDelMenu.add(b1);
              
       // Añadimos elementos a menú 1.
       menu_1.add(item_1); 
       menu_1.add(item_2); 
       menu_1.addSeparator();  
       menu_1.add(item_3); 
                       
       // Añadimos elementos a menú 2.              
       menu_2.add(item_4); 
       menu_2.add(item_5);  
       
       // Le ponemos un borde a la barra de menú y lo añadimos a la ventana.
       barraDelMenu.setBorder(BorderFactory.createLineBorder(Color.blue));
       setJMenuBar(barraDelMenu);             
    }

    private void construirPaneles(){
    
       // Creamos los tres paneles.
       panel_1 = new JPanel();
       panel_2 = new JPanel();
       
       // Creamos las etiquetas y las añadimos a los paneles.
       label_1 = new JLabel("panel_1");
       panel_1.add(label_1);
       label_2 = new JLabel("panel_2");
       panel_2.add(label_2);       
             
       // Creamos bordes y se los añadimos a los paneles.
       panel_1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
       panel_2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
              
       // Añadimos paneles a la ventana (a su contentPane).
       getContentPane().add(panel_1, BorderLayout.NORTH);
       getContentPane().add(panel_2);
    }
    
    
    public static void main(String [] args){
       Ej3_Paneles gui = new Ej3_Paneles();
    }
}