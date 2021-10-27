package physics;

import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import tools.SubPlot;

public class Body extends Mover{
	
	protected int color;
	protected float radius;

	public Body(PVector pos, PVector vel, float mass, float radius, int color) {
		super(pos, vel, mass);
		this.color = color;
		this.radius = radius;
	}
	
	public Body(PVector pos) {
		super(pos, new PVector(), 0f);
	}
	
	public void display(PApplet p, SubPlot plt) {
		p.pushStyle();
		float[] pp = plt.getPixelCoord(pos.x, pos.y);
		float[] r = plt.getVectorCoord(radius, radius);
		p.noStroke();
		p.fill(color);
		p.circle(pp[0], pp[1], 2*r[0]);
		p.popStyle();
	}

	public void display(PApplet p, SubPlot plt, PImage imagem) {
		// TODO Auto-generated method stub
		p.pushMatrix();
		float[] pp = plt.getPixelCoord(pos.x, pos.y);
		p.translate(pp[0], pp[1]);
		p.image(imagem, 0, 0);
		p.popMatrix();
	}
}
