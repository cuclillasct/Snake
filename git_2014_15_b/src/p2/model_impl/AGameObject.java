package p2.model_impl;

import org.json.JSONException;
import org.json.JSONObject;

import p2.basic.Coordinate;
import p2.basic.IGameObject;
import p2.basic.IJSONizable;
import p2.basic.ParamException;


public abstract class AGameObject implements IGameObject {
	
	protected Coordinate pos = new Coordinate();
	protected String id = new String();
	protected String name = new String();
	protected int value = 3;
	
	// Labels for serialization.
	// Subclasses should add their particular labels,
	public static final String PositionLabel = "pos";
	public static final String IdLabel = "id";
	public static final String NameLabel = "name";
	public static final String ValueLabel = "value";
	
	public AGameObject(){}
	
	public AGameObject(String id, String name, int value, Coordinate pos){
		this.id = new String(id);
		this.name = new String(name);
		this.value = value;
		this.pos = new Coordinate(pos.getColumn(), pos.getRow());
	}
	
	public AGameObject(JSONObject jsonObj) throws JSONException, ParamException {
		try{
			if (!jsonObj.getString(IJSONizable.TypeLabel).equals(this.getClass().getName())){
				System.out.println(this.getClass().getName() + ".Constructor: ParamException. jSONObj is malformed");
				throw new ParamException(this.getClass().getName() + ".Constructor.jSONObj is malformed");
			}
			this.value  = jsonObj.getInt(AGameObject.ValueLabel);
			this.id     = jsonObj.getString(AGameObject.IdLabel);
			this.name   = jsonObj.getString(AGameObject.NameLabel);
			this.pos    = new Coordinate(jsonObj.getJSONObject(AGameObject.PositionLabel));
		}
		catch(JSONException je){
			je.printStackTrace();
			System.out.println(jsonObj.toString());
			System.out.println(this.getClass().getName() + ".Constructor: JSONException. jSONObj is malformed");
			throw je;			
		}	
	}
	
	public AGameObject(String str_snake_link) throws JSONException, ParamException{
		this(new JSONObject(str_snake_link));
	}

	@Override
	public JSONObject toJSONObject() {
		JSONObject jObj = new JSONObject();
		try {
			jObj.put(IJSONizable.TypeLabel, this.getClass().getName());
			jObj.put(AGameObject.IdLabel, this.id);
			jObj.put(AGameObject.NameLabel, this.name);
			jObj.put(AGameObject.ValueLabel, this.value);
			jObj.put(AGameObject.PositionLabel, this.pos.toJSONObject());
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObj;
	}

	@Override
	public String serialize() {
		return toJSONObject().toString();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getDescription() {
		return name;
	}

	@Override
	public int getValue() {
		return value;
	}

	@Override
	public void setValue(int value) {
		this.value = value;

	}

	@Override
	public Coordinate getCoordinate() {
		return pos;
	}
}
