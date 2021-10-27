package agents;

import java.util.ArrayList;
import java.util.List;

import ecosystem.Predator;
import ecosystem.Prey;
import ecosystem.SuperPredator;
import physics.Body;
import processing.core.PApplet;
import processing.core.PConstants;
import processing.core.PImage;
import processing.core.PShape;
import processing.core.PVector;
import tools.SubPlot;

public class Boid extends Body{
	
	private PShape shape;
	protected DNA dna;
	protected Eye eye;
	protected List<Behavior> behaviors;
	protected float phiWander;
	private double[] window;
	private float sumWeights;
	private PImage imagem;

	public Boid(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt, PImage imagem) {
		super(pos, new PVector(), mass, radius, color);
		dna = new DNA();
		behaviors = new ArrayList<Behavior>();
		window = plt.getWindow();
		this.imagem = imagem;
	}
	
	public void mutateBehaviors() {
		for(Behavior behavior: behaviors) {
			
			if (this instanceof Prey) {
				
				if(behavior instanceof AvoidObstacle) {
					behavior.weight += DNA.random(-0.5f, 0.5f);
					behavior.weight = Math.max(0, behavior.weight);
				}
				
			}
			
			if (this instanceof Predator) {
				if(behavior instanceof Seek) {
					if (behavior.weight > 2) {
						behavior.weight += DNA.random(-0.2f, 0.2f);
						behavior.weight = Math.max(0, behavior.weight);
					}else {
						behavior.weight += DNA.random(0f, 0.1f);
						behavior.weight = Math.max(0, behavior.weight);
					}
					
				}
				
				if(behavior instanceof Wander) {
					behavior.weight += DNA.random(-0.1f, 0.5f);
					behavior.weight = Math.max(0, behavior.weight);
				}
				
				if(behavior instanceof AvoidObstacle) {
					behavior.weight += DNA.random(-0.5f, 0.5f);
					behavior.weight = Math.max(0, behavior.weight);
				}
			}
			
			if (this instanceof SuperPredator) {
				if(behavior instanceof Seek) {
					if (behavior.weight > 2) {
						behavior.weight += DNA.random(-0.1f, 0.1f);
						behavior.weight = Math.max(0, behavior.weight);
					}else {
						behavior.weight += DNA.random(0f, 0.3f);
						behavior.weight = Math.max(0, behavior.weight);
					}
					
				}
				
				if(behavior instanceof Wander) {
					behavior.weight += DNA.random(-0.2f, 0.2f);
					behavior.weight = Math.max(0, behavior.weight);
				}
				
				if(behavior instanceof AvoidObstacle) {
					behavior.weight += DNA.random(-0.5f, 0.5f);
					behavior.weight = Math.max(0, behavior.weight);
				}
			}
		}
		
	}
	
	public List<Behavior> getBehaviors(){
		return behaviors;
	}
	
	public DNA getDNA() {
		return dna;
	}
	
	public void setEye(Eye eye) {
		this.eye = eye;
	}
	
	public Eye getEye() {
		return eye;
	}
	
	public void setShape(PApplet p, SubPlot  plt, float radius, int color) {
		this.radius = radius;
		this.color = color;
		setShape(p, plt);
	}
	
	public void setShape(PApplet p, SubPlot plt) {
		float[] rr = plt.getVectorCoord(radius, radius);
		shape = p.createShape();
		shape.beginShape();
		shape.noStroke();
		shape.fill(color);
		shape.vertex(-rr[0], rr[0]/2);
		shape.vertex(rr[0], 0);
		shape.vertex(-rr[0], -rr[0]/2);
		shape.vertex(-rr[0]/2, 0);
		shape.endShape(PConstants.CLOSE);
	}
	
	private void updateSumWeights() {
		sumWeights = 0;
		for(Behavior beh : behaviors)
			sumWeights += beh.getWeight();
	}
	
	public void addBehavior(Behavior behavior) {
		behaviors.add(behavior);
		updateSumWeights();
	}
	
	public void removeBehavior(Behavior behavior) {
		if(behaviors.contains(behavior))
			behaviors.remove(behavior);
		updateSumWeights();
	}
	
	public void applyBehavior(int i, float dt) {
		if(eye != null) eye.look();
		Behavior behavior = behaviors.get(i);
		PVector vd = behavior.getDesiredVelocity(this);
		move(dt, vd);
	}

	public void applyBehaviors(float dt) {
		if(eye != null) eye.look();
		
		PVector vd = new PVector();
		for(Behavior behavior : behaviors) {
			PVector vdd = behavior.getDesiredVelocity(this);
			vdd.mult(behavior.getWeight()/sumWeights);
			vd.add(vdd);
		}
		move(dt, vd);
	}
	
	public void move(float dt, PVector vd) {
		vd.normalize().mult(dna.maxSpeed);
		PVector fs = PVector.sub(vd, vel);
		applyForce(fs.limit(dna.maxForce));
		super.move(dt);
		if(pos.x < window[0])
			pos.x += window[1] - window[0];
		if(pos.y < window[2])
			pos.y += window[3] - window[2];
		if(pos.x >= window[1])
			pos.x -= window[1] - window[0];
		if(pos.y >= window[3])
			pos.y -= window[3] - window[2];
	}
	
	public void move(float dt) {
		super.move(dt);
		if(pos.x < window[0])
			pos.x += window[1] - window[0];
		if(pos.y < window[2])
			pos.y += window[3] - window[2];
		if(pos.x >= window[1])
			pos.x -= window[1] - window[0];
		if(pos.y >= window[3])
			pos.y -= window[3] - window[2];
	}
	
	@Override
	public void display(PApplet p, SubPlot plt) {
		p.pushMatrix();
		float[] pp = plt.getPixelCoord(pos.x, pos.y);
		float[] vv = plt.getVectorCoord(vel.x, vel.y);
		p.translate(pp[0], pp[1]);
		p.rotate(-vel.heading());
		//p.shape(shape);
		p.image(imagem, 0, 0);
		p.popMatrix();
	}
	
}
