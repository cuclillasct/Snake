package p2.examples;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.*;

/**
   - Crea una ventana visible
   - A�ade una Barra de Men� con diversos elementos
   - A�ade dos paneles con etiquetas identificativas. 
  
   @author TIC-LSI 
 */
 
public class Ej3_Paneles extends JFrame{

    /**
	 * 
	 */
	private static final long serialVersionUID = 4915693836532900203L;

	// MENUS.
    // Barra del men�. 
    JMenuBar barraDelMenu;
    
    // Men�s
    JMenu menu_1, menu_2;
    
    // Entradas de men�.
    JMenuItem item_1, item_2, item_3, item_4, item_5;
    
    // Tres botones
    JButton b1, b2, b3;
    
    // PANELES.
    JPanel panel_1, panel_2;
    
    // ETIQUETAS.
    JLabel label_1, label_2;
    
    

    public Ej3_Paneles(){

       super("Paneles");
       
       // Construimos y a�adimos men�s.
       construirMenus();
       
       // Construimos y a�adimos paneles.
       construirPaneles();
        
       // Fijamos tama�o de la ventana.       
       setSize (400,240);
       
       // Hacemos la ventana visible.
       setVisible(true);     
       
       // Forzamos a que la aplicaci�n termine al cerrar la ventana.     
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                  
    }


    private void construirMenus(){
        
       // Creamos Barra de Men�.   
       barraDelMenu = new JMenuBar();
       
       // Creamos Men�s.
       menu_1 = new JMenu("Fichero");
       menu_2 = new JMenu("Generador");  
         
       // Creamos entradas de men�s.
       item_1 = new JMenuItem("Salvar ...");       
       item_2 = new JMenuItem("Salvar como ...");  
       item_3 = new JMenuItem("Cargar");        
       item_4 = new JMenuItem("Gen. Aleatorio");         
       item_5 = new JMenuItem("Gen. Sinusoidal");                       
       
       // Creamos botones.    
       b1 = new JButton("Ayuda");

       // A�adimos elementos a barra de men�.
       barraDelMenu.add(menu_1);
       barraDelMenu.add(menu_2);
       barraDelMenu.add(b1);
              
       // A�adimos elementos a men� 1.
       menu_1.add(item_1); 
       menu_1.add(item_2); 
       menu_1.addSeparator();  
       menu_1.add(item_3); 
                       
       // A�adimos elementos a men� 2.              
       menu_2.add(item_4); 
       menu_2.add(item_5);  
       
       // Le ponemos un borde a la barra de men� y lo a�adimos a la ventana.
       barraDelMenu.setBorder(BorderFactory.createLineBorder(Color.blue));
       setJMenuBar(barraDelMenu);             
    }

    private void construirPaneles(){
    
       // Creamos los tres paneles.
       panel_1 = new JPanel();
       panel_2 = new JPanel();
       
       // Creamos las etiquetas y las a�adimos a los paneles.
       label_1 = new JLabel("panel_1");
       panel_1.add(label_1);
       label_2 = new JLabel("panel_2");
       panel_2.add(label_2);       
             
       // Creamos bordes y se los a�adimos a los paneles.
       panel_1.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
       panel_2.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
              
       // A�adimos paneles a la ventana (a su contentPane).
       getContentPane().add(panel_1, BorderLayout.NORTH);
       getContentPane().add(panel_2);
    }
    
    
    public static void main(String [] args){
       Ej3_Paneles gui = new Ej3_Paneles();
    }
}