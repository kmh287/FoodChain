package com.CS3152.FoodChain;

import java.util.List;
import java.util.Random;
import java.util.Random;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.CS3152.FoodChain.Tile.tileType;
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
 * As a major sub-controller, this class must have a reference to all the models.
 */

public class CollisionController implements ContactListener {
	//The game world
	private World world;
	/** All the objects in the world. */
	protected PooledList<SimplePhysicsObject> objects  = new PooledList<SimplePhysicsObject>();
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
	protected void addObject(SimplePhysicsObject obj) {
		objects.add(obj);
		obj.activatePhysics(world);
		obj.setBullet(true);
		//obj.getBody().setUserData(data.toString());
		if(obj instanceof Tile){
			setTraversableTiles((Tile)obj);
		}
		//System.out.println("body1"+obj.getBody().toString() +" "+  obj.isActive());
	}
	
	private void setTraversableTiles(Tile t){
		if(t.type == tileType.GRASS || t.type == tileType.DIRT ||
		   t.type == tileType.N_GRASS || t.type == tileType.NE_GRASS || 
		   t.type == tileType.E_GRASS || t.type == tileType.SE_GRASS ||
		   t.type == tileType.S_GRASS || t.type == tileType.SW_GRASS ||
		   t.type == tileType.W_GRASS || t.type == tileType.NW_GRASS){
			t.setActive(false);
		}
	}
	
	/**
	 * Returns the PooledList of BoxObjects that are in the collision controller.
	 * These objects are used to calculate collisions in the world.
	 * 
	 * @return PooledList of BoxObjects
	 */
	public PooledList<SimplePhysicsObject> getObjects() {
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
		actor.setLinearVelocity(controls[0].getAction().scl(actor.getMoveSpeed()));
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
		if (actor.getAlive()) {
			if (actor instanceof Owl) {
				//System.out.println("yo");
			}
			actor.setLinearVelocity(controls[index].getAction().scl(actor.getMoveSpeed() + 100*AIController.getPanicPercentage()));
			float angle = ((AIController)controls[index]).getAngle();
			actor.updateLOS(angle);
			//actor.setAngle(((AIController)controls[index]).getAngle());
			actor.setFacing(((AIController) controls[index]).getAction());
		}
	}
	
	private void moveOwl(Animal actor,int index) {
		if (actor.getAlive()) {
			//actor.setLinearVelocity(controls[index].getAction());
			//actor.updateLOS(angle);
			//actor.setAngle(((AIController)controls[index]).getAngle());
			actor.setFacing(((AIController) controls[index]).getAction());
		}
	}


	public void update() {
		//world.step(1/60f, 3, 3);
		
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
				if (o instanceof Owl) {
					//System.out.println ("instanceof"); 
					moveOwl((Owl) o, i); 
				}
				else {
					move((Animal)o,i);
				
				}
				i++;
			}
			//System.out.println(o.getPosition().toString());
		}
		world.step(1/100f, 3, 3);
		//checkTrapped();
	}
	

    public void postUpdate(float dt) {
    	for (SimplePhysicsObject o : objects) {
    		if (o.getBody().getUserData() instanceof Animal) {
    			Animal a = (Animal) o;
    			if (a.getTrapped() || !a.getAlive()) {
    				a.setAlive(false);
    				a.setActive(false);
    			}
    		}
    		if (o.getBody().getUserData() instanceof Hunter) {
    			Hunter h = (Hunter) o;
    			if (!h.getAlive()) {
    				h.setActive(false);
    			}
    		}
    		if (trapToRemove != null
    				&& o.getBody().getUserData() instanceof Trap) {
    			Trap t = (Trap) o;
    			if (t.getType() == trapToRemove) {
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
			if (trap.getOnMap()) {
				trap.setOnMap(false);
				trap.setInInventory(true);
			}
		}
		if (bd1 instanceof Trap && bd2 instanceof Hunter) {
			Trap trap = (Trap) bd1;
			if (trap.getOnMap()) {
				trap.setOnMap(false);
				trap.setInInventory(true);
			}
		}
		if (bd1 instanceof Animal && bd2 instanceof Trap) {
			Animal animal = (Animal) bd1;
			Trap trap = (Trap) bd2;

			if (trap.getOnMap() && trap.getType() == "REGULAR_TRAP"
					&& animal.getType() == Actor.actorType.PIG) {

				animal.setTrapped(true);
				trap.setOnMap(false);
				trapToRemove = "REGULAR_TRAP";
				trapToAdd = "SHEEP_TRAP";
				trapLocationToAdd = trap.getPosition();
				
			}
			else if (trap.getOnMap() && trap.getType() == "SHEEP_TRAP"
					&& animal.getType() == Actor.actorType.WOLF) {
				animal.setTrapped(true);
				trap.setOnMap(false);
				trapToRemove = "SHEEP_TRAP";
				trapToAdd = "WOLF_TRAP";
				trapLocationToAdd = trap.getPosition();
			}
		}
		if (bd1 instanceof Trap && bd2 instanceof Animal) {
			Animal animal = (Animal) bd2;
			Trap trap = (Trap) bd1;
			if (trap.getOnMap() && trap.getType() == "REGULAR_TRAP"
					&& animal.getType() == Actor.actorType.PIG) {
				animal.setTrapped(true);
				trap.setOnMap(false);
				trapToRemove = "REGULAR_TRAP";
				trapToAdd = "SHEEP_TRAP";
				trapLocationToAdd = trap.getPosition();
				
			}
			else if (trap.getOnMap() && trap.getType() == "SHEEP_TRAP"
					&& animal.getType() == Actor.actorType.WOLF) {
				animal.setTrapped(true);
				trap.setOnMap(false);
				trapToRemove = "SHEEP_TRAP";
				trapToAdd = "WOLF_TRAP";
				trapLocationToAdd = trap.getPosition();
			}
		}
		if (bd1 instanceof Animal && bd2 instanceof Animal) {
			Animal a1 = (Animal) bd1;
			Animal a2 = (Animal) bd2;
			
			if (a1.canEat(a2)) {
				a2.setAlive(false);
			}
			if (a2.canEat(a1)) {
				a1.setAlive(false);
			}
			
			//If both animals are of the same type
			//Animals need to find a new path as they may
			//be colliding walking in opposite directions
		}
		if (bd1 instanceof Hunter && bd2 instanceof Animal) {
			Animal a = (Animal) bd2;
			Hunter h = (Hunter) bd1;
			
			if (a.canEat(h)) {
				h.setAlive(false);
			}
		}
		if (bd1 instanceof Animal && bd2 instanceof Hunter) {
			Animal a = (Animal) bd1;
			Hunter h = (Hunter) bd2;
			
			if (a.canEat(h)) {
				h.setAlive(false);
			}
		}
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