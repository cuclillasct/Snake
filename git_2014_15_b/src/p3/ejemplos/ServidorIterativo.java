package p3.ejemplos;

import java.io.*;
import java.net.*;

public class ServidorIterativo {
	
	protected int listenPort = 3000;
	
	public void aceptarConexiones(){
		
		System.out.println("Servidor Iterativo: aceptar conexiones.");
		
		try{
			ServerSocket server = new ServerSocket(listenPort);
			Socket incomingConnection = null;
			while (true) {
				incomingConnection = server.accept();
				System.out.println("Servidor Iterativo: nueva conexión.");
				handleConnection(incomingConnection);
			}
		}
		catch(BindException be){
			System.out.println("aceptarConexiones: " + be.getMessage());		
		}
		catch(IOException ioe){
			System.out.println("aceptarConexiones: " + ioe.getMessage());				
		}
	}
	
	public void handleConnection(Socket handler){
		new ManejadorPeticionFichero().handleConnection(handler);		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
       ServidorIterativo servidor = new ServidorIterativo();
       servidor.aceptarConexiones();
       
	}

}
