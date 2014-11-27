package p2.model_impl;

import org.json.JSONException;
import org.json.JSONObject;

import p2.basic.Coordinate;
import p2.basic.IAutonomousObject;
import p2.basic.IGameConstants;
import p2.basic.IGameObservable;
import p2.basic.IGameObserver;
import p2.basic.ParamException;
import p2.basic.tooMuchShiftException;

public class SnakeAutonomous0 extends Snake implements IAutonomousObject {
	
	public SnakeAutonomous0(){
	}
	
	public SnakeAutonomous0(SnakeLink head){
		super(head);
	}
	
	public SnakeAutonomous0(String id, String name, int value, Coordinate pos){
		super(id, name, value, pos);
	}
	
	public SnakeAutonomous0(JSONObject jsonObj) throws JSONException, ParamException {
		super(jsonObj);
	}

	@Override
	public int getDirection(char[][] board) {
		return 0;
	}
	
	@Override
	public Coordinate getNextCoordinate(char[][] board){
		return null;
	}
	
	private boolean collisionWithLimits(char[][] board) {
		return false;
	}
	 
	private boolean collisionWithObstacle(char[][] board){
		return false;
	}
}
