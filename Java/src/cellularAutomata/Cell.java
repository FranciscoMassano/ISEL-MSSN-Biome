package cellularAutomata;

import java.nio.channels.Pipe;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PShape;

public class Cell {
	protected int row, col;
	protected int state;
	protected Cell[] neighbors;
	protected CellularAutomata ca;
	protected PImage img;
	private PShape shape;
	
	public Cell(CellularAutomata ca, int row, int col) {
		this.ca = ca;
		this.row = row;
		this.col = col;
		this.state = 0;
		this.neighbors = null;
	}
	
	public void setNeighbors(Cell[] neigh) {
		this.neighbors = neigh;
	}
	
	public Cell[] getNeighbors() {
		return neighbors;
	}
	
	public void setState(int state) {
		this.state = state;
	}
	
	public int getState() {
		return state;
	}
	
	public void display(PApplet p) {
		p.pushStyle();
		p.noStroke();
		p.fill(ca.getStateColors()[state]);
		p.rect(ca.xmin+col*ca.cellWidth, ca.ymin+row*ca.cellHeight, ca.cellWidth, ca.cellHeight);
		
		//shape = p.createShape();
		//shape.beginShape();
		//shape.texture(img);
		//shape.vertex(ca.xmin+col*ca.cellWidth,ca.ymin+row*ca.cellHeight);
		//shape.vertex(ca.xmin+col*ca.cellWidth + ca.cellWidth ,ca.ymin+row*ca.cellHeight);
		//shape.vertex(ca.xmin+col*ca.cellWidth,ca.ymin+row*ca.cellHeight + ca.cellHeight);
		//shape.vertex(ca.xmin+col*ca.cellWidth + ca.cellWidth,ca.ymin+row*ca.cellHeight + ca.cellHeight);

		
		//shape.endShape();
		p.popStyle();
	}

}
