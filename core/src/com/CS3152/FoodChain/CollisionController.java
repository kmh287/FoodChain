package com.CS3152.FoodChain;

import java.util.List;
import java.util.Random;
import java.util.Random;

import com.CS3152.FoodChain.GameMap.tileType;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Fixture;
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

public class CollisionController implements ContactListener {
	//The game world
	private World world;
	/** All the objects in the world. */
	protected PooledList<BoxObject> objects  = new PooledList<BoxObject>();
	//Vector2 cache for calculations
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
	 * Returns the PooledList of BoxObjects that are in the collision controller.
	 * These objects are used to calculate collisions in the world.
	 * 
	 * @return PooledList of BoxObjects
	 */
	public PooledList<BoxObject> getObjects() {
		return objects;
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
		tmp.set(hunter.getPosition());
		tmp.add(hunter.getVX(), hunter.getVY());	
//		System.out.println("Y vel : "+hunter.getVY());
//		System.out.println("X vel : "+hunter.getVX());
//		System.out.println("Vel : "+hunter.getLinearVelocity());
//		System.out.println("Position : "+hunter.getPosition());
		hunter.setLinearVelocity(tmp);
		//System.out.println("vx:" + hunter.getVX() + "vy:" +hunter.getVY());
		//hunter.setCenter(tmp);
		//hunter.getBody().applyForce(tmp.scl(30),hunter.getBody().getPosition(),true);
		//System.out.println(hunter.getBody().getLinearVelocity());
	}
	
	private void move(Animal animal) {
		tmp.set(animal.getPosition());
		tmp.add(animal.getVX(), animal.getVY());
//		System.out.println("Y vel : "+animal.getVY());
//		System.out.println("X vel : "+animal.getVX());
//		System.out.println("Vel : "+animal.getLinearVelocity());
//		System.out.println("Position : "+animal.getPosition());
		animal.setLinearVelocity(tmp);
	}
	
	//Pass the object to the correct handler
	private void move(PhysicsObject o){
		if (o instanceof Hunter){
			move((Hunter) o);
		}
		else if (o instanceof Animal){
			if (!(((Animal) o).getTrapped())) {
				move((Animal) o);
			}
			else {
				boolean trapped = true;
			}
		}
	}

	public void update() {
		world.step(1/60f, 6, 2);
		for(PhysicsObject o : objects) {
			move(o);
			//System.out.println(o.getPosition().toString());
		}
		//world.step(1/60f, 6, 2);
		//checkTrapped();
	}
	
    public void postUpdate(float dt) {
    	for (BoxObject o : objects) {
    		if (o.getBody().getUserData() instanceof Animal) {
    			Animal a = (Animal) o;
    			if (a.getTrapped()) {
    				a.setActive(false);
    			}
    		}
    	}
    }
	
	@Override
	public void beginContact(Contact contact) {
		Fixture fix1 = contact.getFixtureA();
		Fixture fix2 = contact.getFixtureB();

		Body body1 = fix1.getBody();
		Body body2 = fix2.getBody();

		Object fd1 = fix1.getUserData();
		Object fd2 = fix2.getUserData();
		
		Object bd1 = body1.getUserData();
		Object bd2 = body2.getUserData();
		
		if (bd1 instanceof Hunter && bd2 instanceof Trap) {
			Trap trap = (Trap) bd2;
			trap.setOnMap(false);
			trap.setInInventory(true);
			//trap.setActive(false);
			//trap.setPosition(0.0f, 0.0f);
		}
		if (bd1 instanceof Trap && bd2 instanceof Hunter) {
			Trap trap = (Trap) bd1;
			trap.setOnMap(false);
			trap.setInInventory(true);
			//trap.setActive(false);
			//trap.setPosition(0.0f, 0.0f);
		}
		if (bd1 instanceof Animal && bd2 instanceof Trap) {
			Animal animal = (Animal) bd1;
			animal.setTrapped(true);
			//animal.setActive(false);
		}
		if (bd1 instanceof Trap && bd2 instanceof Animal) {
			Animal animal = (Animal) bd2;
			animal.setTrapped(true);
			//animal.setActive(false);
		}
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