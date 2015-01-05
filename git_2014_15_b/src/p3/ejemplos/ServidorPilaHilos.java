package p3.ejemplos;

import java.io.*;
import java.net.*;
import java.util.*;


public class ServidorPilaHilos {

	// Puerto para escuchar las conexiones entrantes.
	protected int listenPort = 3200;
	
	// Número máximo de conexiones de clientes activas.
	protected int maximoConexiones = 2;
	
	// Socket servidor que acepta las conexiones de los clientes.
	protected ServerSocket server;
	
	// Cola de clientes conectados (representados por su socket de
	// conexión al servidor) a la espera de un hilo que les dé
	// servicio.
	protected static List<Socket> ListaClientesEsperando = new LinkedList<Socket>();

	
	

	public ServidorPilaHilos(){
		arrancarHilos();
	}
	

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
        enqueueRequest(socketToHandle);
	}
	
	
	public static void main(String[] args) {
	       ServidorPilaHilos servidor = new ServidorPilaHilos();
	       servidor.aceptarConexiones();
		}
	
	
	/**
	 * Add incoming request to the pool and tell other objects waiting on the pool
	 * that it now has some contents.
	 * @param handle 
	 */
	public static void enqueueRequest(Socket handle){
		synchronized (ListaClientesEsperando){
			ListaClientesEsperando.add(ListaClientesEsperando.size(), handle);
			ListaClientesEsperando.notifyAll();
		}
	}
	

	public void arrancarHilos() {
		for (int i = 0; i < maximoConexiones; i++){
			new Worker().start();
		}
	}
	

	
	
	class Worker implements Runnable {
		
		Thread worker; 
		Socket connection;
		
		public Worker(){
			worker = new Thread(this);
		}
		
		public void handleConnection(Socket handle){
			new ManejadorPeticionFichero().handleConnection(handle);
		}
		
		public void run(){
			while (true){
				// Se sincroniza con la cola de peticiones de servicio.
				// Si la cola está vacía espera.
				synchronized (ListaClientesEsperando){
					while (ListaClientesEsperando.isEmpty()){
						try{
							ListaClientesEsperando.wait();
						}
						catch(InterruptedException ie){
							System.out.println("PooledConnectionHandler.run." + ie.getMessage());
							return;
						}
					}
					// Consume una conexión pendiente de la cola de conexiones (peticiones).
					connection = (Socket) ListaClientesEsperando.remove(0);
				}
				// Proporcionar el servicio al cliente.
				handleConnection(connection);
			}			
		}
		
		public void start(){
			worker.start();
		}
	}



	
}
