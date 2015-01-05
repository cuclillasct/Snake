package p3.ejemplos;

import p3.basic.IBufferEntero;

public class CeldaNoSync implements IBufferEntero {

	private volatile int dato = -1;

	public void set(int val) {
	   System.out.println( Thread.currentThread().getName() +
	                       "\tEscribe:\t" + val);
	   dato = val;
	}

	public int get() {
	   System.out.println( Thread.currentThread().getName() +
	                       "\tConsume:\t" + dato);
	   return dato;
	}
	
	public int capacidad(){
		return 1;
	}

}
