package p2.model_impl;

import org.json.JSONException;
import org.json.JSONObject;

import p2.basic.Coordinate;
import p2.basic.NoMobileObjectException;
import p2.basic.ParamException;
import p2.basic.tooMuchShiftException;

public class Bug extends AGameObject {

	public Bug(){}
	
	public Bug(String id, String name, int value, Coordinate pos){
		super(id, name, value, pos);
	}
	
	public Bug(JSONObject jsonObj) throws JSONException, ParamException {
		super(jsonObj);
	}
	
	public Bug(String str_snake_link) throws JSONException, ParamException{
		super(str_snake_link);
	}
	
	@Override
	public void setCoordinate(Coordinate coord) throws tooMuchShiftException {
		if (coord != null) {
			int dx = this.pos.getColumn() - coord.getColumn();
			int dy = this.pos.getRow() - coord.getRow();
			dx = (dx >= 0)?dx:-dx;
			dy = (dy >= 0)?dy:-dy;
			if (dx > 1 || dy > 1 || (dx != 0 && dy != 0) ){
				throw new tooMuchShiftException();
			}
			else {
				this.pos.setColumn(coord.getColumn());
				this.pos.setRow(coord.getRow());
			}	
		}
	}
	
	public void incColumn(){
		try {
			setCoordinate(new Coordinate(getCoordinate().getColumn() + 1, getCoordinate().getRow()));
		} catch (tooMuchShiftException e) {
			e.printStackTrace();
		}
	}
	
	public void decColumn(){
		try {
			setCoordinate(new Coordinate(getCoordinate().getColumn() - 1, getCoordinate().getRow()));
		} catch (tooMuchShiftException e) {
			e.printStackTrace();
		}		
	}
	
	public void incRow(){
		try {
			setCoordinate(new Coordinate(getCoordinate().getColumn(), getCoordinate().getRow()+1));
		} catch (tooMuchShiftException e) {
			e.printStackTrace();
		}		
	}
	
	public void decRow(){
		try {
			setCoordinate(new Coordinate(getCoordinate().getColumn(), getCoordinate().getRow()-1));
		} catch (tooMuchShiftException e) {
			e.printStackTrace();
		}		
	}
	
	public String toString(){
		return "SNL(" + id + ", " + name + ") of value: " + value + " at " + pos.toString();
	}
	
	// For testing.
	public static void main(String [] args) throws JSONException, ParamException{
		System.out.println("Bug.main.Testing class SnakeLink");
		for (int i = 0; i < 10; i++){
			Bug c = new Bug("SN_id_"+i,"SN_name_"+ i, (i%2 == 0)?i:-i , new Coordinate(i, i) );
			System.out.println(c);
			JSONObject json = c.toJSONObject();
			System.out.println(json);
			System.out.println(new Bug(json));
			System.out.println("-------------------------------------");
		}
	}	
	
}
