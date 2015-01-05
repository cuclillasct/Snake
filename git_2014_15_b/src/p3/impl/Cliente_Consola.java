package p3.impl;

import java.util.Hashtable;

import p3.basic.IFuturo;
import p3.basic.IObservadorFuturo;
import p3.basic.IServidorReparaciones;
import utilidades.*;


public class Cliente_Consola implements IObservadorFuturo, Runnable {
	
	private Hashtable<String, IFuturo> tablaFuturos = new Hashtable<String, IFuturo>();
	private IServidorReparaciones servidor;
	private Thread miHilo;
	
	
	public Cliente_Consola(IServidorReparaciones pr){
		this.servidor = pr;
		miHilo = new Thread(this);
	}
	
	public void start(){
		miHilo.start();
	}

	@Override
	public void done(String code) {
		System.out.println("done. code = " + code);
		IFuturo r = tablaFuturos.get(code);
		if (r == null){
			System.out.println("Cliente1.done future code unknown");
		}
		else{
			System.out.println("Cliente1.done future code = " + r.getId());
			System.out.println(r.getResult());
			tablaFuturos.remove(r.getId());
		}
	}

	public static String solicitarCmd(){
		System.out.println("-----------------------------------------");
		System.out.println("Cliente reparaciones: ");
		System.out.println("-----------------------------------------");
		System.out.println();
		System.out.println("Introduzca comando: ");
		System.out.println();
		System.out.println("Formatos: ");
		System.out.println("Reparaciones taller:  RT;id_Taller.");
		System.out.println("Tres peticiones:      RA");
		System.out.println("Salir:                END.");
		System.out.println();
		System.out.print("> ");
	
		return Teclado.readString();
	}
	
	public void run()  {
		
		String cmd = solicitarCmd();
		while (!cmd.equalsIgnoreCase("end")){
						
			String [] campos = cmd.split(";");
			if (campos == null){
				System.out.println("Cliente ---> Error: Cmd nulo.");
			}
			else if (campos[0].equalsIgnoreCase("RT")){
				System.out.println("Cliente --->  Pidiendo reparaciones taller : " + campos[1]);
				IFuturo f2 = prepararFuturo();
				servidor.getReparacionPorTaller(campos[1], f2);
			}
			else if (campos[0].equalsIgnoreCase("RA")){
				System.out.println("Cliente --->  Pidiendo reparaciones de talleres T001, T002 y T003");
				IFuturo futs[] = new IFuturo[3];
				for (int i = 0; i < futs.length; i++){
					futs[i] = prepararFuturo();
					System.out.println("Pidiendo reparaciones de taller = T00" +(i+1));
					servidor.getReparacionPorTaller("T00"+(i+1), futs[i]);
				}				
			}
			else {
				System.out.println("Cliente --->  Comando no implementado ");				
			}
			cmd = solicitarCmd();
		}
		System.out.println("Exit application.");
			
	}
	
	private IFuturo prepararFuturo(){
		IFuturo f = new Futuro();
		tablaFuturos.put(f.getId(), f);
		f.attach(this);
		return f;
	}
}
