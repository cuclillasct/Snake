package p2.views_impl;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import p2.basic.IView;

public class VImage implements IView {
	
	private BufferedImage image;
	protected String id;
	protected int edge = 30;

	public VImage(String id,String imgFile) throws IOException {
		this.id = id;      
		image = ImageIO.read(new File(imgFile));
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public void draw(Graphics g, int x, int y) {
		g.drawImage(image, x, y, edge, edge, null);		
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
