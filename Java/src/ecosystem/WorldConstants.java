package ecosystem;

import processing.core.PImage;

public class WorldConstants {
	
	//World
	public final static double[] WINDOW = {-10, 10, -10, 10};
	
	//Terrain
	public final static int NROWS = 45;
	public final static int NCOLS = 60;
	public static enum PatchType {EMPTY, OBSTACLE, FERTILE, FOOD};
	public final static double[] PATCH_TYPE_PROB = {0.15f, 0.15f, 0.2f, 0.5f};
	public final static int NSTATES = PatchType.values().length;
	public static int[][] TERRAIN_COLORS = {{200,200,60} , {200,200,200},  {168,107,51}, {40,200,20}};
	public final static float[] REGENERATION_TIME = {10.f, 20.f}; //seconds
	public static String[] TERRAIN_IMGS = {"img/vazio.png", "img/obstacle.png", "img/fertil.png", "img/food.png"};

		
	//Prey Population
	public final static float PREY_SIZE = .1f;
	public final static float PREY_MASS = 1f;
	public final static int INI_PREY_POPULATION = 25;
	public final static float INI_PREY_ENERGY = 10f;
	public final static float ENERGY_FROM_PLANT = 4f;
	public final static float PREY_ENERGY_TO_REPRODUCE = 60f;
	
	//Predator Population
	public final static float PREDATOR_SIZE = .12f;
	public final static float PREDATOR_MASS = 1.2f;
	public final static int INI_PREDATOR_POPULATION = 10;
	public final static float INI_PREDATOR_ENERGY = 60f;
	public final static float ENERGY_FROM_PREY = 15f;
	public final static float PREDATOR_ENERGY_TO_REPRODUCE = 80f;

	//Predator Population
	public final static float S_PREDATOR_SIZE = .20f;
	public final static float S_PREDATOR_MASS = 1.2f;
	public final static int INI_S_PREDATOR_POPULATION = 4;
	public final static float INI_S_PREDATOR_ENERGY = 60f;
	public final static float S_ENERGY_FROM_PREY = 5f;
	public final static float S_ENERGY_FROM_PREDATOR = 10f;
	public final static float S_PREDATOR_ENERGY_TO_REPRODUCE = 200f;


}
