package p2.examples;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

/**
    - Crea una ventana visible.
    - A�ade una Barra de Men� que a su vez incluye
       - Dos men�s, uno de ellos con submen�s.
       - 2 Botones.
 
   @author TIC-LSI 
 */
 
public class Ej2B_ConManejadoresEventos extends JFrame implements ActionListener {

    /**
	 * 
	 */
	private static final long serialVersionUID = 5110341197899182147L;

	// Barra del men�. 
    JMenuBar barraDelMenu;
    
    // Men�s
    JMenu menu_1, menu_2, menu_3;
    
    // Entradas de men�.
    JMenuItem item_1, item_2, item_3, item_4, item_5;
    
    // Tres botones
    JButton b1, b2, b3;


    public Ej2B_ConManejadoresEventos(){

       super("Men�s");
       
       // Construimos y a�adimos men�s.
       construirMenus();
         
       // Fijamos tama�o de la ventana.       
       setSize (300,240);
       // Hacemos la ventana visible.
       setVisible(true);       
       
       // Forzamos a que la aplicaci�n termine al cerrar la ventana.     
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                       
    }

    private void construirMenus(){
    
       // Creamos Barra de Men�.   
       barraDelMenu = new JMenuBar();
       
       // Creamos Men�s.
       menu_1 = new JMenu("men� 1");
       menu_2 = new JMenu("men� 2");  
       menu_3 = new JMenu("men� 3");  
         
       // Creamos entradas de men�s.
       item_1 = new JMenuItem("item_1");   
       item_1.addActionListener(this);
       item_2 = new JMenuItem("item_2");  
       item_2.addActionListener(this);
       item_3 = new JMenuItem("item_3");  
       item_3.addActionListener(this);
       item_4 = new JMenuItem("item_4");  
       item_4.addActionListener(this);
       item_5 = new JMenuItem("item_5");     
       item_5.addActionListener(this);
       
       // Creamos botones.    
       Manejador m = new Manejador();
       b1 = new JButton("b1");
       b1.addActionListener(m);
       b2 = new JButton("b2");  
       b2.addActionListener(m);
       b3 = new JButton("b3");    
       b3.addActionListener(
    		   new ActionListener(){
    			   public void actionPerformed(ActionEvent ae){
    					System.out.println("b3 !!!!!!!!!!!!!!!!!: ");
    			   }
    		   });
  
       // A�adimos elementos a barra de men�.
       barraDelMenu.add(menu_1);
       barraDelMenu.add(menu_2);
       barraDelMenu.add(b1);
       barraDelMenu.add(b2);
             
       // A�adimos elementos a men� 1.
       menu_1.add(item_1); 
       menu_1.addSeparator();
       menu_1.add(menu_3);  
       
       // A�adimos elementos a men� 3.
       menu_3.add(item_2); 
       menu_3.add(item_3); 
       
       // A�adimos elementos a men� 2.              
       menu_2.add(item_4); 
       menu_2.add(item_5);  
       menu_2.add(b3);
       
       // Le ponemos un borde a la barra de men� y lo a�adimos a la ventana.
       barraDelMenu.setBorder(BorderFactory.createLineBorder(Color.blue));
       setJMenuBar(barraDelMenu);             
    }
    
	@Override
	// Manejo Entradas de men�.
	public void actionPerformed(ActionEvent evento) {
		if (evento.getSource() == item_1) {
			System.out.println("item_1 seleccionado.");
		}
		else if (evento.getSource() == item_2) {
			System.out.println("item_2 seleccionado.");
		}
		else if (evento.getSource() == item_3) {
			System.out.println("item_3 seleccionado.");
		}
		else if (evento.getSource() == item_4) {
			System.out.println("item_4 seleccionado.");
		}
		else if (evento.getSource() == item_5) {
			System.out.println("item_5 seleccionado.");
		}
		
	}
    
    public static void main(String [] args){
       Ej2B_ConManejadoresEventos gui = new Ej2B_ConManejadoresEventos();
    }
}

class Manejador implements ActionListener {

	@Override
	public void actionPerformed(ActionEvent evento) {
		System.out.println("Pulsado bot�n: " + evento.getActionCommand());
	}
}