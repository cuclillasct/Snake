package p2.model_impl;

import org.json.JSONException;
import org.json.JSONObject;

import p2.basic.Coordinate;
import p2.basic.NoMobileObjectException;
import p2.basic.ParamException;
import p2.basic.tooMuchShiftException;

public class Obstacle extends AGameObject {
	
	public Obstacle(){}
	
	public Obstacle(String id, String name, int value, Coordinate pos){
		super(id, name, value, pos);
	}
	
	public Obstacle(JSONObject jsonObj) throws JSONException, ParamException {
		super(jsonObj);
	}
	
	public Obstacle(String str_snake_link) throws JSONException, ParamException{
		super(str_snake_link);
	}

	@Override
	public void setCoordinate(Coordinate coord) throws NoMobileObjectException,
			tooMuchShiftException {
		throw new NoMobileObjectException();

	}

}
