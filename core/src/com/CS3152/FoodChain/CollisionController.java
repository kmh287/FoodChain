package com.CS3152.FoodChain;

import java.util.List;
import java.util.Random;
import java.util.Random;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

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
	private InputController[] controls;
	private Vector2 action;
	
	private String trapToRemove = null;
	private String trapToAdd = null;
	private Vector2 trapLocationToAdd;
	
	public CollisionController(){
		//no gravity for top down
		this.world = new World(new Vector2(0,0), false);
		world.setContactListener(this);
		this.tmp = new Vector2();
		trapLocationToAdd = new Vector2();
	}
	
	/**
	 * Add the object to the list of objects maintained in the CollisionController
	 * @param obj: the object to add
	 */
	//protected void addObject(BoxObject obj, Object data) {
	protected void addObject(BoxObject obj) {
		objects.add(obj);
		obj.activatePhysics(world);
		//obj.getBody().setUserData(data.toString());
		if(obj instanceof Tile){
			setGrassTileOff((Tile)obj);
		}
		//System.out.println("body1"+obj.getBody().toString() +" "+  obj.isActive());
	}
	
	private void setGrassTileOff(Tile t){
		if(t.type==Tile.tileType.GRASS){
			t.setActive(false);
		}
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
	private void move(Hunter actor) {
		actor.setLinearVelocity(controls[0].getAction());
		actor.setFacing(controls[0].getAction());
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

	private void move(Animal actor,int index) {
		actor.setLinearVelocity(controls[index].getAction());
		float angle = ((AIController)controls[index]).getAngle();
		actor.updateLOS(angle);
		//actor.setAngle(((AIController)controls[index]).getAngle());
		actor.setFacing(((AIController) controls[index]).getAction());
	}


	public void update() {
		world.step(1/60f, 3, 3);
		
		//Updates the animals' actions
		//i is the index of each animal AI in controls
		int i = 1;
		//System.out.println(objects.size());
		for(PhysicsObject o : objects) {
			
			if (o instanceof Hunter){
				move((Hunter)o);
			}
			//unsure about order of objects.
			else if (o instanceof Animal){
				move((Animal)o,i);
				i++;
			}
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
    		if (trapToRemove != null
    				&& o.getBody().getUserData() instanceof Trap) {
    			Trap t = (Trap) o;
    			if (t.getType() == trapToRemove) {
    				t.setOnMap(false);
    				t.setActive(false);
    				trapToRemove = null;
    			}
    		}
    		if (trapToAdd != null
    				&& o.getBody().getUserData() instanceof Trap) {
    			Trap t = (Trap) o;
    			if (t.getType() == trapToAdd) {
    				t.setPosition(trapLocationToAdd);
    				t.setOnMap(true);
    				trapToAdd = null;
    			}
    		}
    	}
    }

	public void setControls(InputController [] controls){
		this.controls=controls;
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
			Trap trap = (Trap) bd2;

			if (trap.getType() == "REGULAR_TRAP"
					&& animal.getType() == Actor.actorType.SHEEP) {
				animal.setTrapped(true);
				trapToRemove = "REGULAR_TRAP";
				trapToAdd = "SHEEP_TRAP";
				trapLocationToAdd = trap.getPosition();
				
			}
			else if (trap.getType() == "SHEEP_TRAP"
					&& animal.getType() == Actor.actorType.WOLF) {
				animal.setTrapped(true);
				trapToRemove = "SHEEP_TRAP";
				trapToAdd = "WOLF_TRAP";
				trapLocationToAdd = trap.getPosition();
			}
				
			//animal.setActive(false);
		}
		if (bd1 instanceof Trap && bd2 instanceof Animal) {
			Animal animal = (Animal) bd2;
			Trap trap = (Trap) bd1;
			if (trap.getOnMap()) {
				animal.setTrapped(true);
			}
			//animal.setActive(false);
		}
		//System.out.println("COLLISION");

		body1 = contact.getFixtureA().getBody();
		body2 = contact.getFixtureB().getBody();
		//System.out.println("body1"+body1.getUserData() +" "+  body1.isActive());
//		System.out.println(body1.getUserData());
//		System.out.println(body2.getUserData());
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
	
	public World getWorld() {
		return this.world;
	}
}