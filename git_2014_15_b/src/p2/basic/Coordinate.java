package p2.basic;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Models a location of a game cell in the game board.
 * @author LSI-JAPF
 */
public class Coordinate implements IJSONizable, Comparable {
	
	protected int column, row;
	
	// Labels for serialization.
	public static final String TypeName = "Coordinate";
	public static final String ColumnLabel = "col";
	public static final String RowLabel = "row";

	/**
	 * Crea una posición en (0,0)
	 */
	public Coordinate(){}
	
	/**
	 * Crea una posición en (column, row)
	 * @param column columna
	 * @param row fila
	 */
	public Coordinate(int column, int row){
		this.column = column;
		this.row = row;
	}
	
	/**
	 * Crea un objeto Coordinate a partir de su representación JSON
	 * @param jsonObj representación del objeto serializado.
	 * @throws ParamException Se lanza si el objeto no representa a una coordenada.
	 * @throws JSONException Se lanza si alguno de los campos del JSONObject no se
	 *         corresponde con los que se experan en una coordenada o alguno 
	 *         de los campos está ausente.
	 */
	public Coordinate(JSONObject jsonObj) throws JSONException, ParamException {
		try{
			if (!jsonObj.getString(IJSONizable.TypeLabel).equals(TypeName)){
				System.out.println("Coordinate.Constructor: ParamException. jSONObj is malformed");
				throw new ParamException("Coordinate.Constructor.jSONObj is malformed");
			}
			this.row     = jsonObj.getInt(Coordinate.RowLabel);
			this.column  = jsonObj.getInt(Coordinate.ColumnLabel);
		}
		catch(JSONException je){
			je.printStackTrace();
			System.out.println(jsonObj.toString());
			System.out.println("Coordinate.Constructor: JSONException. jSONObj is malformed");
			throw je;			
		}		
	}
	
	/**
	 * Crea una coordenada a partir de un string en formato JSON.
	 * @param strCoord representación del objeto serializado.
	 * @throws JSONException Se lanza si el objeto no representa a una coordenada.
	 * @throws ParamException Se lanza si alguno de los campos del JSONObject no se
	 *         corresponde con los que se experan en una coordenada o alguno 
	 *         de los campos está ausente.
	 */
	public Coordinate(String strCoord) throws JSONException, ParamException {
		this(new JSONObject(strCoord));
	}
	
	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}

	public int getRow() {
		return row;
	}

	public void setRow(int row) {
		this.row = row;
	}

	public static String getColumnlabel() {
		return ColumnLabel;
	}

	@Override
	public JSONObject toJSONObject() {
		// Se crea objeto JSON vacío.
		JSONObject jObj = new JSONObject();
		try {
			// Se le van añadiendo los campos del objeto.
			
			// Primero: una indicación del tipo de datos.
			jObj.put(IJSONizable.TypeLabel, Coordinate.TypeName);
			
			// Después el resto de los datos.
			jObj.put(Coordinate.ColumnLabel, this.column);
			jObj.put(Coordinate.RowLabel, this.row);			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jObj;
	}

	@Override
	public String serialize() {
		return toJSONObject().toString();
	}
	
	public String toString(){
		return "C(" + column + ", " + row + ")";
	}
	
	// For testing.
	public static void main(String [] args) throws JSONException, ParamException{
		System.out.println("Coordinate.main.Testing class SnakeLink");
		for (int i = 0; i < 10; i++){
			Coordinate c = new Coordinate(i, (i%2 == 0)?i:-i);
			System.out.println(c);
			JSONObject json = c.toJSONObject();
			System.out.println(json);
			System.out.println(new Coordinate(json));
			System.out.println("-------------------------------------");
		}
	}

	@Override
	public int compareTo(Object arg0) {
		if (arg0 instanceof Coordinate){
			Coordinate coor = (Coordinate) arg0;
			if (this.row == coor.row && this.column == coor.column){
				return 0;
			}
			else{
				return 1;
			}
		}
		return -1;
	}

}
