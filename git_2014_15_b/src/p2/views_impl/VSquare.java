package p2.views_impl;

import java.awt.Color;
import java.awt.Graphics;

import p2.basic.IView;

public class VSquare implements IView {
	
	protected String id = new String();
	protected int edge = 30;
	protected Color myColor = Color.GREEN;
	
	
	public VSquare(){}
	
	public VSquare(String id){
		this.id = id;
	}
	
	public VSquare(String id, Color c){
		this.id = id;
		myColor = c;
	}
	
	@Override
	public String getId() {
		return id;
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		Color c = g.getColor();
		g.setColor(myColor);
		g.fillRect(x, y, edge, edge); 
		g.setColor(c);
	}

	@Override
	public int getSize() {
		return edge;
	}

	@Override
	public void setSize(int edge) {
		this.edge = edge;
	}
}
