package p2.examples;
import java.awt.Color;
import javax.swing.*;

/**
    - Crea una ventana visible.
    - Añade una Barra de Menú que a su vez incluye
       - Dos menús, uno de ellos con submenús.
       - 2 Botones.
 
   @author TIC-LSI 
 */
 
public class Ej2_MenusYBotones extends JFrame{

    /**
	 * 
	 */
	private static final long serialVersionUID = 5110341197899182147L;

	// Barra del menú. 
    JMenuBar barraDelMenu;
    
    // Menús
    JMenu menu_1, menu_2, menu_3;
    
    // Entradas de menú.
    JMenuItem item_1, item_2, item_3, item_4, item_5;
    
    // Tres botones
    JButton b1, b2, b3;


    public Ej2_MenusYBotones(){

       super("Menús");
       
       // Construimos y añadimos menús.
       construirMenus();
         
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
       menu_1 = new JMenu("menú 1");
       menu_2 = new JMenu("menú 2");  
       menu_3 = new JMenu("menú 3");  
         
       // Creamos entradas de menús.
       item_1 = new JMenuItem("item_1");       
       item_2 = new JMenuItem("item_2");  
       item_3 = new JMenuItem("item_3");        
       item_4 = new JMenuItem("item_4");         
       item_5 = new JMenuItem("item_5");                
       
       // Creamos botones.    
       b1 = new JButton("b1");
       b2 = new JButton("b2");  
       b3 = new JButton("b3");         
  
       // Añadimos elementos a barra de menú.
       barraDelMenu.add(menu_1);
       barraDelMenu.add(menu_2);
       barraDelMenu.add(b1);
       barraDelMenu.add(b2);
             
       // Añadimos elementos a menú 1.
       menu_1.add(item_1); 
       menu_1.addSeparator();
       menu_1.add(menu_3);  
       
       // Añadimos elementos a menú 3.
       menu_3.add(item_2); 
       menu_3.add(item_3); 
       
       // Añadimos elementos a menú 2.              
       menu_2.add(item_4); 
       menu_2.add(item_5);  
       menu_2.add(b3);
       
       // Le ponemos un borde a la barra de menú y lo añadimos a la ventana.
       barraDelMenu.setBorder(BorderFactory.createLineBorder(Color.blue));
       setJMenuBar(barraDelMenu);             
    }
    
    public static void main(String [] args){
       Ej2_MenusYBotones gui = new Ej2_MenusYBotones();
    }
}