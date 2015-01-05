package p3.ejemplos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class ManejadorPeticionFichero {
	
	/**
	 * Interacciona con el socket cliente para enviarle los contenidos
	 * del fichero solicitado.
	 * Es un método bloqueante, retorna cuando ha terminado de enviar el fichero.
	 * 
	 * @param incomingConection socket de conexión con el cliente.
	 */
	public void handleConnection(Socket incomingConnection){
		
		System.out.println("ManejadorServicio.handleConnection.");
		
		try{
            OutputStream outStrm = incomingConnection.getOutputStream();
            InputStream  inStrm  = incomingConnection.getInputStream();
            
            // Stream para leer del socket.
            BufferedReader streamReader = new BufferedReader(new InputStreamReader(inStrm));
            
            // Stream para escribir en el socket.
            PrintWriter streamWriter = new PrintWriter(outStrm);
            
            // Obtenemos nombre del fichero a descargar.
            String fileName = streamReader.readLine();
            
            // Creamos stream para leer del fichero
            BufferedReader fileReader = new BufferedReader(new FileReader(fileName));
            
            // Leemos líneas del fichero y las escribirmos en el socket.
            String line = null;
            while((line = fileReader.readLine()) != null){
            	streamWriter.println(line);
            	
            	try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
            	streamWriter.flush();
            }
            fileReader.close();
            streamWriter.close();
            streamReader.close();	
		}
		catch(IOException ioe){
			System.out.println("handleConnection: " + ioe.getMessage());				
		}		
	}

}
