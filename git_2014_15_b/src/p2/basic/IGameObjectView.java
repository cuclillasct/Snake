package p2.basic;

import java.awt.Color;
import java.awt.Graphics;

public interface IGameObjectView {
	String getId();
	void draw(Graphics g, int columna, int fila);
	void setSize(int tam);
	void setColor(Color c);
}
