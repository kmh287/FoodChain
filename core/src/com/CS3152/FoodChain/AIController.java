package com.CS3152.FoodChain;

import java.util.*;

import com.badlogic.gdx.math.*;

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
    // The game board; used for pathfinding
    protected GameMap map;
    // The animal's current state
    protected State state;
    // The animal's target (assigned when an animal crosses
    // this animal's line of sight) (null if this animal is a sheep)
    protected Actor target;
    // The animal that this animal is fleeing
    protected Actor attacker;
    // All animals on the map
    protected List<Actor> actors;
    
    /* Although Vector2 stores floats, we are using their int values to specify the
     * tile the animal is on.
     */
    // The animal's goal tile
    protected Vector2 goal;
    // The animal's tile location
    protected Vector2 loc;
    
    // The animal's next move; a ControlCode
    protected int move;
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
        
        this.loc = new Vector2(map.screenXToMap(animal.getxPos()),
                               map.screenYToMap(animal.getyPos()));
        
        this.state = State.PATROL;//FIND;
        this.goal = new Vector2();
        // To where it should start moving
        setGoal((int)getLoc().x - 4, (int)getLoc().y);
        this.move = InputController.WEST;
        this.ticks = 0;
        
        this.target = null;
        this.attacker = null;
    }
    
    /*
     * Updates the animal's tile location
     */
    public void updateLoc() {
        this.loc.set(map.screenXToMap(animal.getxPos()),
                     map.screenYToMap(animal.getyPos()));
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
    public int getAction() {
        // Increment the number of ticks.
        ticks++;
        
        if (ticks % 10 == 0 && state != State.DEAD) {
            // Process the State
            changeStateIfApplicable();
            //System.out.println("state:" + this.state);
            
            // Pathfinding
            markGoal();
            move = getNextMoveToGoal();
        }
        
        //System.out.println(move);
        return move;
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
     * Gets the move that will get the animal to its goal the fastest
     *
     * @return int corresponding to InputController bit-vector
     */
    public int getNextMoveToGoal() {
    	
    	//System.out.println("goalx:" + goal.x + "goaly:" + goal.y);
    	//System.out.println("locx:" + getLoc().x + "locy:" + getLoc().y);
    	
        if (goal.x - getLoc().x == 0 && goal.y - getLoc().y > 0) {
            return NORTH;
        }
        else if (goal.x - getLoc().x > 0 && goal.y - getLoc().y > 0) {
            return NORTHEAST;
        }
        else if (goal.x - getLoc().x > 0 && goal.y - getLoc().y == 0) {
            return EAST;
        }
        else if (goal.x - getLoc().x > 0 && goal.y - getLoc().y < 0) {
            return SOUTHEAST;
        }
        else if (goal.x - getLoc().x == 0 && goal.y - getLoc().y < 0) {
            return SOUTH;
        }
        else if (goal.x - getLoc().x < 0 && goal.y - getLoc().y < 0) {
            return SOUTHWEST;
        }
        else if (goal.x - getLoc().x < 0 && goal.y - getLoc().y == 0) {
            return WEST;
        }
        else if (goal.x - getLoc().x < 0 && goal.y - getLoc().y > 0) {
            return NORTHWEST;
        }
        else {
            return NO_ACTION;
        }
    }
    
    // Should not be here, but need to finish
    public Vector2 getClickPos() {return new Vector2();}
}







