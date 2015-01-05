package p3.ejemplos;

import p3.basic.IBufferEntero;


public class SumaElementos {


	public static void main(String[] args) {
		
		IBufferEntero celda;
		
		System.out.println("--------------------------------------------");
		System.out.println("Celda de un solo elemento sin sincronizar ");
        System.out.println();
		celda = new CeldaNoSync();
		testCelda(celda);
		
		System.out.println("--------------------------------------------");
		System.out.println("Celda de un solo elemento sincronizada ");
        System.out.println();
		celda = new CeldaSync();
		testCelda(celda);
		
		System.out.println("--------------------------------------------");
		System.out.println("Celda de cinco elementos sin sincronizar ");
        System.out.println();
		celda = new BufferNoSync();
		testCelda(celda);		
		
		System.out.println("--------------------------------------------");
		System.out.println("Celda de cinco elementos sincronizada Prod/Cons ");
        System.out.println();
		celda = new BufferPC();
		testCelda(celda);
		
	}
	
	
	public static void testCelda(IBufferEntero celda){
		ProduceEntero p = new ProduceEntero(celda);
		ConsumeEntero c = new ConsumeEntero(celda);
		System.out.println("Creando y arrancando hilos...cronómetro a cero...");
		long t_ini = System.currentTimeMillis();
		p.start();
		c.start();
		try {
			p.join();
			c.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}	
		System.out.println("Terminan hilos... tiempo consumido = " + (System.currentTimeMillis() - t_ini));
		int suma = 0;
		for(int i = 1; i <= 10; i++){
			suma+=i;
		}
		System.out.println("La suma debe ser: " + suma);
		System.out.println("La suma es      : " + c.getSuma());	
	}
}

class ProduceEntero implements Runnable {
	
	Thread miHilo = null;
	IBufferEntero celda;
	   
	public ProduceEntero(IBufferEntero celda) {
		this.celda = celda;
		miHilo = new Thread(this, "Productor");
	}

	public void run() {
	   for ( int k = 1; k <= 10; k++ ) {
	      try {
	         Thread.sleep( (int) ( Math.random() * 50 ) );
	      }
	      catch( InterruptedException e ) {
	         System.out.println( e.toString() );
	      }
	      celda.set(k);
	   }
	   System.out.println( miHilo.getName() + " Se acabó la producción de valores.");
	}
	
	public void start() {
		if (miHilo != null){
			miHilo.start();
		}
	}	
	
	public void join() throws InterruptedException {
		if (miHilo != null){
			miHilo.join();
		}
	}
}


class ConsumeEntero implements Runnable {
	
	private IBufferEntero celda;
	private Thread miHilo = null;
	private int suma;
	

	public ConsumeEntero(IBufferEntero celda) {
	   this.celda = celda;
	   miHilo = new Thread(this, "Consumidor");
	}

	public void run() {
	   int val;

	   do {
	      try {
	         Thread.sleep( (int) ( Math.random() * 50 ) );
	      }
	      catch( InterruptedException e ) {
	         System.out.println( e.toString() );
	      }

	      val = celda.get();
	      suma += val;
	   } while ( val != 10 );

	   System.out.println( miHilo.getName() + " suma de valores leídos: " + suma);
	}
	
	public void join() throws InterruptedException {
		if (miHilo != null){
			miHilo.join();
		}
	}
	
	public void start() {
		if (miHilo != null){
			miHilo.start();
		}
	}
	
	public int getSuma(){
		return suma;
	}
}

