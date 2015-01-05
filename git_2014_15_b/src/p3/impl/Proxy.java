package p3.impl;

import java.util.Date;

import com.mysql.jdbc.Connection;

import p3.basic.IFuturo;
import p3.basic.IMethodRequest;
import p3.basic.IScheduler;
import p3.basic.IServidorReparaciones;


public class Proxy implements IServidorReparaciones {
	
	private IScheduler scheduler;
	
	public Proxy(IScheduler scheduler){
		if (scheduler != null){
		    this.scheduler = scheduler;
		}
		else {
			System.out.println("Server not started");
			throw new RuntimeException("ProxyLocal: Server not started");
		}
		if (!this.scheduler.isActive())
		    this.scheduler.dispatch();
	}


	public void stop() {
		scheduler.stop();		
	}
	
	@Override
	public void getReparacionPorId(String id, IFuturo f) {
		//
	}
	
	@Override
	public void getReparacionPorTaller(String idTaller, IFuturo f) {
		IMethodRequest rq = new GetReparacionPorTaller(idTaller, f);	
		scheduler.putRequest(rq);		
	}

	@Override
	public void getReparacionPorFechas(Date fi, Date fs, IFuturo f) {
		//
	}

	@Override
	public void getReparacionPorCoche(String matricula, IFuturo f) {
		//			
	}	


	@Override
	public void nuevaReparcacion(String id, String mat, String taller,
			Date inicio, IFuturo f) {
		//		
	}

	@Override
	public void cerrarReparacion(String id, Date fin, int precio, IFuturo f) {
		//
	}
}
