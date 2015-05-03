package com.CS3152.FoodChain;

import java.util.*; 	

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath.LinePathParam;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
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
    public static enum State {
        // Animal is wandering
        WANDER,
        // Animal is chasing
        CHASE,
        // Animal is running away
        FLEE,
        // Animal kills target
        KILL,
        // Animal is dead
        DEAD,
        //Animal stays still
        STAYSTILL, 
        PATROL 
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
    protected int Wanderturns;
    
    // The animal's next move; a ControlCode
    protected Vector2 move;

    // Number of ticks since controller started
    protected int ticks;
    
    // Which direction to patrol
    protected int patrolTurn;
    
    //this is the delay when animals switch from flee to wander to patrol
    private int stateDelay = 200;
    


    private static float panicPercentage;
    private static boolean panicked;
    private Hunter hunter; 
    
    private Vector2 vect;
    private float angle;

    private Sound sound;
	/** The associated sound cue (if ship is making a sound). */
	private long sndcue;

    private int WanderStopRate;
    
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
        
        this.loc = new Vector2(map.screenXToMap(GameMap.metersToPixels(animal.getX())),
                               map.screenYToMap(GameMap.metersToPixels(animal.getY())));
        
        this.target = null;
        this.attacker = null;
        
        this.vcb = new VisionCallback(this);
        //fcb = new FleeCallback(this, attacker);
        
        //this.state = State.PATROL;//FIND;
        this.goal = new Vector2();
        // To where it should start moving
        this.move = InputController.WEST;
        goal.set (getAnimal().getX() + 1 , getAnimal().getY());

        this.turns = 10;//should be 0 in future
        
        this.patrolTurn = 0;
        
        this.tmp = new Vector2();
        
        panicPercentage = 0f;
        angle = animal.getAngle();
        vect = new Vector2(animal.getPosition());
        
        sound = null;
		sndcue = -1;
		WanderStopRate= MathUtils.random(175,225);
    }
    
    /*
     * Updates the animal's tile location
     */
    public void updateLoc() {
        this.loc.set(map.screenXToMap(GameMap.metersToPixels(animal.getX())),
                     map.screenYToMap(GameMap.metersToPixels(animal.getY())));
    }
    
    // Determines whether or not an actor is in the animal's line of sight
    public static boolean withinCone(Animal an, Vector2 meToActor) {
      return isClockwise(an.getLeftSectorLine(), meToActor) &&
           !isClockwise(an.getRightSectorLine(), meToActor) &&
           meToActor.len() <= an.getSightLength();
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
    		return length.len() <= an.getSightRadius();
    }

    // Determine the new angle the animal wants to face
    // @return that angle.
    public float getAngle() {
    	return animal.getAngle();
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
        if (ac != null) {
        	this.attacker = ac;
        	animal.setFlee(ac);
        }
        else {
        	this.attacker = null;
        }
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
        if (ac != null) {
	    	this.target = ac;
	        animal.setTarget(ac);
        }
        else {
        	this.target = null;
        }
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
      this.scared = false;
      /*else {
        this.scared = false;
        setAttacker(null);
        return;
      }*/ 
    }
    
    /*
     * Returns whether or not this animal should run away
     * 
     * @return true if the animal saw a predator. False otherwise.
     */
    public boolean isScared() {
      return this.scared;
    }
    
    public void setTurns(int turns) {
      this.turns = turns;
    }
    
    // Should not be here, but need to finish
    public Vector2 getClickPos() {return new Vector2();}
    
    public boolean isClicked() {return false;}
    
    public int getNum() {return 0;}

    public static float getPanicPercentage() {
      return panicPercentage;
    }

    public void play(String sound) {
		if (sndcue != -1) {
			this.sound.stop(sndcue);
		}
		this.sound = SoundController.get(sound);
		sndcue = this.sound.loop(); 
	}

	@Override
	public boolean resetPressed() {
		return false;
	}

	@Override
	public boolean isSpacePressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSpaceHeldDown() {
		// TODO Auto-generated method stub
		return false;
	}

	public void preUpdate() {
		rayCast();
		animal.calculateSteering();
	}
	
	public void update(float delta) {
		// TODO Auto-generated method stub
		if (animal instanceof Owl) {
			
		}
		else {
			animal.applySteering(delta);
			changeStateIfApplicable();
		}
	}
	
	/*
     * Changes the animal's state depending on what it's doing and what it's seen
     */
    public void changeStateIfApplicable() {
    	if (getAnimal().getAlive()) {
	        switch (animal.getState()) {
			    case WANDER:
			    	//System.out.println(getAnimal() + " is wandering");
			    	if (hasTarget()) {
			    	  if (animal instanceof Pig) {
			    	    animal.setState(State.FLEE);
			    	    System.out.println("hello");
			    	    setTurns(stateDelay);
			    	  }
			    	  else if (animal instanceof Wolf) {
			    	    animal.setState(State.CHASE);
			    	    setTurns(stateDelay);
			    	  }
			      }
			    	//stop periodically in wander
			    	else if(Wanderturns%250>WanderStopRate){
			    		animal.setState(State.STAYSTILL);
			    		WanderStopRate= MathUtils.random(40,120);
			    		Wanderturns=0;
			    	}
			    	Wanderturns+=1;
			    	break;
			    case STAYSTILL:
			    	if(Wanderturns%250>WanderStopRate){
			    		animal.setState(State.WANDER);
			    		WanderStopRate= MathUtils.random(175,225);
			    		Wanderturns=0;
			    	}
			    	Wanderturns+=1;
			    	break;
			    case CHASE:
			    	//System.out.println(getAnimal() + " is chasing");
			        if (turns <= 0) {
			        	animal.setState(State.WANDER);
			        	setTarget(null);
			        }
			        if (isScared()) {
			        	animal.setState(State.FLEE);
			        }
			        turns--;
			        if (hasTarget() && !target.getAlive()) {
			        	animal.setState(State.KILL);
			        	setTarget(null);
			        	setTurns(500);
			        }
			        //if chasing the hunter, then increase panic
			        if(getTarget() instanceof Hunter){
				        panicked = true;
			        }
			        break;
			    case FLEE:
			    	//System.out.println(getAnimal() + " is fleeing");
			        if (canSettle()) {
			            animal.setState(State.WANDER);
			            setAttacker(null);
			            setTarget(null);
			        }
			        turns--;
			        //if fleeing hunter, then increase panic
			        if(getTarget() instanceof Hunter){
				        panicked = true;
			        }
			        System.out.println(turns);
			    	break;
			    case KILL:
			    	//sdSystem.out.println(getAnimal() + " is killing");
			    	if (turns <= 0) {
			    		animal.setState(State.WANDER);
			    	}
			    	turns--;
			    	break;
			    case PATROL:
			    	animal.setState(State.PATROL);
			    	//this code is commented out until we can resolve state machine
			    	if (hasTarget()) {
				    	  if (animal instanceof Pig) {
				    	    animal.setState(State.FLEE);
				    	    setTurns(stateDelay);
				    	  }
				    	  else if (animal instanceof Wolf) {
				    	    animal.setState(State.CHASE);
				    	    setTurns(stateDelay);
				    	  }
				      }
			    	break;
			    case DEAD:
			        break;
			}
    	}
    	else {
    		animal.setState(State.DEAD);
    	}
	}

	public static void increasePanic() {
		if(panicPercentage<1f){
			panicPercentage+=.005f;
		}
	}
	
	public static void decreasePanic() {
		if(panicPercentage>0){
			panicPercentage-=.002f;
		}
	}
	
	public static boolean AtLeastOneAnimalPanic(){
		return panicked;
	}
	
	public static void resetPanicFlag(){
		panicked = false;
	}
	
	@Override
	public Vector2 getAction(float delta) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}
	
	private void rayCast() {
	  for (Actor a : actors) {
      if (a != getAnimal() && a.getAlive()) {
        world.rayCast(vcb, getAnimal().getPosition(), a.getPosition());
        Fixture fix = vcb.getFixture();
        if (fix != null) {
          Object objSeen = fix.getBody().getUserData();
          if (objSeen instanceof Actor) {
            if (animal.canKill((Actor) objSeen) || ((Actor) objSeen).canKill(animal)) {
              setTarget((Actor) objSeen);
            }
          }
        }
      }
    }
	}
}