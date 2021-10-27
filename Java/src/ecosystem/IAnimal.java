package ecosystem;

import java.util.List;

import physics.Body;
import processing.core.PApplet;

public interface IAnimal {
	public Animal reproduce(boolean mutate);
	public void eat(Terrain terrain);
	public List<Animal> eat(List<Animal> animals);
	public void energy_consumption(float dt, Terrain terrain);
	public boolean die();
	public void updateTargetPrey(PApplet p, List<Body> obstacles, List<Animal> allPrey);
}
