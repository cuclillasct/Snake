package p3.impl;

import p3.basic.IFuturo;
import p3.basic.IObservadorFuturo;

public class Futuro implements IFuturo {
	
	String result = new String();
	boolean hecho;
	String descripcion;
	IObservadorFuturo obs;
	static int contador = 1;
	String id;
	
	
	public Futuro (){
		id = new Integer(contador++).toString();
	}
	
	
	public Futuro (IObservadorFuturo obs){
		attach(obs);
	}
	
	/*
	public Futuro (int key, IObservadorFuturo obs){
		attach(obs);
	}
	*/

	@Override
	public String getResult() {
		return result;
	}
	
	public void setResult(String result){
		this.result = new String(result);
		hecho = true;
		if (obs != null) obs.done(id);
	}


	@Override
	public boolean isDone() {
		return hecho;
	}

	@Override
	public void attach(IObservadorFuturo obs) {
		this.obs = obs;		
	}

	
	public String toString(){
		return "FT, Cmd = " + descripcion + ", result = " + result.toString();
	}

	@Override
	public String getId() {
		return id;
	}

}
