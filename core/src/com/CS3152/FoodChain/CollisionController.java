package com.CS3152.FoodChain;

import java.util.Random;
import java.util.List;
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
		hunter.setPosition(tmp);
	}
	
	private void moveIfPossible(Animal animal) {
		
	}
}
