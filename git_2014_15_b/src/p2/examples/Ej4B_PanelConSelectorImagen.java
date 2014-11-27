package p2.examples;

import java.util.Hashtable;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;

import p2.basic.IView;
import p2.views_impl.VCircle;
import p2.views_impl.VImage;
import p2.views_impl.VSquare;


public class Ej4B_PanelConSelectorImagen extends JFrame {

    /**
	 * 
	 */
	private static final long serialVersionUID = -8924852465850235207L;
	
	
	// PANELES.
    PanelGrafica     panelGrafica;
    PanelGenerador   panelBotones;
    
    // Identificadores de vista
    public static final String [] 
    		clavesVistas = {"Raton", "Conejo", "Pastel", "Cara", "Rectangulo", "Circulo"};
    String claveSeleccionada = null;
    


    // Diccionario de vistas.
    Hashtable <String, IView> tablaVistas = new Hashtable<String, IView>();
    
    
    public Ej4B_PanelConSelectorImagen() throws IOException{

       super("Panel e Imagen");
       
       crearDiccionarioVistas();
       
       // Construimos y añadimos paneles.
       construirPaneles();
         
       // Forzamos a que la aplicación termine al cerrar la ventana.     
       setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            
       // Fijamos tamaño de la ventana.       
       setSize (600,600);
       // Hacemos la ventana visible.
       setVisible(true);                       
    }
    
    private void crearDiccionarioVistas() throws IOException{
	    tablaVistas.put(clavesVistas[0], new VImage(clavesVistas[0], "resources/raton.jpg"));
	    tablaVistas.put(clavesVistas[1], new VImage(clavesVistas[1], "resources/conejo.jpg"));
	    tablaVistas.put(clavesVistas[2], new VImage(clavesVistas[2], "resources/pastel1.jpg"));
	    tablaVistas.put(clavesVistas[3], new VImage(clavesVistas[3], "resources/img1.jpg"));
	    tablaVistas.put(clavesVistas[4], new VSquare(clavesVistas[4]));
	    tablaVistas.put(clavesVistas[5], new VCircle(clavesVistas[5]));
    }


    private void construirPaneles(){
    
       // Creamos los dos paneles.
       panelGrafica =   new PanelGrafica();
       panelBotones = new PanelGenerador();
             
       // Creamos bordes y se los añadimos a los paneles.
       panelGrafica.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
       panelBotones.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
              
       // Añadimos paneles a la ventana (a su contentPane).
       getContentPane().add(panelGrafica);
       getContentPane().add(panelBotones, BorderLayout.EAST);
    }
    
    
    public static void main(String [] args) throws IOException{
    	Ej4B_PanelConSelectorImagen gui = new Ej4B_PanelConSelectorImagen();
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
       	     lb = new JLabel("Aquí puede ir una imagen");
             add(lb);  
         } 
         
         public void paintComponent(Graphics g){
        	 super.paintComponent(g);
        	 IView vista = null;
        	 
        	 if (claveSeleccionada != null) {
        	    vista = tablaVistas.get(claveSeleccionada);
        	    vista.setSize(100);
        	 }
        	 if (vista != null){
        		 vista.draw(g, 0, 0);
        	 }
        	 else{ 
                 g.drawImage(miImagen, 0, 0, this);         
        	 }
         }                	     	    
	}

   /**
    * Clase interna: 
    * Utiliza GridLayout como gestor de diseño (LayoutManager).
    * 
    */     
    class PanelGenerador extends JPanel implements ActionListener{
	 
        JButton btControl [] = new JButton[6];
	 
	    public PanelGenerador(){
            setLayout(new GridLayout(3, 2));
            for (int i = 0; i < btControl.length; i++){
       	        btControl[i] = new JButton(clavesVistas[i]);
       	        btControl[i].addActionListener(this);
	            add(btControl[i]); 
	        }
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			claveSeleccionada = arg0.getActionCommand();
			System.out.println(claveSeleccionada);
			panelGrafica.repaint();
		}	   	              	    	    
	 }
        
}
