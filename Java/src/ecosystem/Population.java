package ecosystem;

import java.util.ArrayList;
import java.util.List;
import agents.AvoidObstacle;
import agents.Eye;
import agents.Seek;
import agents.Wander;
import physics.Body;
import processing.core.PApplet;
import processing.core.PImage;
import processing.core.PVector;
import tools.SubPlot;

public class Population {
	
	private List<Animal> allPrey;
	private List<Animal> allPredator;
	private List<Animal> allSuperPredator;
	private List<String> preyBehaviors;
	private List<String> predatorBehaviors;
	private List<String> superPredatorBehaviors;
	private List<Body> obstacles;
	
	
	private double[] window;
	private boolean mutate = true;
	
	private float timer;
	private float targetUpdate = 5;
	
	private PImage imgPrey;
	private PImage imgPredator;
	private PImage imgSPredator;
	
	public Population(PApplet parent, SubPlot plt, Terrain terrain) {
		timer = 0;
		window = plt.getWindow();
		
		allPrey = new ArrayList<Animal>();
		allPredator = new ArrayList<Animal>();
		allSuperPredator = new ArrayList<Animal>();
		
		preyBehaviors = new ArrayList<String>();
		predatorBehaviors = new ArrayList<String>();
		superPredatorBehaviors = new ArrayList<String>();

		obstacles = terrain.getObstacles();
		
		imgPrey = parent.loadImage("img/prey.png");
		imgPrey.resize(15, 15);
		imgPredator = parent.loadImage("img/predator.png");
		imgSPredator = parent.loadImage("img/superPredator.png");
		
		//prey
		for(int i=0; i<WorldConstants.INI_PREY_POPULATION; i++) {
			PVector pos = new PVector(parent.random((float)window[0], (float)window[1]), 
						 			  parent.random((float)window[2], (float)window[3]));
		
			
			
			Animal a = new Prey(pos, WorldConstants.PREY_MASS, WorldConstants.PREY_SIZE, 0, parent, plt, imgPrey);
			a.addBehavior(new Wander(1));
			preyBehaviors.add(new String("Wander"));
			a.addBehavior(new AvoidObstacle(0));
			preyBehaviors.add("Avoid");
			allPrey.add(a);
		}
		
		//predator
		for(int i=0; i<WorldConstants.INI_PREDATOR_POPULATION; i++) {
			PVector pos = new PVector(parent.random((float)window[0], (float)window[1]), 
						 			  parent.random((float)window[2], (float)window[3]));
		
			
			
			Animal a = new Predator(pos, WorldConstants.PREDATOR_MASS, WorldConstants.PREDATOR_SIZE, 0, parent, plt, imgPredator);
			
			a.addBehavior(new AvoidObstacle(3));
			predatorBehaviors.add("Avoid");
			a.addBehavior(new Seek(1));
			predatorBehaviors.add("Seek");
			a.addBehavior(new Wander(1));
			predatorBehaviors.add("Wander");
			
			//definir um target inicial
			//allTrackingBodies e uma variavel local, cada Predador tem a sua lista
			//Dado que um predador procura uma nova presa e mais facil nao ter uma lista persistente
			List<Body> allTrackingBodies = new ArrayList<Body>();
			
			//target e uma variavel local pela mesma razao acima
			Prey target = (Prey) allPrey.get( (int) parent.random(0, allPrey.size()));
			allTrackingBodies.add(target);
			//e preciso guarda a informacao sobre a presa actual
			a.target = target;
			
			//adicionar os objectos tambem
			allTrackingBodies.addAll(obstacles);
			
			Eye eye = new Eye(a, allTrackingBodies);
			a.setEye(eye);

			allPredator.add(a);
		}
		
		for(int i=0; i<WorldConstants.INI_S_PREDATOR_POPULATION; i++) {
			PVector pos = new PVector(parent.random((float)window[0], (float)window[1]), 
						 			  parent.random((float)window[2], (float)window[3]));
			
			
		
			Animal a = new SuperPredator(pos, WorldConstants.S_PREDATOR_MASS, WorldConstants.S_PREDATOR_SIZE, 0, parent, plt, imgSPredator);
			
			a.addBehavior(new AvoidObstacle(5));
			superPredatorBehaviors.add("Avoid");
			a.addBehavior(new Seek(1));
			superPredatorBehaviors.add("Seek");
			a.addBehavior(new Wander(1));
			superPredatorBehaviors.add("Wander");
			
			
			List<Body> allTrackingBodies = new ArrayList<Body>();
			
			allTrackingBodies.addAll(allPrey);
			allTrackingBodies.addAll(allPredator);

			
			
			Animal target = (Animal) allTrackingBodies.get( (int) parent.random(0, allTrackingBodies.size()));
			allTrackingBodies.add(target);
			//e preciso guarda a informacao sobre a presa actual
			a.target = target;
			
			allTrackingBodies.addAll(obstacles);
			
			Eye eye = new Eye(a, allTrackingBodies);
			a.setEye(eye);

			allSuperPredator.add(a);
		}
		
		//apply do eye na presa no fim para incluir os predadores
		for (Animal animal : allPrey) {
			List<Body> allTrackingBodies = new ArrayList<Body>();
			allTrackingBodies.addAll(obstacles);
			allTrackingBodies.addAll(allPredator);
			allTrackingBodies.addAll(allSuperPredator);
			Eye eye = new Eye(animal, allTrackingBodies);
			animal.setEye(eye);
		}
}
	
	public void update(PApplet p, float dt, Terrain terrain) {
		timer += dt;
		die();
		move(terrain, dt);
		eat(terrain, allPrey);
		energy_consumption(dt, terrain);
		reproduce(mutate);
		timer = updateTargets(p, timer);
		//System.out.println(timer);
	}
	
	private float updateTargets(PApplet p, float timer) {
		List<Body> predObstacles = new ArrayList<Body>();
		predObstacles.addAll(obstacles);
		predObstacles.addAll(allSuperPredator);
		
		if (timer > targetUpdate) {
			//System.out.println("Updating");
			for (Animal animal : allPredator) 
				animal.updateTargetPrey(p, predObstacles, allPrey);
			
			List<Animal> allAnimalsAsPrey = new ArrayList<Animal>();
			allAnimalsAsPrey.addAll(allPrey);
			allAnimalsAsPrey.addAll(allPredator);
			for (Animal animal : allSuperPredator) 
				animal.updateTargetPrey(p, obstacles, allAnimalsAsPrey);
			
			timer = 0;
		}
		
		return timer;
	}
	
	private void move(Terrain terrain, float dt) {
		for(Animal animal : allPrey)
			animal.applyBehaviors(dt);
		
		for(Animal animal : allPredator) {
			animal.applyBehaviors(dt);
		}
		
		for(Animal animal : allSuperPredator) {
			animal.applyBehaviors(dt);
		}
	}
	
	private void eat(Terrain terrain, List<Animal> animals) {
		List<Animal> animalList = new ArrayList<Animal>();

		for(Animal a : allPrey) {
			a.eat(terrain);		
			animalList.add(a);
		}
		
		for(Animal a : allPredator) {
			animalList = a.eat(animalList);	
			animalList.add(a);
		}
			
		
		for(Animal a : allSuperPredator)
			animalList = a.eat(animalList);
		
		
		allPredator = new ArrayList<Animal>();
		allPrey = new ArrayList<Animal>();
		for (Animal animal : animalList) {
			if (animal instanceof Prey) 
				allPrey.add(animal);
			
			if (animal instanceof Predator) 
				allPredator.add(animal);
			
			
		}
	}
	
	private void energy_consumption(float dt, Terrain terrain) {
		for(Animal a : allPrey)
			a.energy_consumption(dt, terrain);
		
		for(Animal a : allPredator)
			a.energy_consumption(dt, terrain);
		
		for(Animal a : allSuperPredator)
			a.energy_consumption(dt, terrain);
	}
	
	private void die() {
		for(int i=allPrey.size()-1; i>=0; i--) {
			Animal a = allPrey.get(i);
			if(a.die()) {
				allPrey.remove(a);
			}
		}
		
		for(int i=allPredator.size()-1; i>=0; i--) {
			Animal a = allPredator.get(i);
			if(a.die()) {
				allPredator.remove(a);
			}
		}
		
		for(int i=allSuperPredator.size()-1; i>=0; i--) {
			Animal a = allSuperPredator.get(i);
			if(a.die()) {
				System.out.println("removido");
				allSuperPredator.remove(a);
			}
		}
		
	}
	
	private void reproduce(boolean mutate) {
		for(int i=allPrey.size()-1; i>=0; i--) {
			Animal a = allPrey.get(i);
			Animal child = a.reproduce(mutate);
			if(child != null)
				allPrey.add(child);
		}
		
		for(int i=allPredator.size()-1; i>=0; i--) {
			Animal a = allPredator.get(i);
			Animal child = a.reproduce(mutate);
			if(child != null)
				allPredator.add(child);
		}
		
		for(int i=allSuperPredator.size()-1; i>=0; i--) {
			Animal a = allSuperPredator.get(i);
			Animal child = a.reproduce(mutate);
			if(child != null)
				allSuperPredator.add(child);
		}
		
		List<Body> preyObstacles = new ArrayList<Body>();
		preyObstacles.addAll(obstacles);
		preyObstacles.addAll(allPredator);
		preyObstacles.addAll(allSuperPredator);
		
		for (Animal a : allPrey) {
			Eye e = new Eye(a, preyObstacles);
			a.setEye(e);
		}
		
		List<Body> predObstacles = new ArrayList<Body>();
		predObstacles.addAll(obstacles);
		predObstacles.addAll(allPredator);
		
		for (Animal a : allPredator) {
			Eye e = new Eye(a, predObstacles);
			a.setEye(e);
		}
	}
	
	public void display(PApplet p, SubPlot plt) {
		for(Animal a : allPrey)
			a.display(p, plt, imgPrey);
		
		for(Animal a : allPredator)
			a.display(p, plt, imgPredator);
		
		for(Animal a : allSuperPredator)
			a.display(p, plt, imgSPredator);
	}
	
	public int getNumPrey() {
		return allPrey.size();
	}
	
	public int getNumPredator() {
		return allPredator.size();
	}
	
	public int getNumSuperPredator() {
		// TODO Auto-generated method stub
		return allSuperPredator.size();
	}
	
	public List<String> getPreyBehaviors() {
		return preyBehaviors;
	}
	
	public List<String> getPredatorBehaviors() {
		return predatorBehaviors;
	}
	
	public List<String> getSuperPredatorBehaviors() {
		// TODO Auto-generated method stub
		return superPredatorBehaviors;
	}
	
	public float getMeanMaxSpeed(List<Animal> animals) {
		float sum = 0;
		for(Animal a: animals)
			sum += a.getDNA().maxSpeed;
		return sum/animals.size();
	}
	
	public float getStdMaxSpeed(List<Animal> animals) {
		float mean = getMeanMaxSpeed(animals);
		float sum = 0;
		for(Animal a: animals)
			sum += Math.pow(a.getDNA().maxSpeed - mean, 2);
		return (float)Math.sqrt(sum/animals.size());
	}
	
	//alterado
	public float[] getMeanWeights(List<Animal> animals) {
		float[] sums = new float[animals.get(0).getBehaviors().size()];//alterado
		for(Animal a: animals) {
			int nBehaviors = a.getBehaviors().size();
			for (int i = 0; i < nBehaviors; i++) {
				sums[i] += a.getBehaviors().get(i).getWeight();
			}
			
		}
		for (int i = 0; i < sums.length; i++) {
			sums[i] /= animals.size();
		}
		return sums;
	}
	
	public List<Animal> getAllPrey() {
		return allPrey;
	}
	
	public List<Animal> getAllPredator() {
		return allPredator;
	}

	public List<Animal> getAllSuperPredator() {
		// TODO Auto-generated method stub
		return allSuperPredator;
	}

	

	
		
}
