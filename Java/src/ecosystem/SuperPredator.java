package ecosystem;

import java.util.ArrayList;
import java.util.List;

import agents.Eye;
import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import tools.SubPlot;
import ecosystem.*;

public class SuperPredator extends Animal{
	
	private PApplet parent;
	private SubPlot plt;
	private PImage img;

	protected SuperPredator(PVector pos, float mass, float radius, int color, PApplet parent, SubPlot plt, PImage img) {
		super(pos, mass, radius, color, parent, plt, img);
		this.parent = parent;
		this.plt = plt;
		this.img = img;
		energy = WorldConstants.INI_S_PREDATOR_ENERGY;
	}
	
	public SuperPredator(SuperPredator superPredator, Boolean mutate, PApplet parent, SubPlot plt, PImage img) {
		super(superPredator, mutate, parent, plt, "", img);
		this.parent = parent;
		this.plt = plt;
		this.img = img;
		energy = WorldConstants.INI_S_PREDATOR_ENERGY;
	}

	@Override
	public List<Animal> eat(List<Animal> animals) {
		PVector mePos = this.getPos().copy();
		for (Animal animal : animals) {
			if (animal instanceof Prey) {
				PVector preyPos = animal.getPos().copy();
				float distance = PVector.dist(mePos, preyPos);
				if (distance < this.radius*5) {
					energy += WorldConstants.ENERGY_FROM_PREY;
					animals.remove(animal);
					return animals;
				}
			}
			
			if (animal instanceof Predator) {
				PVector preyPos = animal.getPos().copy();
				float distance = PVector.dist(mePos, preyPos);
				if (distance < this.radius*3) {
					energy += WorldConstants.S_ENERGY_FROM_PREDATOR;
					animals.remove(animal);
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
		if(energy > WorldConstants.S_PREDATOR_ENERGY_TO_REPRODUCE) {
			energy -= WorldConstants.INI_S_PREDATOR_ENERGY;
			child = new SuperPredator(this, mutate, parent, plt, img);
			if (mutate) child.mutateBehaviors();
		}
		return child;
	}
	
	/*Procurar outro animal com base na visao*/
	public void updateTargetPrey(PApplet p, List<Body> obstacles, List<Animal> allPrey) {
		float dist = 0;
		if (!obstacles.contains(this.target) && (this.target != null)) {
			//distancia entre o animal e a prey
			dist = PVector.dist(this.getPos(), this.target.getPos());
			if (dist > this.getDNA().visionDistance) { 
				for (Animal animal : allPrey) {
					float newDist = PVector.dist(this.getPos(), animal.getPos());
					if (newDist < dist && this.target != animal && ( animal instanceof Prey || animal instanceof Predator)) {
						this.target = animal;
						if (animal instanceof Prey || animal instanceof Predator) {
							this.target = animal;
							//System.out.println("alvo alterado");
						}
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
