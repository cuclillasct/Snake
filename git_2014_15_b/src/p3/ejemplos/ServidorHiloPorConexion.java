package p3.ejemplos;


import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorHiloPorConexion {

	protected int listenPort = 3100;
	
	
	public void aceptarConexiones(){
		
		try{
			ServerSocket server = new ServerSocket(listenPort, 5);
			Socket incomingConnection = null;
			while (true) {
				incomingConnection = server.accept();
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
	
	public void handleConnection(Socket socketToHandle){
        new Worker(socketToHandle).start();
	}
	
	public static void main(String[] args) {
       ServidorHiloPorConexion servidor = new ServidorHiloPorConexion();
       servidor.aceptarConexiones();
       
	}
	
	class Worker implements Runnable {
		
		Thread worker;
		Socket handler;
		
		public Worker(Socket handler){
			this.handler = handler;
			worker = new Thread(this);
		}
		
		public void run(){
			new ManejadorPeticionFichero().handleConnection(handler);		
		}
		
		public void start(){
			worker.start();
		}
	}

}
