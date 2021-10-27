package agents;

import ecosystem.Prey;

public class DNA {
	
	public float maxSpeed;
	public float maxForce;
	public float visionDistance;
	public float visionSafeDistance;
	public float visionAngle;
	public float deltaTPursuit;
	public float radiusArrive;
	public float KArrive;
	public float deltaTWander;
	public float radiusWander;
	public float deltaPhiWander;
	
	public DNA() {
		//Physics
		maxSpeed = random(0.3f, 1f);
		maxForce = random(4f, 7f);
		//Vision
		visionDistance = random(1.5f, 2.5f);
		visionSafeDistance = 0.25f * visionDistance;
		visionAngle = (float)Math.PI * 0.3f;
		//Pursuit
		deltaTPursuit = random(0.5f, 1f);
		//Arrive
		radiusArrive = random(3, 5);
		KArrive = 120;
		//Wander
		deltaTWander = random(0.3f, 0.6f);
		radiusWander = random(1f, 3f);
		deltaPhiWander = (float)Math.PI/8;
	}
	
	public DNA(DNA dna, boolean mutate, String str) {
		maxSpeed = dna.maxSpeed;
		maxForce = dna.maxForce;
		
		visionDistance = dna.visionDistance;
		visionSafeDistance = dna.visionSafeDistance;
		visionAngle = dna.visionAngle;
		
		deltaTPursuit = dna.deltaTPursuit;
		radiusArrive = dna.radiusArrive;
		
		deltaTWander = dna.deltaTWander;
		deltaPhiWander = dna.deltaPhiWander;
		radiusWander = dna.radiusWander;
		if (mutate) mutate(str);
	}
	
	private void mutate(String str) {
		if (str == "Prey") {
			maxSpeed += random(-0.1f, 0.1f);
			maxSpeed = Math.max(0, maxSpeed);
		}
		if (str == "Predator") {
			maxSpeed += random(-0.2f, 0.2f);
			maxSpeed = Math.max(0, maxSpeed);
		}
		if (str == "SuperPredator") {
			maxSpeed += random(-0.3f, 0.3f);
			maxSpeed = Math.max(0, maxSpeed);
		}
	}
	
	public static float random(float min, float max) {
		return (float) (min + (max - min)*Math.random());
	}

}
