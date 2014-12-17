package p2.model_impl;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import p2.basic.Coordinate;
import p2.basic.IGameCharacter;
import p2.basic.IJSONizable;
import p2.basic.NoMobileObjectException;
import p2.basic.ParamException;
import p2.basic.tooMuchShiftException;

public class Snake extends AGameObject implements IGameCharacter {
	
	protected ArrayList<SnakeLink> links = new ArrayList<>();
	public static final String LinksLabel = "links";

	public Snake(){
		SnakeLink head = new SnakeLink();
		this.id = head.id;
		this.value = head.value;
		this.name = head.name;
		this.pos = head.getCoordinate();
		links.add(head);
	}
	
	public Snake(SnakeLink head){
		this.id = head.id;
		this.value = head.value;
		this.name = head.name;
		this.pos = head.getCoordinate();
		links.add(head);
	}
	
	public Snake(String id, String name, int value, Coordinate pos){
		SnakeLink head = new SnakeLink(id, name, value, pos);
		this.id = head.id;
		this.value = head.value;
		this.name = head.name;
		this.pos = head.getCoordinate();
		links.add(head);
	}
	
	public Snake(JSONObject jsonObj) throws JSONException, ParamException {
		try{
			if (!jsonObj.getString(IJSONizable.TypeLabel).equals(this.getClass().getName())){
				System.out.println(this.getClass().getName() + ".Constructor: ParamException. jSONObj is malformed");
				throw new ParamException(this.getClass().getName() + ".Constructor.jSONObj is malformed");
			}
			this.value  = jsonObj.getInt(AGameObject.ValueLabel);
			this.id     = jsonObj.getString(AGameObject.IdLabel);
			this.name   = jsonObj.getString(AGameObject.NameLabel);
			this.pos    = new Coordinate(jsonObj.getJSONObject(AGameObject.PositionLabel));
			JSONArray jarr = jsonObj.getJSONArray(LinksLabel);
			for (int i = 0; i < jarr.length(); i++){
				links.add(new SnakeLink(jarr.getJSONObject(i)));
			}
		}
		catch(JSONException je){
			je.printStackTrace();
			System.out.println(jsonObj.toString());
			System.out.println(this.getClass().getName() + ".Constructor: JSONException. jSONObj is malformed");
			throw je;			
		}		
	}
	
	public Snake(String str_snake) throws JSONException, ParamException {
		this(new JSONObject(str_snake));
	}
	
	@Override
	public Coordinate getCoordinate() {
		return links.get(0).getCoordinate();
	}

	@Override
	public void setCoordinate(Coordinate coord) throws tooMuchShiftException {
		Object mylinks [] =  links.toArray();
		for (int i = mylinks.length - 1; i >= 1; i--){
			Coordinate newCoord = ((SnakeLink) mylinks[i-1]).getCoordinate();
			((SnakeLink) mylinks[i]).setCoordinate(newCoord);
		}
		((SnakeLink) mylinks[0]).setCoordinate(coord);
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
	
	@Override
	public int getValue() {
		return links.get(0).getValue();
	}

	@Override
	public void setValue(int value) {
		links.get(0).value = value;
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject jObj = super.toJSONObject();
		try {			
			JSONArray jArr = new JSONArray();
			for (int i = 0; i < links.size(); i++){
				jArr.put(i, links.get(i).toJSONObject());
			}
			jObj.put(Snake.LinksLabel, jArr);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObj;
	}


	public SnakeLink addLink() {
		SnakeLink lnk = new SnakeLink(id, name, value, links.get(links.size()-1).getCoordinate());
		links.add(lnk);
		return lnk;
	}
	
	public SnakeLink addLink(SnakeLink lnk) {
		links.add(lnk);
		return lnk;
	}

	public SnakeLink removeLink() {
		if(links.size() > 1){
			return links.remove(links.size()-1);
		}
		return null;
	}
	
	public void removeLinks(){
		for (int i = 0; i <= getNumberOfLinks(); i++) {
			removeLink();
		}
	}

	public int getNumberOfLinks() {
		return links.size();
	}

	public ArrayList<SnakeLink> getLinks() {
		return (ArrayList<SnakeLink>) links.clone();
	}
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		sb.append("Snake links:\n");
		for (SnakeLink link : links){
			sb.append(link.toString() + "\n");
		}
		return sb.toString();
	}
	
	// For testing.
	public static void main(String [] args) throws JSONException, ParamException{
		System.out.println("Sanke.main.Testing class SnakeLink");
		Snake snake = new Snake();
		for (int i = 0; i < 11; i++){
			System.out.println(snake);
			JSONObject json = snake.toJSONObject();
			System.out.println(json);
			System.out.println(new Snake(json));
			System.out.print("Adding link and moving ");
			snake.addLink();
			if (i%5 == 0){
				System.out.println("to rigth");
				snake.incColumn();
			}
			else if (i%4 == 0){
				System.out.println("to left");
				snake.decColumn();
			}
			else if (i%3 == 0){
				System.out.println("down");
				snake.incRow();
			}
			else if (i%4 == 0){
				System.out.println("up");
				snake.decRow();
			}
			else {
				System.out.println("to rigth");
				snake.incColumn();
			}
			System.out.println("-------------------------------------");
		}
		for (int i = 0; i < 10; i++){
			System.out.print("Removing link ");
			snake.removeLink();
			System.out.println(snake);
			JSONObject json = snake.toJSONObject();
			System.out.println(json);
			System.out.println(new Snake(json));
			System.out.println("-------------------------------------");
		}		
	}
}
