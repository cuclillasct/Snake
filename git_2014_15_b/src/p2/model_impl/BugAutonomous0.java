package p2.model_impl;

import java.util.Random;

import org.json.JSONException;
import org.json.JSONObject;

import p2.basic.Coordinate;
import p2.basic.IAutonomousObject;
import p2.basic.IGameConstants;
import p2.basic.IGameObservable;
import p2.basic.IGameObserver;
import p2.basic.ParamException;
import p2.basic.tooMuchShiftException;

public class BugAutonomous0 extends Bug implements IAutonomousObject {
	
	int direction = IGameConstants.Right;
	
	public BugAutonomous0(){}
	
	public BugAutonomous0(String id, String name, int value, Coordinate pos){
		super(id, name, value, pos);
	}
	
	public BugAutonomous0(JSONObject jsonObj) throws JSONException, ParamException {
		super(jsonObj);
	}
	
	public BugAutonomous0(String str_snake_link) throws JSONException, ParamException{
		super(str_snake_link);
	}

	@Override
	public int getDirection(char[][] board) {
		return direction;
	}
	
	@Override
	public Coordinate getNextCoordinate(char[][] board){
		Coordinate nextCoord = null;

		switch(randInt(1, 4)){
		case IGameConstants.Up: 
			nextCoord = new Coordinate(this.getCoordinate().getColumn(), this.getCoordinate().getRow()-1);
			break;
		case IGameConstants.Down:
			nextCoord = new Coordinate(this.getCoordinate().getColumn(), this.getCoordinate().getRow()+1);
			break;
		case IGameConstants.Left:
			nextCoord = new Coordinate(this.getCoordinate().getColumn()-1, this.getCoordinate().getRow());
			break;
		case IGameConstants.Right:
			nextCoord = new Coordinate(this.getCoordinate().getColumn()+1, this.getCoordinate().getRow());
			break;			
		default:break;
		}
		if(nextCoord.getRow() < 0 || nextCoord.getColumn() < 0 || nextCoord.getRow() == board.length || nextCoord.getColumn() == board[1].length || board[nextCoord.getColumn()][nextCoord.getRow()] == IGameConstants.Obstacle){
			return getNextCoordinate(board);
		}
    	return nextCoord;
	}
	
	private boolean collisionWithLimits(char[][] board) {
		return false;
	}
	 
	private boolean collisionWithObstacle(char[][] board){
		return false;
	}
	
	public static int randInt(int min, int max) {

	    // NOTE: Usually this should be a field rather than a method
	    // variable so that it is not re-seeded every call.
	    Random rand = new Random();

	    // nextInt is normally exclusive of the top value,
	    // so add 1 to make it inclusive
	    int randomNum = rand.nextInt((max - min + 1)) + min;

	    return randomNum;
	}
}
