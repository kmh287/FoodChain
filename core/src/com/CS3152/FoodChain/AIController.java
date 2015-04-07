package com.CS3152.FoodChain;

import java.util.*;	

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.math.*;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;

/**
 * InputController corresponding to AI control.
 * General controller for all animal AIs
 */
public class AIController implements InputController {
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
    
    protected VisionCallback vcb;
    //protected FleeCallback fcb;
    
    // The vector position of the animal's goal
    protected Vector2 goal;
    // tmp helper for subtracting from goal
    protected Vector2 tmp;
    // The animal's tile location
    protected Vector2 loc;
    // The shortest distance to run to in a situation where the animal can't run
    // directly away
    protected Vector2[] distVctrs;
    
    // How many more turns (1 turn = 10 frames) before the animal can stop running
    protected int turns;
    
    // The animal's next move; a ControlCode
    protected Vector2 move;

    // Number of ticks since controller started
    protected int ticks;
    
    // Which direction to patrol
    protected int patrolTurn;
    
    // The animal's patrol path
    protected Array<Vector2> patrolPath;
    
    // The random number generator used by the AI
    private Random random;

    
    /*
     * Creates an AIController for the animal
     *
     * @param animal The Animal being controlled
     * @param map The game map
     */
    public AIController(Animal animal, World world, GameMap map, List<Actor> actors) {
        this.animal = animal;
        this.world = world;
        this.map = map;
        this.actors = actors;
        
        this.distVctrs = new Vector2[8];
        for (int id = 0; id < distVctrs.length; id++) {
        	distVctrs[id] = new Vector2();
        }
        
        this.loc = new Vector2(map.screenXToMap(animal.getX()),
                               map.screenYToMap(animal.getY()));
        
        this.target = null;
        this.attacker = null;
        
        this.vcb = new VisionCallback(this);
        //fcb = new FleeCallback(this, attacker);
        
        //this.state = State.PATROL;//FIND;
        this.goal = new Vector2();
        // To where it should start moving
        this.move = InputController.WEST;
        goal.set (getAnimal().getX() + 1, getAnimal().getY());

        this.turns = 10;//should be 0 in future
        
        this.patrolTurn = 0;
        
        this.tmp = new Vector2();
        
        this.random = new Random();
    }
    
    /*
     * Updates the animal's tile location
     */
    public void updateLoc() {
        this.loc.set(map.screenXToMap(animal.getX()),
                     map.screenYToMap(animal.getY()));
    }
    
    // Determine the new angle the animal wants to face
    // @return that angle.
    public float getAngle() {
    	// Subtracting the animal's current position from the goal it wants to be at
    	tmp.set(goal);
    	return (tmp.sub(getAnimal().getPosition())).angleRad();
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
        //ticks++;
        
        //if (ticks % 10 == 0 && state != State.DEAD) {
        //comment out for fixing collisions
    	// Process the State
    	//changeStateIfApplicable();
    	//checkCone();
        // RayCasting
        //Should be at beginning
    	if (getTarget() instanceof Animal) {
    		Animal target = (Animal) getTarget();
    		if (!target.getAlive()) {
    			setTarget(null);
    		}
    	}
    	if (getTarget() instanceof Hunter) {
    		Hunter target = (Hunter) getTarget();
    		if (!target.getAlive()) {
    			setTarget(null);
    		}
    	}
    	for (Actor a : actors) {
    		if (a != getAnimal()) {
    			world.rayCast(vcb, getAnimal().getPosition(), a.getPosition());
    			Fixture fix = vcb.getFixture();
    			if (fix != null) {
        			Object objSeen = fix.getBody().getUserData();
        			if (objSeen instanceof Actor) {
        				if (((Actor)objSeen).canKill(getAnimal())) {
        					setScared((Actor)objSeen);
        					setTurns();
        					setTarget(null);
        				}
        				if (getAnimal().canKill((Actor)objSeen)) {
        					setScared(null);
        					setTurns();
        					setTarget((Actor)objSeen);
        				}
        			}
        			else if (objSeen instanceof Tile) {
        				if (turns > 0) {
        					turns--;
        				}
        			}
        			else {
        				if (turns > 0) {
        					turns--;
        				}
        			}
    			}
    			if (canSettle()) { 
			    	setScared(null);
					setTarget(null);
			    }
    			//vcb.getFixture();
        	}
    	}
        	
        if (isScared()) {
        	//System.out.println(animal.getType() + ": flee");
        	flee();
        }
        else if (hasTarget()) {
        	//System.out.println(animal.getType() + ": chase");
        	chase();
        }
        else {
        	//System.out.println(animal.getType() + ": patrol");
        	patrol();
        }

        move = getNextMoveToGoal();
        
        //System.out.println(move);
        return move;
    }
    
    // Determines whether or not an actor is in the animal's line of sight
    public static boolean withinCone(Animal an, Vector2 meToActor) {
    	return isClockwise(an.getLeftSectorLine(), meToActor) &&
    		   !isClockwise(an.getRightSectorLine(), meToActor) &&
    		   withinRadius(an, meToActor);
    }
    
    /* Determines if meToActor is clockwise to sectorLine.
     * The function computes the dot product between the tangent of sectorLine and
     * meToActor. If the product is negative, then meToActor is clockwise.
     * 
     * @param sectorLine the line that we are measuring with respect to
     * @param meToActor the line we are testing to be clockwise
     * 
     * @return true if meToActor is clockwise to sectorLine. False otherwise.
     */
    public static boolean isClockwise(Vector2 sectorLine, Vector2 meToActor) {
    	return 0 >= -sectorLine.y * meToActor.x + sectorLine.x * meToActor.y;
    }
    
    /* Determines if length is within the length of vision sector
     * 
     * @param the vector we are testing
     * 
     * @return true if length is at most as long as one of the vision sector lines.
     */
    public static boolean withinRadius(Animal an, Vector2 length) {
    	return length.len() <= an.getSightLength();

    }
    
    // Determines whether or not the animal should run away
    public void flee() {

    	/*world.rayCast(fcb, attacker.getPosition(), getAnimal().getPosition());
    	if (fcb.getContact() != getAnimal()) {
    		turns--;
    	}*/

        // Animal's position
    	float anX = getAnimal().getX();
    	float anY = getAnimal().getY();
    	// Attacker's position
    	float attackX = attacker.getX();
        float attackY = attacker.getY();
        // Animal's best option

        Vector2 norm = getAnimal().getPosition().sub(attacker.getPosition()).nor();
        if (map.isSafeAt(anX + 200*norm.x, anY + 200*norm.y)) {
        	goal.set(anX + 200*norm.x, anY + 200*norm.y);

        	return;
        }
        // Find farthest valid tile from attacker
        if (map.isSafeAt(anX - 50, anY + 50)) {
        	distVctrs[0].x = anX - 50;
        	distVctrs[0].y = anY + 50;
        }
        if (map.isSafeAt(anX, anY + 50)) {
        	distVctrs[1].x = anX;
        	distVctrs[1].y = anY + 50;
        }
        if (map.isSafeAt(anX + 50, anY + 50)) {
        	distVctrs[2].x = anX + 50;
        	distVctrs[2].y = anY + 50;
        }
        if (map.isSafeAt(anX - 50, anY)) {
        	distVctrs[3].x = anX - 50;
        	distVctrs[3].y = anY;
        }
        if (map.isSafeAt(anX + 50, anY)) {
        	distVctrs[4].x = anX + 50;
        	distVctrs[4].y = anY;
        }
        if (map.isSafeAt(anX - 50, anY - 50)) {
        	distVctrs[5].x = anX - 50;
		   	distVctrs[5].y = anY - 50;
	    }            
	    if (map.isSafeAt(anX, anY - 50)) {
	    	distVctrs[6].x = anX;
		   	distVctrs[6].y = anY - 50;
	    }
	    if (map.isSafeAt(anX + 50, anY - 50)) {
	    	distVctrs[7].x = anX + 50;
		   	distVctrs[7].y = anY - 50;
        }
        // biggest distance

        float biggest = 0;
        int bigIndex = 0;
        for (int index = 0; index < distVctrs.length; index++) {
        	float distance = Vector2.dst(distVctrs[index].x, distVctrs[index].y,
        			                     attackX, attackY);
        	if (distance > biggest) {
        		biggest = distance;
        		bigIndex = index;
        	}
        }
        goal.set(distVctrs[bigIndex].x, distVctrs[bigIndex].y);
    }
    
    public void chase() {
    	// Animal's position
    	float anX = getAnimal().getX();
    	float anY = getAnimal().getY();
    	// Attacker's position
    	float targetX = target.getX();
        float targetY = target.getY();
        // Animal's best option
        float goalX = targetX;
        float goalY = targetY;
        
        // Set best goal if it is safe.
        // Choose valid position farthest from attacker.
        if (map.isSafeAt(goalX, goalY)) {
        	goal.set(goalX, goalY);
        	return;
        }
    }
	
	public void patrol() {
		float anX = getAnimal().getPosition().x;
		float anY = getAnimal().getPosition().y;
		
		boolean isSafeAt = false;
		
		if (patrolTurn == 0) {
			float rand = random.nextFloat();
			if (rand < 0.25) {
				patrolTurn = 1;
			}
			else if (rand < 0.5) {
				patrolTurn = 2;
			}
			else if (rand < 0.75) {
				patrolTurn = 3;
			}
			else {
				patrolTurn = 4;
			}
		}
		isSafeAt = map.isSafeAt(anX + 50, anY);
		if (map.isSafeAt(anX + 50, anY) && patrolTurn == 1) {
			goal.set(anX + 50, anY);
			return;
		}
		isSafeAt = map.isSafeAt(anX - 50, anY);
		if (map.isSafeAt(anX - 50, anY) && patrolTurn == 2) {
			goal.set(anX - 50, anY);
			return;
		}
		isSafeAt = map.isSafeAt(anX, anY + 50);
		if (map.isSafeAt(anX, anY + 50) && patrolTurn == 3) {
			goal.set(anX, anY + 50);
			return;
		}
		isSafeAt = map.isSafeAt(anX, anY - 50);
		if (map.isSafeAt(anX, anY - 50) && patrolTurn == 4) {
			goal.set(anX, anY - 50);
			return;
		}
		patrolTurn = 0;
		return;
	}
    
	public boolean canSettle() {
		return this.turns <= 0;
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
    //public abstract void changeStateIfApplicable();
    
    /*
     * Marks the goal tile the animal is trying to go to
     */
    //public abstract void markGoal();
    
    /*
     * Determines whether or not the animal sees a predator
     *
     * @return true if a predator is in the animal's line of sight. False otherwise
     */
    //public abstract boolean seesPredator();
    
    /*
     * Sets the animal that this animal should flee from. Null if it shouldn't flee.
     */
    public void setAttacker(Actor ac) {
        this.attacker = ac;
    }
    
    /**
     * Returns the target of the current animal AI
     * 
     * @return Target of the animal AI
     */
    public Actor getTarget() {
    	return target;
    }
    
    /**
     * Sets the animal that this animal should chase. Null if it shouldn't chase.
     * 
     * @param ac Actor the animal should target
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
    //public abstract boolean isSeenByPredator();
        
    /*
     * Determines whether the animal sees prey.
     *
     * @return true if an animal's prey is in the line of sight of the animal.
     * false otherwise.
     */
    //public abstract boolean seesPrey();
    
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
    
    public void setTurns() {
    	this.turns = 50;
    }
    
    /**
     * Gets the move that will get the animal to its goal the fastest
     *
     * @return int corresponding to InputController bit-vector
     */
    public Vector2 getNextMoveToGoal() {
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
    	tmp.set(goal);
    	return tmp.sub(getAnimal().getPosition());
    }
    
    // Should not be here, but need to finish
    public Vector2 getClickPos() {return new Vector2();}
    
    public boolean isClicked() {return false;}
    
    public int getNum() {return 0;}
}







