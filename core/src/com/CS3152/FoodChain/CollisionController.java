package com.CS3152.FoodChain;

import java.util.List;
import java.util.Random;
import java.util.Random;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.CS3152.FoodChain.GameMap.tileType;
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
	
<<<<<<< HEAD
=======
	/** Reference to the GameMap */
	public GameMap map;
	/** Reference to the canvas */
	public GameCanvas canvas;
>>>>>>> origin/ashton
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
	 * @param c The canvas
	 * @param h The hunter
	 * @param a The list of animals
	 * @param m The map
	 */
<<<<<<< HEAD
	public CollisionController(Hunter h, List<Animal> a) {
=======
	public CollisionController(GameCanvas c, Hunter h, List<Animal> a,GameMap m) {
>>>>>>> origin/ashton
		hunter = h;
		animals = a;
		map = m;
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
	
	/**
	 * Checks to see if it is possible to move, if not, then move player back. 
	 * Collision physics are modeled after first programming lab.
	 * 
	 * @param hunter the hunter
	 * 
	 * TODO have to decide how to handle multiple collisions and which collisions to process first. like animal or tiles
	 */
	private void moveIfPossible(Hunter hunter) {
		tmp.set(hunter.getPosition());
		tmp.add(hunter.getVX(), hunter.getVY());		
		canMove=true;
		//check player against animals
		for(Animal a : animals){
			normal.set(hunter.getxPos()-a.getxPos(), hunter.getyPos()-a.getyPos());
			distance = normal.len();
			normal.nor();
			if (distance<30){
				canMove=false;
				//have to play around with numbers to smooth collisions
				tmp.set(normal).scl((15 - distance) / 2);  // normal * (d1 - dist)/2
				hunter.setPosition(hunter.getPosition().sub(tmp));
			}
		}
		//check tiles surrounding player
		System.out.println(map.screenPosToTile(tmp.x,tmp.y));
		if (map.screenPosToTile(tmp.x,tmp.y).type!=(tileType.GRASS)){
			canMove=false;
			normal.set(hunter.getPosition().sub(tmp));
			distance = normal.len();
			normal.nor();
			tmp.set(normal).scl(-4);  // normal * (d1 - dist)/2
			hunter.setPosition(hunter.getPosition().sub(tmp));
		}

		
		if(canMove){
			hunter.setPosition(tmp);
		}
	}
	
	private void moveIfPossible(Animal animal) {
		
	}
	
	private void checkForCollision(Animal animal, Trap trap) {
		
	}
}
