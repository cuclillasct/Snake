package p3.impl;

import p3.basic.IScheduler;
import p3.basic.IServidorReparaciones;


public class Test1 {

	public static void main(String[] args) {
		IScheduler sch = new Server();
		sch.dispatch();
		IServidorReparaciones pr = new Proxy(sch);
		Cliente_Consola cliente = new Cliente_Consola(pr);
		cliente.start();
	}
}
