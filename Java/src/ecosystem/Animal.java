package ecosystem;

import agents.Behavior;
import agents.Boid;
import agents.DNA;
import agents.Eye;
import cellularAutomata.Patch;
import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import tools.SubPlot;

public abstract class Animal extends Boid implements IAnimal{
	
	protected float energy;
	protected Body target;//guardar a presa se houver

	public Animal(PVector pos, float mass, float radius, int color, PApplet p, SubPlot plt, PImage img) {
		super(pos, mass, radius, color, p, plt, img);
	}
	
	protected Animal(Animal a, Boolean mutate, PApplet p, SubPlot plt, String str, PImage img) {
		super(a.pos, a.mass, a.radius, a.color, p, plt, img);
		for(Behavior b : a.behaviors) {
			this.addBehavior(b);
		}
		if (a.eye != null) {
			eye = new Eye(this, a.eye);
		}
		dna = new DNA(a.dna, mutate, str );
	}
	
	@Override
	public boolean die() {
		return(energy < 0);
	}
	
	public void energy_consumption(float dt, Terrain terrain) {
		//energy -= dt; //basic metabolism
		Patch patch = (Patch)terrain.world2Cell(pos.x, pos.y);
		
		if (this instanceof Prey) {
			energy  -=.10*mass*Math.pow(vel.mag(), 2)*dt;
			if(patch.getState() == WorldConstants.PatchType.OBSTACLE.ordinal()) {
				energy -= 10*dt;
				this.vel.mult(0.7f);
			}
		}
		
		if (this instanceof Predator) {
			energy  -=.25*mass*Math.pow(vel.mag(), 2)*dt;
			if(patch.getState() == WorldConstants.PatchType.OBSTACLE.ordinal()) {
				energy -= 25*dt;
				this.vel.mult(0.8f);
			}
		}
		
		if (this instanceof SuperPredator) {
			energy  -=.10*mass*Math.pow(vel.mag(), 2)*dt;
			if(patch.getState() == WorldConstants.PatchType.OBSTACLE.ordinal()) {
				energy -= 50*dt;
				this.vel.mult(0.9f);
			}
		}
	}

}
