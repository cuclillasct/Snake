package p3.ejemplos;

import p3.basic.IBufferEntero;


public class BufferNoSync implements IBufferEntero{

	private int buffer[];
	private int in  = 0;
	private int out = 0;

	public BufferNoSync () {
		this(5);
	}
	
	public BufferNoSync (int capacidad) {
		buffer = new int[capacidad];
	}

	public void set(int val) {
		buffer[in] = val;
		System.out.print(Thread.currentThread().getName() + "\tproduce:" + val + "\t");
		mostrarBuffer();
		in = (in + 1) % buffer.length;
	}

	public int get() {
		int valor = buffer[out];
		buffer[out] = 0;
		System.out.print(Thread.currentThread().getName() + "\tconsume:" + valor + "\t");
		mostrarBuffer();	      
		out = (out + 1) % buffer.length;
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
