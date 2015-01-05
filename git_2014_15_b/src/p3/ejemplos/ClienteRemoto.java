package p3.ejemplos;

import java.io.*;
import java.net.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * 
 * @author Juan Angel
 *
 */
public class ClienteRemoto extends JFrame implements ActionListener {
	
	String svsIp = "127.0.0.1";     // Dirección servidores
	int    pSvcIter  = 3000; 		// puerto Servidor Iterativo
	int    pSvcConc  = 3100; 		// puerto Servidor Concurrente
	int    pSvcPool  = 3200; 		// puerto Servidor Con Pila de Hilos
	
	// Identificador del servicio a solicitar.
	// Definir uno por cada nuevo servicio.
	public static String PedirServicio = "PedirFichero";
	
	// Contador de peticiones de servicio:
	int requestsCount;
	
	// Si true se arranca un thread independiente para (1) pedir servicio,
	// (2) recoger resultado, y (3) procesarlo.
	boolean threadOn = false;
	
	// Identificación del fichero a pedir. Para simplificar el ejemplo, se 
	// consideran sólo 4 ficheros de texto que se piden de forma cíclica.
	String textos [] = {"Macbeth.txt", "LordOfTheRings.txt", "LaVidaEsSuenio.txt", "Pensees.txt"};
	//int indiceTexto;
	
	// Elementos de la interfaz gráfica .......................................
	//int indiceAreaTexto  = 0;

	JScrollPane [] textPanes;
	JTextArea[] textAreas;
	int contadorPeticiones = 1;

	JScrollPane logPane;
	JTextArea   logArea;
	
	JButton btRqIter;
	JButton btRqTpr;
	JButton btRqPooled;
	JButton btClear;
	JButton btThreadOn;
	JButton btThreadOff;
	

	/**
	 * 
	 * @param hostIp
	 * @param port
	 */
	public ClienteRemoto(){	
		
		super("Cliente Remoto");
		
		// Text Panes y text areas para mostrar los ficheros recibidos............
		textPanes = new JScrollPane[4];
		textAreas = new JTextArea[4];
		JPanel textPanel = new JPanel();
		textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.LINE_AXIS));
		
		textPanel.add(Box.createRigidArea(new Dimension(1, 0)));
		
		for (int i = 0; i < textPanes.length; i++){
			textAreas[i] = new JTextArea(60, 10);
			textPanes[i] = new JScrollPane(textAreas[i]);
			textPanel.add(textPanes[i]);
			textPanel.add(Box.createRigidArea(new Dimension(1, 0)));
		}
		
		// Text Area for log messsages...............................................
		logArea = new JTextArea(10, 10);
		logPane = new JScrollPane(logArea);
		
		// Controles ................................................................
		btRqIter = new JButton("Request (Iterativo) ");
		btRqIter.addActionListener(this);
		btRqTpr = new JButton("Request (Hilo/petición) ");
		btRqTpr.addActionListener(this);
		btRqPooled = new JButton("Request (Pila de hilos) ");
		btRqPooled.addActionListener(this);
		
		btClear = new JButton("Clear Texts ");
		btClear.addActionListener(this);
		
		btThreadOn = new JButton("Cliente Multihilo");
		btThreadOn.addActionListener(this);
		
		btThreadOff = new JButton("Cliente monohilo");
		btThreadOff.setEnabled(false);
		btThreadOff.addActionListener(this);
		
		
		// Construcción ventana .......................................................
		JPanel pB = new JPanel();
		pB.add(btRqIter); pB.add(btRqTpr); pB.add(btRqPooled); 
		pB.add(btClear);
		pB.add(btThreadOn);	pB.add(btThreadOff);
		
		getContentPane().add(pB, BorderLayout.NORTH);
		getContentPane().add(textPanel);
		getContentPane().add(logPane, BorderLayout.SOUTH);

		
		setSize (1500,700);
		setVisible(true);  
		validate();
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 		
	}
	
	/**
	 * Establece conexión con uno de los servidores.
	 * @param ip dirección del servidor
	 * @param port puerto del servidor
	 * @return socket de conexión.
	 */
	public Socket establecerConexion(String ip, int port){
		
		Socket cliente = null;
		
		try{
			cliente = new Socket(ip, port);
		}
		catch(UnknownHostException uhe){
			System.out.println("establecerConexion: " + uhe.getMessage());
		}
		catch(IOException ioe){
			System.out.println("establecerConexion: " + ioe.getMessage());			
		}
		return cliente;
	}
	
	/**
	 * Establece conexión con servidor.
	 * Realiza petición de servicio.
	 * @param port puerto del servidor.
	 * @param args servicio solicitado (args[0]) y parámetros del servicio.
	 * @param rqCount número de la petición de servicio.
	 */
	public void processRequest(int port, String [] args, int rqCount){
		
		if (args == null) return;
		
		Socket handler = establecerConexion(svsIp, port);
		
		System.out.println("Cliente. processRequest. Connection stablished on port " + port);
		
		if (args[0].equals(ClienteRemoto.PedirServicio)) {
			String fichero = args[1];
			pedirFichero(handler, fichero, rqCount);
		}
	}
		
	
	/**
	 * Va leyendo el fichero recibido a medida que lo va recibiendo del servidor
	 * y volcando su conteniod en un área de texto determinada.
	 * Una vez terminado el servico se desconecta del servidor (cierre de conexión).
	 * 
	 * @param handler Socket de conexión con el servidor.
	 * @param nombreFichero Nombre del fichero que pedimos al servidor
	 * @param rqCount identifica la petición de servicio.
	 */
	private void pedirFichero(Socket handler, String nombreFichero, int rqCount){
		
		BufferedReader socketReader;
		PrintWriter socketWriter;

		try {
			socketReader = new BufferedReader(new InputStreamReader(handler.getInputStream()));
			socketWriter = new PrintWriter(handler.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		try {
			// Envíamos petición de servico.
			socketWriter.println(nombreFichero);
			
			// Vacíamos el buffer, forzando a que los datos (petición de servico)
			// se envíen al servidor.
			socketWriter.flush();

			// Leemos respuesta procedente del servidor y la vamos mostrando.
			String linea = null;
			while((linea = socketReader.readLine()) != null){
				textAreas[rqCount%textAreas.length].append(linea + "\n");	
			}
			textAreas[rqCount%textAreas.length].append("----------------------------\n");	
			logArea.append("Servicio: "+ rqCount + " finished \n");	
		}
		catch(IOException ioe){
			System.out.println("leerFichero: " + ioe.getMessage());				
		}
		
		// Cierra conexión.
		try{
			socketWriter.close();
			socketReader.close();
		}
		catch(IOException ioe){
			System.out.println("Leer Fichero: cerrarConexion: " + ioe.getMessage());			
		}	
	}
	
	
	public void actionPerformed(ActionEvent ae){
		
		if (ae.getSource() == btRqIter || ae.getSource() == btRqTpr || ae.getSource() == btRqPooled) {
			
			int port = 0;
			
			if (ae.getSource() == btRqIter)         port = pSvcIter;
			else if (ae.getSource() == btRqTpr)     port = pSvcConc;
			else if (ae.getSource() == btRqPooled)  port = pSvcPool; 
			
			logArea.append(requestsCount + " SENT \n");
			
			
			String args [] = {"PedirFichero", "data/" + textos[requestsCount%textos.length] };
			if (!threadOn)
				processRequest(port, args, requestsCount);
			else
				new ProcesadorServicio(port, args, requestsCount).start();	
			
			requestsCount++;
		}
		else if (ae.getSource() == btThreadOn) {
			threadOn = true;
			btThreadOn.setEnabled(false);
			btThreadOff.setEnabled(true);			
		}	
		else if (ae.getSource() == btThreadOff) {
			threadOn = false;
			btThreadOn.setEnabled(true);
			btThreadOff.setEnabled(false);	
		}	
		else if (ae.getSource() == btClear){
			for (int i = 0; i < textAreas.length; i++){
				textAreas[i].setText(null);
			}
		}
	}
	
		
	public static void main(String [] args){
		ClienteRemoto cliente = new ClienteRemoto();
	}
	
	/**
	 * Hilo del cliente.
	 */
	class ProcesadorServicio implements Runnable {
		
		Thread worker;
		int port;
		String args [];
		int contador;
		
		public ProcesadorServicio(int port, String args[], int contador){
			this.port = port;
			this.args = args;
			this.contador = contador;
			worker = new Thread(this);
		}
		
		public void start(){
			worker.start();
		}

		@Override
		public void run() {
			processRequest(port, args, contador);
		}
		
	}
	
}
