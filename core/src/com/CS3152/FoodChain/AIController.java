package com.CS3152.FoodChain;

import java.util.*;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.World;

/**
 * InputController corresponding to AI control.
 * General controller for all animal AIs
 */
public abstract class AIController implements InputController {
    /**
     * Enumeration to encode the states of animals
     */
    protected static enum State {
        // Animal determines where to go next
        FIND,
        // Animal is patrolling
        PATROL,
        // Animal is chasing
        CHASE,
        // Animal is running away
        FLEE,
        // Animal kills target
        KILL,
        // Aniaml is dead
        DEAD
    }
    
    // Instance Attributes
    // The animal controlled by this AIController
    protected Animal animal;
    // The world
    protected World world;
    // The game board; used for pathfinding
    protected GameMap map;
    // The animal's current state
    protected State state;
    // Whether or not the animal should flee
    protected boolean scared;
    // The animal's target (assigned when an animal crosses
    // this animal's line of sight) (null if this animal is a sheep)
    protected Actor target;
    // The animal that this animal is fleeing
    protected Actor attacker;
    // All actors on the map
    protected List<Actor> actors;
    
    /* Although Vector2 stores floats, we are using their int values to specify the
     * tile the animal is on.
     */
    // The animal's goal tile
    protected Vector2 goal;
    // The animal's tile location
    protected Vector2 loc;
    
    // The animal's next move;
    protected Vector2 move;
    // Number of ticks since controller started
    protected int ticks;
    
    
    /*
     * Creates an AIController for the animal
     *
     * @param animal The Animal being controlled
     * @param map The game map
     */
    public AIController(Animal animal, GameMap map, List<Actor> actors) {
        this.animal = animal;
        this.map = map;
        this.actors = actors;
        
        this.loc = new Vector2(map.screenXToMap(animal.getX()),
                               map.screenYToMap(animal.getY()));
        
        this.state = State.PATROL;//FIND;
        this.goal = new Vector2();
        // To where it should start moving
        setGoal((int)getLoc().x - 4, (int)getLoc().y);
        this.move = new Vector2(100,0);
        this.ticks = 0;
        
        this.target = null;
        this.attacker = null;
    }
    
    /*
     * Updates the animal's tile location
     */
    public void updateLoc() {
        this.loc.set(map.screenXToMap(animal.getX()),
                     map.screenYToMap(animal.getY()));
    }
    
    /*
     * The animal's tile location
     *
     * @return tile location
     */
    public Vector2 getLoc() {
        return this.loc;
    }
    
    /*
     * Sets the goal to specified Vector2
     */
    public void setGoal(int x, int y) {
        goal.set(x,y);
    }
    /*
     * Gets the goal tile
     *
     * @return goal tile location
     */
    public Vector2 getGoal() {
        return this.goal;
    }
        
    /*
     * Returns the action selected by this InputController
     *
     * The returned int is a bit-vector of more than one possible input
     * option.
     *
     * This funciton tests the enivornment and uses the State choose the
     * next action of the ship.
     *
     * @return the action selected by this InputController
     */
    public Vector2 getAction() {
        // Increment the number of ticks.
        ticks++;
        
        if (ticks % 10 == 0 && state != State.DEAD) {
        	//comment out for fixing collisions
//            // Process the State
//            //changeStateIfApplicable();
//            
//            checkCone();
//            if (isScared()) {
//            	flee();
//            }
//            else if (hasTarget()) {
//            	chase();
//            }
//            else {
//            	//patrol();
//            }
//            
//            // Pathfinding
//            //markGoal();
//            move = getNextMoveToGoal();
        }
        
        //System.out.println(move);
        return move;
    }
    
    // Checks animal's line of sight to see if anything is there and responds
    // accordingly.
    public void checkCone() {
    	for (Actor a : actors) {
    		if (withinCone(a)) {
    			VisionCallback vcb = new VisionCallback(this);
    			world.rayCast(vcb, getAnimal().getPosition(), a.getPosition());
    		}
    	}
    }
    
    // Determines whether or not an actor is in the animal's line of sight
    //TODO
    public boolean withinCone(Actor a) {
    		//TODO
//    		throw new NotImplementedException();

    	return false;
    }
    
    // Determines whether or not the animal should run away
    public void flee() {
        // Go to the next tile farthest from attacker
        float fleex = getAnimal().getX() - attacker.getX();
        float fleey = getAnimal().getY() - attacker.getY();
        int attackTileX = map.screenXToMap(animal.getX());
        int attackTileY = map.screenYToMap(animal.getY());
        // Normalize distance to choose next tile
        fleex = fleex / (Math.abs(fleex));
        fleey = fleey / (Math.abs(fleey));
        // The best goal tile
        float tileX = getLoc().x + fleex;
        float tileY = getLoc().y + fleey;
        // Set best goal if it is safe. Otherwise choose tile farthest from attacker
        if (map.isSafeAt(tileX, tileY)) {
        	goal.set(tileX, tileY);
        	return;
        }
        // Find farthest valid tile from attacker
        float[] dists = new float[7];
        if (map.isSafeAt(getLoc().x + fleex, tileY)) {
        	dists[0] = Vector2.dst(getLoc().x + fleex, tileY, attackTileX, attackTileY);
        }
        if (map.isSafeAt(tileX, getLoc().y + fleey)) {
        	dists[1] = Vector2.dst(tileX, getLoc().y + fleey, attackTileX, attackTileY);
        }
        if (map.isSafeAt(getLoc().x - fleex, tileY)) {
        	dists[2] = Vector2.dst(getLoc().x - fleex, tileY, attackTileX, attackTileY);
        }
        if (map.isSafeAt(tileX, getLoc().y - fleey)) {
        	dists[3] = Vector2.dst(tileX, getLoc().y - fleey, attackTileX, attackTileY);
        }
        if (map.isSafeAt(getLoc().x - fleex, getLoc().y - fleey)) {
        	dists[4] = Vector2.dst(getLoc().x - fleex, getLoc().y - fleey,
					   			   attackTileX, attackTileY);
        }
        if (map.isSafeAt(getLoc().x - fleex, getLoc().y + fleey)) {
        	dists[5] = Vector2.dst(getLoc().x - fleex, getLoc().y + fleey,
		   			   attackTileX, attackTileY);
        }            
        if (map.isSafeAt(getLoc().x + fleex, getLoc().y - fleey)) {
        	dists[6] = Vector2.dst(getLoc().x + fleex, getLoc().y - fleey,
		   			   attackTileX, attackTileY);
        }
        // biggest distance
        int biggest = 0;
//        for (int x = 0; x < dists.length; x++) {
//        	if (dists[x] > biggest) {
//        		biggest = dists[x];
//        	}
//        }

        return;
    }
    
    public void chase() {
        // Go to the next tile closest to target
        float chasex = getAnimal().getX() - target.getX();
        float chasey = getAnimal().getY() - target.getY();
        // Normalize distance to choose next tile
        chasex = chasex / (Math.abs(chasex));
        chasey = chasey / (Math.abs(chasey));
        goal.set(getLoc().x - chasex, getLoc().y - chasey);
    }
    
    /*
     * The animal this AI controls
     *
     * @return the animal
     */
    public Animal getAnimal() {
        return this.animal;
    }
    
    /*
     * Changes the animal's state depending on what it's doing and what it's seen
     */
    public abstract void changeStateIfApplicable();
    
    /*
     * Marks the goal tile the animal is trying to go to
     */
    public abstract void markGoal();
    
    /*
     * Determines whether or not the animal sees a predator
     *
     * @return true if a predator is in the animal's line of sight. False otherwise
     */
    public abstract boolean seesPredator();
    
    /*
     * Sets the animal that this animal should flee from. Null if it shouldn't flee.
     */
    public void setAttacker(Actor ac) {
        this.attacker = ac;
    }
    
    /*
     * Sets the animal that this animal should chase. Null if it shouldn't chase.
     */
    public void setTarget(Actor ac) {
        this.target = ac;
    }
    
    public boolean hasTarget() {
    	return this.target != null;
    }
    
    /*
     * Determines whether the animal controlled by this controller is seen by a predator.
     *
     * @return true if it is in a predator's line of sight. false otherwise
     */
    public abstract boolean isSeenByPredator();
        
    /*
     * Determines whether the animal sees prey.
     *
     * @return true if an animal's prey is in the line of sight of the animal.
     * false otherwise.
     */
    public abstract boolean seesPrey();
    
    /*
     * Sets whether or not this animal should be scared. If ac is null, then
     * it should not be scared. Otherwise it should be.
     */
    public void setScared(Actor ac) {
    	if (ac != null) {
    		this.scared = true;
    		setAttacker(ac);
    		return;
    	}
    	else {
    		this.scared = false;
    		setAttacker(null);
    		return;
    	}	
    }
    
    /*
     * Returns whether or not this animal should run away
     * 
     * @return true if the animal saw a predator. False otherwise.
     */
    public boolean isScared() {
    	return this.scared;
    }
    
    /*
     * Gets the move that will get the animal to its goal the fastest
     *
     * @return int corresponding to InputController bit-vector
     */
    public int getNextMoveToGoal() {
    	return 0;
    	//System.out.println("goalx:" + goal.x + "goaly:" + goal.y);
    	//System.out.println("locx:" + getLoc().x + "locy:" + getLoc().y);
    	
//        if (goal.x - getLoc().x == 0 && goal.y - getLoc().y > 0) {
//            return NORTH;
//        }
//        else if (goal.x - getLoc().x > 0 && goal.y - getLoc().y > 0) {
//            return NORTHEAST;
//        }
//        else if (goal.x - getLoc().x > 0 && goal.y - getLoc().y == 0) {
//            return EAST;
//        }
//        else if (goal.x - getLoc().x > 0 && goal.y - getLoc().y < 0) {
//            return SOUTHEAST;
//        }
//        else if (goal.x - getLoc().x == 0 && goal.y - getLoc().y < 0) {
//            return SOUTH;
//        }
//        else if (goal.x - getLoc().x < 0 && goal.y - getLoc().y < 0) {
//            return SOUTHWEST;
//        }
//        else if (goal.x - getLoc().x < 0 && goal.y - getLoc().y == 0) {
//            return WEST;
//        }
//        else if (goal.x - getLoc().x < 0 && goal.y - getLoc().y > 0) {
//            return NORTHWEST;
//        }
//        else {
//            return NO_ACTION;
//        }
    }
    
    // Should not be here, but need to finish
    public Vector2 getClickPos() {return new Vector2();}
    
    public boolean isClicked(){return false;}
    
    public int getNum(){return 0;}
}







