package com.CS3152.FoodChain;

import java.util.Random;
import java.util.List;
import java.util.Random;
import java.util.Random;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.Body;
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

public class CollisionController implements ContactListener {
	//The game world
	private World world;
	/** All the objects in the world. */
	protected PooledList<BoxObject> objects  = new PooledList<BoxObject>();
	//Vector2 cache for calculations

	private Vector2 tmp;
	private InputController[] controls;
	private Vector2 action;
	
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
	protected void addObject(BoxObject obj, Object data) {
		objects.add(obj);
		obj.activatePhysics(world);
		obj.getBody().setUserData(data.toString());
		if(obj instanceof Tile){
			setGrassTileOff((Tile)obj);
		}
		System.out.println("body1"+obj.getBody().toString() +" "+  obj.isActive());
	}
	
	private void setGrassTileOff(Tile t){
		if(t.type==Tile.tileType.GRASS){
			t.setActive(false);
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
	private void move(Hunter actor) {
//		float mult = 10f;
		tmp.set(controls[0].getAction());
//		tmp.scl(mult, mult);
		System.out.println(tmp.toString());
		actor.setLinearVelocity(tmp);
	}
	
	private void move(Animal actor,int index) {
		actor.setLinearVelocity(controls[index].getAction());
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
		//checkTrapped();
	}
	
	public void setControls(InputController [] controls){
		this.controls=controls;
	}
	
	
	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
		System.out.println("COLLISION");
		Body body1 = contact.getFixtureA().getBody();
		Body body2 = contact.getFixtureB().getBody();
		System.out.println("body1"+body1.getUserData() +" "+  body1.isActive());
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
	
	public PooledList<BoxObject> getObjects(){
		return objects;
	}
	
}