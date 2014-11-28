package p2.model_impl;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import p2.basic.Coordinate;
import p2.basic.IAutonomousObject;
import p2.basic.IGameConstants;
import p2.basic.IGameObservable;
import p2.basic.IGameObserver;
import p2.basic.IJSONizable;
import p2.basic.ParamException;
import p2.basic.tooMuchShiftException;

public class SnakeAutonomous0 extends Snake implements IAutonomousObject {
	
	protected ArrayList<SnakeLink> links = new ArrayList<>();
	public static final String LinksLabel = "links";
	int dir;
	
	public SnakeAutonomous0(){
		this.dir = IGameConstants.Right;
	}
	
	public SnakeAutonomous0(SnakeLink head){
		super(head);
		this.dir = IGameConstants.Right;
	}
	
	public SnakeAutonomous0(String id, String name, int value, Coordinate pos){
		super(id, name, value, pos);
		this.dir = IGameConstants.Right;
	}
	
	public SnakeAutonomous0(JSONObject jsonObj) throws JSONException, ParamException {
		super(jsonObj);
		this.dir	= jsonObj.getInt(IGameConstants.DirectionLabel);	
	}

	@Override
	public int getDirection(char[][] board) {
		return dir;
	}
	
	@Override
	public Coordinate getNextCoordinate(char[][] board){
		Coordinate coord = null;
		
		switch (this.dir) {
		case IGameConstants.Down:
			coord = new Coordinate(this.pos.getRow()+1,this.pos.getColumn());
			break;
		case IGameConstants.Up:
			coord = new Coordinate(this.pos.getRow()-1,this.pos.getColumn());
			break;
		case IGameConstants.Left:
			coord = new Coordinate(this.pos.getRow(),this.pos.getColumn()-1);
			break;
		case IGameConstants.Right:
			coord = new Coordinate(this.pos.getRow(),this.pos.getColumn()+1);
			break;
		default:
			break;
		}
		
		return coord;
	}
	
	private boolean collisionWithLimits(char[][] board) {
		return false;
	}
	 
	private boolean collisionWithObstacle(char[][] board){
		return false;
	}
	
}
