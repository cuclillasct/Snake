package p3.ejemplos;

public class ImprimeHola {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	      PrintThread thread[] = new PrintThread [4];

	      for (int i = 0; i < thread.length; i++) {
	    	  thread[i] = new PrintThread( "thread_" + i );
	      }
	     
	      System.out.println( "Comienzan los threads" );
	      
	      for (int i = 0; i < thread.length; i++) {
	    	  thread[i].start();
	      }
	      System.out.println( "Threads comenzados" );
	      System.out.println( "Fin de main.");
	}

}

class PrintThread extends Thread {

	   private int sleepTime;

	   // El constructor asigna un nombre al hilo 
	   // llamando al constructor de Thread
	   public PrintThread( String name ){
	   
	      super(name);

	      // duerme entre 0 y 5 segundos
	      sleepTime = (int) ( Math.random() * 5000 );

	      System.out.println( getName() + " \tId: " + getId() +
	                          "\t;  Hola!!!! dormiré: " + sleepTime + " milisegundos");
	   }

	   // ejecuta el hilo
	   public void run()
	   {
	      // pone el hilo a dormir un tiempo = sleepTime.
	      try {
	         System.err.println( getName() + " \tId: " + getId() + "\tme voy a dormir " +
			 	                 sleepTime + " milisegundos");
	         Thread.sleep( sleepTime );
	      }
	      catch ( InterruptedException exception ) {
	         System.err.println( exception.toString() );
	      }

	      // imprimo el nombre
	      System.out.println( getName() + " \tId: " + getId() +  
	    		              "\t Adiós !!!!!!!!!  me despierto y termino" );
	   }
}
