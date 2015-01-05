package p3.impl;

import p3.basic.IFuturo;
import p3.basic.IMethodRequest;

public class GetReparacionPorTaller implements IMethodRequest {

	protected IFuturo futuro;
	protected String consulta;
	protected String id;
		
	public GetReparacionPorTaller(){}
	
	public GetReparacionPorTaller(String idTaller, IFuturo resultado){
		this.id = idTaller;
		this.futuro = resultado;
		this.consulta = "SELECT * FROM reparacion WHERE cif_taller = '" + id + "';";
	}

	@Override
	public void execute() {
		String res = null;
		if (consulta != null){
			res = Server.getData(consulta);
		}
		else{
			res = "Consulta is NULL";
		}
		if (futuro != null){
			futuro.setResult(res);
		}
	}

}
