package com.CS3152.FoodChain;

import java.util.List;
import java.util.Random;
import java.util.Random;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.CS3152.FoodChain.Tile.tileType;
import com.badlogic.gdx.audio.Sound;
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
	
	private Trap trapToRemove = null;
	private String trapToAdd = null;
	private Vector2 trapLocationToAdd;
	private Animal animalToTrap = null;
	private float animalToTrapAngle = 0;
	
	private Trap trapOver = null;
	
	private Sound sound;
	/** The associated sound cue (if ship is making a sound). */
	private long sndcue;
	
	public CollisionController(){
		//no gravity for top down
		this.world = new World(new Vector2(0,0), false);
		world.setContactListener(this);
		this.tmp = new Vector2();
		trapLocationToAdd = new Vector2();
		
		sound = null;
		sndcue = -1;
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

	public void update() {
		world.step(1/100f, 3, 3);
		//checkTrapped();
		
		//shit
		if (trapOver!= null && trapOver.getisOver() && trapOver.getOnMap()) {
			if (trapOver.getSetWell() && controls[0].isSpaceHeldDown() && trapOver.getisOver()){
				trapOver.setOnMap(false);
				trapOver.setInInventory(true);
			}
			else{
				trapOver.setSetWell(true);
			}
		}
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
    			if (t == trapToRemove) {
    				t.setActive(false);
    				trapToRemove = null;
    			}
    		}
    		if (trapToAdd != null
    				&& o.getBody().getUserData() instanceof Trap) {
    			Trap t = (Trap) o;
    			if (t.getType() == trapToAdd && t.getInInventory()!=true) {
    				t.setPosition(trapLocationToAdd);
    				t.setOnMap(true);
    				//t.setInInventory(true);
    				trapToAdd = null;
    			}
    		}
    		if(animalToTrap!=null){
    			animalToTrap.setPosition(trapLocationToAdd);
    			animalToTrap.setAngle((float)(animalToTrapAngle-1.9f));
    			animalToTrap=null;
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
		
		//System.out.println("Collision!!!");
		
		Object bd1 = body1.getUserData();
		Object bd2 = body2.getUserData();
		
//		System.out.println(bd1.toString());
//		System.out.println(bd2.toString());
		
//		if (bd1 instanceof Hunter && bd2 instanceof Hunter){
//			System.out.println("uh oh");
//		}
		
		if (bd1 instanceof Hunter && bd2 instanceof Trap) {
			Trap trap = (Trap) bd2;
			if (trap.getOnMap() ){
				trapOver = trap;
				trap.setisOver(true);
			}
			if (trap.getOnMap() && controls[0].isSpaceHeldDown()) {
				trap.setOnMap(false);
				trap.setInInventory(true);
			}
		}
		if (bd1 instanceof Trap && bd2 instanceof Hunter) {
			Trap trap = (Trap) bd1;
			if (trap.getOnMap()) {
				trap.setisOver(true);
				trapOver = trap;
				if (trap.getSetWell() && controls[0].isSpaceHeldDown()){
					trap.setOnMap(false);
					trap.setInInventory(true);
				}
				else{
					trap.setSetWell(true);
				}
			}
		}
		if (bd1 instanceof Animal && bd2 instanceof Trap) {
			Animal animal = (Animal) bd1;
			Trap trap = (Trap) bd2;

			if (trap.getOnMap() && trap.getType() == "REGULAR_TRAP"
					&& animal.getType() == Actor.actorType.PIG) {
				animal.setTrapped(true);
				animalToTrap = animal;
				animalToTrapAngle= animal.getAngle();
				trap.setOnMap(false);
				trapToRemove = trap;
				trapToAdd = "SHEEP_TRAP";
				trapLocationToAdd = trap.getPosition();
				
			}
			else if (trap.getOnMap() && trap.getType() == "SHEEP_TRAP"
					&& animal.getType() == Actor.actorType.WOLF) {
				animalToTrap = animal;
				animalToTrapAngle= animal.getAngle();
				animal.setTrapped(true);
				trap.setOnMap(false);
				trapToRemove = trap;
				trapToAdd = "WOLF_TRAP";
				trapLocationToAdd = trap.getPosition();
			}
		}
		if (bd1 instanceof Trap && bd2 instanceof Animal) {
			Animal animal = (Animal) bd2;
			Trap trap = (Trap) bd1;
			
			if (trap.getOnMap() && trap.getType() == "REGULAR_TRAP"
					&& animal.getType() == Actor.actorType.PIG) {
				animalToTrap = animal;
				animalToTrapAngle= animal.getAngle();
				animal.setTrapped(true);
				trap.setOnMap(false);
				trapToRemove = trap;
				trapToAdd = "SHEEP_TRAP";
				trapLocationToAdd = trap.getPosition();
				
			}
			else if (trap.getOnMap() && trap.getType() == "SHEEP_TRAP"
					&& animal.getType() == Actor.actorType.WOLF) {
				animal.setTrapped(true);
				animalToTrap = animal;
				animalToTrapAngle= animal.getAngle();
				trap.setOnMap(false);
				trapToRemove = trap;
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
				play(SoundController.HUNTER_DEAD_SOUND);
			}
		}
	}

	@Override
	public void endContact(Contact contact) {
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
			trap.setisOver(false);
		}
		if (bd1 instanceof Trap && bd2 instanceof Hunter) {

			Trap trap = (Trap) bd1;
			trap.setisOver(false);
		}
		
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
		
	}
	
	 public void play(String sound) {
			if (sndcue != -1) {
				this.sound.stop(sndcue);
			}
			this.sound = SoundController.get(sound);
			sndcue = this.sound.play(); 
		}
	
	public World getWorld() {
		return this.world;
	}
}