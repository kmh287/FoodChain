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
	
	/** Reference to the GameMap */
	public GameMap map;
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
	 * @param c The canvas
	 * @param h The hunter
	 * @param a The list of animals
	 * @param m The map
	 */
	public CollisionController(GameCanvas c, Hunter h, List<Animal> a,GameMap m) {
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
		tmp.set(hunter.getCenter());
		tmp.add(hunter.getVX(), hunter.getVY());		
		canMove=true;
		//check player against animals
		for(Animal a : animals){
			normal.set(hunter.getCenter().sub(a.getCenter()));
			distance = normal.len();
			normal.nor();
			if (distance<20){
				canMove=false;
				tmp.set(normal).scl((hunter.getXDiamter()-distance)/2);
				//have to play around with numbers to smooth collisions
				hunter.setCenter(hunter.getCenter().add(tmp));
			}
		}
		tmp.set(hunter.getCenter());
		tmp.add(hunter.getVX(), hunter.getVY());
		//check tiles surrounding player
//		System.out.println(map.screenPosToTile(tmp.x,tmp.y));
		if (map.screenPosToTile(tmp.x,tmp.y).type!=(tileType.GRASS)){
			canMove=false;
			normal.set(hunter.getCenter().sub(tmp));
			distance = normal.len();
			normal.nor();
			tmp.set(normal).scl(-4);
			hunter.setCenter(hunter.getCenter().sub(tmp));
		}
		if(canMove){
			hunter.setCenter(tmp);
		}
	}
	
	private void moveIfPossible(Animal animal) {
		tmp.set(animal.getCenter());
		tmp.add(animal.getVX(), animal.getVY());
		//System.out.println("vx: " + animal.getVX() + " vy: " + animal.getVY());
		animal.setCenter(tmp);
	}
}
