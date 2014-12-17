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
	
	public static final int BUSCA_FRUTAS = 1;
	public static final int BUSCA_FRUTAS_Y_ESQUIVA_OBSTACULOS = 2;
	
	protected ArrayList<SnakeLink> links = new ArrayList<>();
	public static final String LinksLabel = "links";
	int dir, estrategia;
	Coordinate currentObjetive = null;
	
	public SnakeAutonomous0(){
		this.dir = IGameConstants.Right;
		this.estrategia = BUSCA_FRUTAS;
	}
	
	public SnakeAutonomous0(SnakeLink head){
		super(head);
		this.dir = IGameConstants.Right;
		this.estrategia = BUSCA_FRUTAS;
	}
	
	public SnakeAutonomous0(String id, String name, int value, Coordinate pos){
		super(id, name, value, pos);
		this.dir = IGameConstants.Right;
		this.estrategia = BUSCA_FRUTAS;
	}
	
	public SnakeAutonomous0(JSONObject jsonObj) throws JSONException, ParamException {
		super(jsonObj);	
		this.dir = jsonObj.getInt(Game_0.DirectionLabel);
		this.estrategia = BUSCA_FRUTAS;
	}

	@Override
	public int getDirection(char[][] board) {
		return dir;
	}
	
	public void setDirection (int direction){
		this.dir = direction;
	}
	
	// Métodos de inteligencia
	
	@Override
	public Coordinate getNextCoordinate(char[][] board){
		
		Coordinate nextCoord = null;
		
		// Buscamos un objetivo hacia el que movernos.
		if(currentObjetive == null) currentObjetive = findObjective(board);
		
		// Buscando el camino hacia el objetivo, encontramos el mejor movimiento (direccion a la que movernos)
		dir = estrategia == BUSCA_FRUTAS ? getBestNextMovement(board): getBestNextMovementNoObs(board);
		
		// Finalmente encontramos la coordenada donde queremos estar
		nextCoord = getCoordinateObjective(pos, dir);
		
    	return nextCoord;
	}
	
	private Coordinate findObjective(char [][] board){
		
		Coordinate coord = new Coordinate();
		double distance = Double.MAX_VALUE, distAux = 0;
		
		// Buscamos la fruta más cercana, y la establecemos como coordenada OBJETIVO
		for (int i = 0; i < board[0].length; i++) {
			for (int j = 0; j < board.length; j++) {
				if (board[i][j] == IGameConstants.Fruit) {
					distAux = Math.sqrt(Math.pow(j - pos.getRow(), 2) + Math.pow(i - pos.getColumn(), 2));
					if (distance > distAux) {
						distance = distAux;
						coord.setRow(j); coord.setColumn(i);	
					}
				}
			}
		}
		System.out.println(coord.getRow() + ";" + coord.getColumn() + " a " + distance);
		// Si no hay frutas, nos quedamos quietos, deberíamos haber terminado...
		if(distance == Double.MAX_VALUE) return this.getCoordinate();
		
		return coord;
	}
	
	public void setNoObjective(){
		currentObjetive = null;
	}
	
	private int getBestNextMovement(char [][] board) {
		int filaFruta = currentObjetive.getRow();
		int columnaFruta = currentObjetive.getColumn();
		int filaSnake = pos.getRow();
		int columnaSnake = pos.getColumn();
		
		// Comprobamos si está en linea recta
		if (filaFruta == filaSnake) {
			if (columnaFruta > columnaSnake) {
				return IGameConstants.Right;
			}else {
				return IGameConstants.Left;
			}
		}else if (columnaFruta == columnaSnake) {
			if (filaFruta > filaSnake) {
				return IGameConstants.Down;
			}else {
				return IGameConstants.Up;
			}
		}
		
		/** Si no está en línea recta se moverá eligiendo de la siguiente manera:
		- De los dos caminos posibles, si en uno hay obstáculo, coge el otro camino.
		- Si tengo los dos caminos libres, elige aleatoriamene - HECHO
		**/
		if (filaFruta >  filaSnake) {
			if (columnaFruta > columnaSnake) {
				return aleatorioEntre(IGameConstants.Down, IGameConstants.Right);
			}else if(columnaFruta < columnaSnake){
				return aleatorioEntre(IGameConstants.Down, IGameConstants.Left);
			}
		}else if (filaFruta <  filaSnake) {
			if (columnaFruta > columnaSnake) {
				return aleatorioEntre(IGameConstants.Up, IGameConstants.Right);
			}else if(columnaFruta < columnaSnake){
				return aleatorioEntre(IGameConstants.Up, IGameConstants.Left);
			}
		}
		
		return 0; 
	}
	
	private int getBestNextMovementNoObs(char [][] board) {
		int filaFruta = currentObjetive.getRow();
		int columnaFruta = currentObjetive.getColumn();
		int filaSnake = pos.getRow();
		int columnaSnake = pos.getColumn();
		int dirMovimiento = getBestNextMovement(board);
		Coordinate coordObjective = getCoordinateObjective(pos, dirMovimiento);
		int filaObjetivo = coordObjective.getRow();
		int columnaObjetivo = coordObjective.getColumn();
		
		if (board[columnaObjetivo][filaObjetivo] == IGameConstants.Obstacle) {
			if (dirMovimiento == IGameConstants.Up) {
				if (board[columnaObjetivo - 1][filaObjetivo - 1] == IGameConstants.Obstacle && board[columnaObjetivo + 1][filaObjetivo - 1] == IGameConstants.Obstacle) {
					// No se que hacer
				}else if (board[columnaObjetivo - 1][filaObjetivo - 1] == IGameConstants.Obstacle || board[columnaObjetivo + 1][filaObjetivo - 1] == IGameConstants.Obstacle) {
					if (board[columnaObjetivo - 1][filaObjetivo - 1] == IGameConstants.Obstacle) {
						return IGameConstants.Right;
					}else {
						return IGameConstants.Left;
					}
				}else {
					return aleatorioEntre(IGameConstants.Right, IGameConstants.Left);
				}
			}
			if (dirMovimiento == IGameConstants.Right) {
				if (board[columnaObjetivo - 1][filaObjetivo - 1] == IGameConstants.Obstacle && board[columnaObjetivo - 1][filaObjetivo + 1] == IGameConstants.Obstacle) {
					// No se que hacer
				}else if (board[columnaObjetivo - 1][filaObjetivo - 1] == IGameConstants.Obstacle || board[columnaObjetivo - 1][filaObjetivo + 1] == IGameConstants.Obstacle) {
					if (board[columnaObjetivo - 1][filaObjetivo - 1] == IGameConstants.Obstacle) {
						return IGameConstants.Down;
					}else {
						return IGameConstants.Up;
					}
				}else {
					return aleatorioEntre(IGameConstants.Down, IGameConstants.Up);
				}
			}
			if (dirMovimiento == IGameConstants.Down) {
				if (board[columnaObjetivo - 1][filaObjetivo - 1] == IGameConstants.Obstacle && board[columnaObjetivo + 1][filaObjetivo + 1] == IGameConstants.Obstacle) {
					// No se que hacer
				}else if (board[columnaObjetivo - 1][filaObjetivo - 1] == IGameConstants.Obstacle || board[columnaObjetivo + 1][filaObjetivo + 1] == IGameConstants.Obstacle) {
					if (board[columnaObjetivo - 1][filaObjetivo - 1] == IGameConstants.Obstacle) {
						return IGameConstants.Right;
					}else {
						return IGameConstants.Left;
					}
				}else {
					return aleatorioEntre(IGameConstants.Right, IGameConstants.Left);
				}
			}
			if (dirMovimiento == IGameConstants.Left) {
				if (board[columnaObjetivo + 1][filaObjetivo - 1] == IGameConstants.Obstacle && board[columnaObjetivo + 1][filaObjetivo + 1] == IGameConstants.Obstacle) {
					// No se que hacer
				}else if (board[columnaObjetivo + 1][filaObjetivo - 1] == IGameConstants.Obstacle || board[columnaObjetivo + 1][filaObjetivo + 1] == IGameConstants.Obstacle) {
					if (board[columnaObjetivo + 1][filaObjetivo - 1] == IGameConstants.Obstacle) {
						return IGameConstants.Down;
					}else {
						return IGameConstants.Up;
					}
				}else {
					return aleatorioEntre(IGameConstants.Down, IGameConstants.Up);
				}
			}
		}
		
		return dirMovimiento; 
	}
	
	private Coordinate getCoordinateObjective(Coordinate coord, int direction){
		Coordinate nextCoord;
		switch (direction) {
		case IGameConstants.Up: 
			nextCoord = new Coordinate(coord.getColumn(), coord.getRow()-1);
			break;
		case IGameConstants.Down:
			nextCoord = new Coordinate(coord.getColumn(), coord.getRow()+1);
			break;
		case IGameConstants.Left:
			nextCoord = new Coordinate(coord.getColumn()-1, coord.getRow());
			break;
		case IGameConstants.Right:
			nextCoord = new Coordinate(coord.getColumn()+1, coord.getRow());
			break;			
		default:
			nextCoord = coord;
			break;
		}
		return nextCoord;
	}
	
	private boolean collisionWithLimits(char[][] board) {
		return false;
	}
	 
	private boolean collisionWithObstacle(char[][] board){
		return false;
	}
	
	public void setEstrategia(int str){
		estrategia = str;
	}

	@Override
	public JSONObject toJSONObject() {
		// TODO Auto-generated method stub
		JSONObject json = super.toJSONObject();
		try {
			json.put(Game_0.DirectionLabel, dir);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	
	// Utilidad
	private int aleatorioEntre(int a, int b){
		int r = BugAutonomous0.randInt(0, 1);
		if (r == 0) {
			return a;
		} else {
			return b;
		}
	}
	
	
}
