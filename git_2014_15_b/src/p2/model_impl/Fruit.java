package p2.model_impl;

import org.json.JSONException;
import org.json.JSONObject;

import p2.basic.Coordinate;
import p2.basic.IGameObject;
import p2.basic.NoMobileObjectException;
import p2.basic.ParamException;
import p2.basic.tooMuchShiftException;

public class Fruit extends AGameObject {


	public Fruit(){}
	
	public Fruit(String id, String name, int value, Coordinate pos){
		super(id, name, value, pos);
	}
	
	public Fruit(JSONObject jsonObj) throws JSONException, ParamException {
		super(jsonObj);
	}
	
	public Fruit(String str_snake_link) throws JSONException, ParamException{
		super(str_snake_link);
	}

	@Override
	public void setCoordinate(Coordinate coord) throws NoMobileObjectException,
			tooMuchShiftException {
		// TODO Auto-generated method stub

	}

}
