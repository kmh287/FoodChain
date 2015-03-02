package com.CS3152.FoodChain;

import java.util.List;
import java.util.Random;

import java.util.Random;
import com.badlogic.gdx.math.*;

/**
 * Class to handle basic collisions in the game.
 *
 * This is the simplest of physics engines.  In later labs, we will see how to work 
 * with more interesting engines.
 *
 * As a major subcontroller, this class must have a reference to all the models.
 */

public class CollisionController {
	
	/** Reference to the canvas */
	public GameCanvas canvas;
	/** Reference to the hunter */
	public Hunter hunter;
	/** Reference to the animals */
	public List<Animal> animals;
	
	private Vector2 tmp;
	
	/** Caching object for computing normal */
	private Vector2 normal;

	/** Caching object for computing net velocity */
	private Vector2 velocity;
	
	private float distance;
	
	private boolean canMove;
	/**
	 * Creates a CollisionController for the given models.
	 * 
	 * @param h The hunter
	 * @param a The list of animals
	 */
	public CollisionController(GameCanvas c, Hunter h, List<Animal> a) {
		hunter = h;
		animals = a;
		
		tmp = new Vector2();
		normal= new Vector2();
		velocity = new Vector2();
	}
	
	public void update() {
		moveIfPossible(hunter);
		for(Animal a : animals) {
			moveIfPossible(a);
		}
	}
	
	private void moveIfPossible(Hunter hunter) {
		tmp.set(hunter.getxPos(), hunter.getyPos());
		tmp.add(hunter.getVX(), hunter.getVY());		
		canMove=true;
		for(Animal a : animals){
			normal.set(hunter.getxPos()-a.getxPos(), hunter.getyPos()-a.getyPos());
			distance = normal.len();
			normal.nor();
			if (distance<30){
				canMove=false;
				tmp.set(normal).scl((1 - distance) / 2);  // normal * (d1 - dist)/2
				hunter.setPosition(hunter.getPosition().sub(tmp));
			}
		}
		if(canMove){
			hunter.setPosition(tmp);
		}
	}
	
	private void moveIfPossible(Animal animal) {
		
	}
}
