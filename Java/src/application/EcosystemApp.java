package application;

import ecosystem.Population;
import ecosystem.Terrain;
import ecosystem.WorldConstants;
import processing.core.PApplet;
import tools.IProcessingApp;
import tools.SubPlot;
import tools.TimeGraph;

public class EcosystemApp implements IProcessingApp {

	private float timeDuration = 120;
	private float refPopulation = 200f;
	
	private float[] viewEcosystem = {0f, 0f, 1f, 0.76f};
	private float[] viewGraphPop = {0f, 0.77f, 1f, 0.23f};
	
	private double[] winGraphPop = {0, timeDuration, 0, 2*refPopulation};

	private SubPlot plt, pltGraphPop;
	private TimeGraph tgPreyPop, tgPredPop, tgSPredPop;

	
	private Terrain terrain;
	private Population population;
	private float timer, updateGraphTime;
	private float intervalUpdate = 1;

	@Override
	public void setup(PApplet p) {
		plt = new SubPlot(WorldConstants.WINDOW, viewEcosystem, p.width, p.height);
		
		p.pushStyle();
		double[] ww = plt.getWindow();
		p.color(0);
		p.noStroke();
		p.fill(255);
		p.rect((float) ww[0], (float) ww[1], p.width+10, p.height);
		p.popStyle();
		
		pltGraphPop = new SubPlot(winGraphPop, viewGraphPop, p.width, p.height);
		tgPreyPop = new TimeGraph(p, pltGraphPop, p.color(255,0,0), refPopulation);
		tgPredPop = new TimeGraph(p, pltGraphPop, p.color(22,165,62), refPopulation);
		tgSPredPop = new TimeGraph(p, pltGraphPop, p.color(0,0,255), refPopulation);

		
		terrain = new Terrain(p, plt);
		terrain.setStateColors(getColors(p));
		terrain.initRandomCustom(WorldConstants.PATCH_TYPE_PROB);
		for(int i=0; i<3; i++) terrain.majorityRule();
		
		population = new Population(p, plt, terrain);
		
		timer = 0;
		updateGraphTime = timer + intervalUpdate;
		
		p.pushStyle();
		p.noStroke();
		p.fill(255);
		p.rect(20, 2, 200, 50);
		p.textSize(12);
		p.fill(255,0,0);
		p.text(String.format("Prey Population: " + population.getNumPrey() , tgPreyPop.getRef()), 20, 12);
		p.fill(22,165,62);
		p.text(String.format("Predator Population: " + population.getNumPredator() , tgPredPop.getRef()), 20, 24);
		p.fill(0,0,255);
		p.text(String.format("Super Predator Population: " + population.getNumSuperPredator() , tgSPredPop.getRef()), 20, 36);
		p.popStyle();
		
		
		
	}

	@Override
	public void draw(PApplet p, float dt) {		
		timer += dt;
		
		terrain.regenerate();
		population.update(p, dt, terrain);
		
		terrain.display(p);
		population.display(p, plt);
		
		if(timer > updateGraphTime) {
			
//			System.out.println(String.format("Time = %ds", (int)timer));
//			System.out.println();
//			System.out.println("Prey pop. = " + population.getNumPrey());
//			System.out.println("Predator pop. = " + population.getNumPredator());
//			System.out.println("Super Predator pop. = " + population.getNumSuperPredator());
//
//			System.out.println();
//			System.out.println("Prey Stats");
//			if (population.getNumPrey() > 0) {
//				System.out.println("MeanMaxSpeed = " + population.getMeanMaxSpeed(population.getAllPrey()));
//				System.out.println("StdMaxSpeed = " + population.getStdMaxSpeed(population.getAllPrey()));
//				int nPreyBehaviors = population.getMeanWeights(population.getAllPrey()).length;
//				for (int i = 0; i < nPreyBehaviors; i++) {
//					System.out.println("Mean Weight " + population.getPreyBehaviors().get(i) 
//							            + " = " + population.getMeanWeights(population.getAllPrey())[i]);
//				}
//			}
//			System.out.println();
//			System.out.println("Predator Stats:");
//			if (population.getNumPredator() > 0) {
//				System.out.println("MeanMaxSpeed = " + population.getMeanMaxSpeed(population.getAllPredator()));
//				System.out.println("StdMaxSpeed = " + population.getStdMaxSpeed(population.getAllPredator()));
//				int nPredatorBehaviors = population.getMeanWeights(population.getAllPredator()).length;
//				for (int i = 0; i < nPredatorBehaviors; i++) {
//					System.out.println("Mean Weight " + population.getPredatorBehaviors().get(i) 
//							            + " = " + population.getMeanWeights(population.getAllPredator())[i]);
//				}
//			}
//			System.out.println();
//			System.out.println("Super Predator Stats:");
//			if (population.getNumSuperPredator() > 0) {
//				System.out.println("MeanMaxSpeed = " + population.getMeanMaxSpeed(population.getAllSuperPredator()));
//				System.out.println("StdMaxSpeed = " + population.getStdMaxSpeed(population.getAllSuperPredator()));
//				int nPredatorBehaviors = population.getMeanWeights(population.getAllSuperPredator()).length;
//				for (int i = 0; i < nPredatorBehaviors; i++) {
//					System.out.println("Mean Weight " + population.getSuperPredatorBehaviors().get(i) 
//							            + " = " + population.getMeanWeights(population.getAllSuperPredator())[i]);
//				}
//			}
//			System.out.println("");
			updateGraphTime = timer + intervalUpdate;
			
			p.pushStyle();
			p.noStroke();
			p.fill(255);
			p.rect(20, 2, 200, 50);
			p.textSize(12);
			p.fill(255,0,0);
			p.text(String.format("Prey Population: " + population.getNumPrey() , tgPreyPop.getRef()), 20, 12);
			p.fill(22,165,62);
			p.text(String.format("Predator Population: " + population.getNumPredator() , tgPredPop.getRef()), 20, 24);
			p.fill(0,0,255);
			p.text(String.format("Super Predator Population: " + population.getNumSuperPredator() , tgSPredPop.getRef()), 20, 36);
			p.popStyle();
		}
		
		
		
		tgPreyPop.plot(timer, population.getNumPrey());
		tgPredPop.plot(timer, population.getNumPredator());
		tgSPredPop.plot(timer, population.getNumSuperPredator());
		
		
	


	}

	@Override
	public void mousePressed(PApplet p) {
		p.pushStyle();
		p.noStroke();
		p.fill(255);
		p.rect(20, 2, 200, 50);
		p.textSize(12);
		p.fill(255,0,0);
		p.text(String.format("Prey Population: " + population.getNumPrey() , tgPreyPop.getRef()), 20, 12);
		p.fill(22,165,62);
		p.text(String.format("Predator Population: " + population.getNumPredator() , tgPredPop.getRef()), 20, 24);
		p.fill(0,0,255);
		p.text(String.format("Super Predator Population: " + population.getNumSuperPredator() , tgSPredPop.getRef()), 20, 36);
		p.popStyle();
		
		
		winGraphPop[0] = timer;
		winGraphPop[1] = timer + timeDuration;
		winGraphPop[3] = 2*population.getNumPrey();
		pltGraphPop = new SubPlot(winGraphPop, viewGraphPop, p.width, p.height);
		tgSPredPop = new TimeGraph(p, pltGraphPop, p.color(0,0,255), population.getNumPrey());
		tgPredPop = new TimeGraph(p, pltGraphPop, p.color(0,255,0), population.getNumPrey());
		tgPreyPop = new TimeGraph(p, pltGraphPop, p.color(255,0,0), population.getNumPrey());


					
	}

	@Override
	public void mouseReleased(PApplet p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseDragged(PApplet p) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyPressed(PApplet p) {
		// TODO Auto-generated method stub
		
	}
	
	private int[] getColors(PApplet p) {
		int[] colors = new int[WorldConstants.NSTATES];
		for(int i=0; i<WorldConstants.NSTATES; i++) {
			colors[i] = p.color(WorldConstants.TERRAIN_COLORS[i][0], WorldConstants.TERRAIN_COLORS[i][1], WorldConstants.TERRAIN_COLORS[i][2]);
		}
		return colors;
	}
}
