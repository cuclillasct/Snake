package p3.ejemplos;

import p3.basic.IBufferEntero;

public class CeldaSync implements IBufferEntero {
	
	private volatile int dato = -1;
	boolean writable = true;

	public synchronized void set(int val) {
	   while (!writable){
		   try{
			   wait();
		   }
		   catch(InterruptedException ie){
			   
		   }
	   }
	   System.out.println( Thread.currentThread().getName() +
	                       "\tEscribe:\t" + val);
	   dato = val;
	   writable = false;
	   notifyAll();
	}

	public synchronized int get() {
	   System.out.println( Thread.currentThread().getName() +
	                       "\tConsume:\t" + dato);
	   
	   while (writable){
		   try{
			   wait();
		   }
		   catch(InterruptedException ie){ 
		   }
	   }	   
	   writable = true;
	   notifyAll();
	   return dato;
	}
	
	public int capacidad(){
		return 1;
	}
}
