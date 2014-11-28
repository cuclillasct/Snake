package p2.examples;
import javax.swing.*;

/**
   Crea una ventana visible con un nombre.
  
   @author TIC-LSI 
 */
public class Ej1_UnaVentana extends JFrame{

    /**
	 * 
	 */
	private static final long serialVersionUID = -276938680268304313L;

	public Ej1_UnaVentana(){
    
       super("Una Ventana");
    
       // Fijamos tamaño de la ventana.       
       setSize (375,398);
       
       // Hacemos la ventana visible.
       setVisible(true);  
       
       // Forzamos a que la aplicación termine al cerrar la ventana.     
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);                            
    }
    
    public static void main(String [] args){
       Ej1_UnaVentana gui = new Ej1_UnaVentana();
    }
}