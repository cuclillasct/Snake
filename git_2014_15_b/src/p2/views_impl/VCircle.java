package p2.views_impl;

import java.awt.Color;
import java.awt.Graphics;

import p2.basic.IView;

public class VCircle implements IView {
	
	protected String id;
	protected int edge = 30;
	protected Color myColor = Color.blue;
	
	public VCircle(){}
	
	public VCircle(String id){
		this.id = id;
	}
	
	public VCircle(String id, Color c){
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
		g.fillOval(x, y, edge, edge); 
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
