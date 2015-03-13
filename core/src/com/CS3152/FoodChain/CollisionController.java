package com.CS3152.FoodChain;

import java.util.Random;
import java.util.List;
import java.util.Random;
import java.util.Random;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.CS3152.FoodChain.GameMap.tileType;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.badlogic.gdx.physics.box2d.World;

/**
 * Class to handle basic collisions in the game.
 *
 * This is the simplest of physics engines.  In later labs, we will see how to work 
 * with more interesting engines.
 *
 * As a major subcontroller, this class must have a reference to all the models.
 */

<<<<<<< HEAD
public class CollisionController {

	/** Reference to the canvas */
	public GameCanvas canvas;
	/** Reference to the hunter */
	public Hunter hunter;
	/** Reference to the animals */
	public List<Animal> animals;
	
=======
public class CollisionController implements ContactListener {
	//The game world
	private World world;
	/** All the objects in the world. */
	protected PooledList<BoxObject> objects  = new PooledList<BoxObject>();
	//Vector2 cache for calculations
>>>>>>> origin/ashton
	private Vector2 tmp;
	
	public CollisionController(){
		//no gravity for top down
		this.world = new World(new Vector2(0,0), false);
		world.setContactListener(this);
		this.tmp = new Vector2();
	}
	
	/**
	 * Add the object to the list of objects maintained in the CollisionController
	 * @param obj: the object to add
	 */
	protected void addObject(BoxObject obj) {
		objects.add(obj);
		obj.activatePhysics(world);
	}
	
	/**
	 * Checks to see if it is possible to move, if not, then move player back. 
	 * Collision physics are modeled after first programming lab.
	 * 
	 * @param hunter the hunter
	 * 
	 * TODO have to decide how to handle multiple collisions and which collisions to process first. like animal or tiles
	 */
	private void move(Hunter hunter) {
//		System.out.println("Y vel : "+hunter.getVY());
//		System.out.println("X vel : "+hunter.getVX());
//		System.out.println("Vel : "+hunter.getLinearVelocity());
//		System.out.println("Position : "+hunter.getPosition());
//		hunter.getBody().setLinearVelocity(hunter.getVX(), hunter.getVY());
		//System.out.println("vx:" + hunter.getVX() + "vy:" +hunter.getVY());
		//hunter.setCenter(tmp);
		//hunter.getBody().applyForce(tmp.scl(30),hunter.getBody().getPosition(),true);
		//System.out.println(hunter.getBody().getLinearVelocity());
	}
	
	private void move(Animal animal) {
//		tmp.set(animal.getPosition());
//		tmp.add(animal.getVX(), animal.getVY());
//		System.out.println("Y vel : "+animal.getVY());
//		System.out.println("X vel : "+animal.getVX());
//		System.out.println("Vel : "+animal.getLinearVelocity());
//		System.out.println("Position : "+animal.getPosition());
//		animal.getBody().setLinearVelocity(animal.getVX(), animal.getVY());
	}
	
	//Pass the object to the correct handler
	private void move(PhysicsObject o){
		if (o instanceof Hunter){
			move((Hunter)o);
		}
		else if (o instanceof Animal){
			move((Animal)o);
		}
	}

	public void update() {
		world.step(1/60f, 3, 3);
		for(PhysicsObject o : objects) {
			move(o);
			//System.out.println(o.getPosition().toString());
		}
		//checkTrapped();
	}
	
	
	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		System.out.println("COLLISION");
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	
}