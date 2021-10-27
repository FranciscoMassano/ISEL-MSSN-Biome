package ecosystem;

import java.util.ArrayList;
import java.util.List;

import agents.Eye;
import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import tools.SubPlot;

public class Predator extends Animal{
	
	private PApplet parent;
	private SubPlot plt;
	private PImage img;

	protected Predator(PVector pos, float mass, float radius, int color, PApplet parent, SubPlot plt, PImage img) {
		super(pos, mass, radius, color, parent, plt, img);
		this.parent = parent;
		this.plt = plt;
		this.img = img;
		energy = WorldConstants.INI_PREDATOR_ENERGY;
	}
	
	public Predator(Predator predator, Boolean mutate, PApplet parent, SubPlot plt, PImage img) {
		super(predator, mutate, parent, plt, "Predator", img);
		this.parent = parent;
		this.plt = plt;
		this.img = img;
		energy = WorldConstants.INI_PREDATOR_ENERGY;
	}

	@Override
	public List<Animal> eat(List<Animal> animals) {
		PVector mePos = this.getPos().copy();
		//System.out.println("me " + mePos);
		for (Animal animal : animals) {
			if (animal instanceof Prey) {
				PVector preyPos = animal.getPos().copy();
				//System.out.println(preyPos);
				float distance = PVector.dist(mePos, preyPos);
				//System.out.println("dist" + distance);
				if (distance < this.radius*4) {
					energy += WorldConstants.ENERGY_FROM_PREY;
					animals.remove(animal);
					//System.out.println("Prey Comida");
					return animals;
				}
			}
		}
		return animals;
	}
	
	@Override
	public void eat(Terrain terrain) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Animal reproduce(boolean mutate) {
		Animal child = null;
		if(energy > WorldConstants.PREDATOR_ENERGY_TO_REPRODUCE) {
			energy -= WorldConstants.INI_PREDATOR_ENERGY;
			child = new Predator(this, mutate, parent, plt, img);
			if (mutate) child.mutateBehaviors();
		}
		return child;
	}
	
	
	public void updateTargetPrey(PApplet p, List<Body> obstacles, List<Animal> allPrey) {
		float dist = 0;
		if (!obstacles.contains(this.target) && this.target != null) {
			//distancia entre o animal e a prey
			dist = PVector.dist(this.getPos(), this.target.getPos());
			if (dist > this.getDNA().visionSafeDistance) { 
				for (Animal animal : allPrey) {
					float newDist = PVector.dist(this.getPos(), animal.getPos());
					if (newDist < dist && this.target != animal && animal instanceof Prey) {
						this.target = animal;
						//System.out.println("alvo alterado");
					}
				}
				List<Body> tempTrackingBodies = new ArrayList<Body>();
				//adicionar o target e obstaculos a lista
				tempTrackingBodies.add(this.target);
				tempTrackingBodies.addAll(obstacles);
				//criar um novo Eye com essa lista
				Eye eye = new Eye(this, tempTrackingBodies);
				this.setEye(eye);
			}
		}	
	}

}
