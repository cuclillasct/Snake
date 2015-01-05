package p3.ejemplos;

import p3.basic.IBufferEntero;

public class BufferPC implements IBufferEntero {

	private int buffer[];

	private boolean vacio  = true;   
	private boolean lleno = false;
	private int in  = 0;
	private int out = 0;

	public BufferPC () {
		this(5);
	}

	public BufferPC(int capacidad) {
		buffer = new int[capacidad];
	}

	public synchronized void set(int val) {
		// Comprobamos si hay sitio para almacenar una nueva produccion
		while (lleno) {  
			try {
				System.err.println( "\n" + Thread.currentThread().getName() + "ESPERANDO ...");
				wait();  
			}
			catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		buffer[in] = val;
		System.out.print(Thread.currentThread().getName() + "\tproduce:" + val + "\t");
		mostrarBuffer();
		vacio = false;	      
		in = (in + 1) % buffer.length;

		if (in == out) {
			lleno = true;
			System.out.println("\nBUFFER LLENO!");
		}     	  
		notifyAll();  // Notifico a algún hilo la disponibilidad
	}

	public synchronized int get() {
		int valor;
		// Comprobamos si se puede sacar algun elemento
		while (vacio) {   
			try {
				System.err.println( "\n" + Thread.currentThread().getName() + "ESPERANDO ..."); 
				wait();
			}
			catch ( InterruptedException e ){
				e.printStackTrace();
			}
		}

		lleno = false;

		valor = buffer[out];
		buffer[out] = 0;

		System.out.print(Thread.currentThread().getName() + "\tconsume:" + valor + "\t");
		mostrarBuffer();	      
		out = (out + 1) % buffer.length;

		if (out == in) {
			vacio = true;
			System.out.println("\nBUFFER VACIO!");
		}		
		notifyAll();
		return valor;
	}

	public synchronized void mostrarBuffer(){	
		System.out.print("\t");
		for(int i=0; i<buffer.length; i++)
			System.out.print(buffer[i] + " ");		
		System.out.println();
	}

	public int capacidad(){
		return buffer.length;
	}
}

