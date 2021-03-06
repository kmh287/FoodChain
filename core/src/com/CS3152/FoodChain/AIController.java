package com.CS3152.FoodChain;

import java.util.*; 	

import com.badlogic.gdx.audio.Sound;
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
        //Animal finds waypoint
        FIND,
        //Animal patrols through waypoints
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
    protected Array<Actor> actors;
    
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
    
    protected int rabidity;
    
    // The animal's next move; a ControlCode
    protected Vector2 move;

    // Number of ticks since controller started
    protected int ticks;
    
    // Which direction to patrol
    protected int patrolTurn;
    
    //this is the delay when animals switch from flee to wander to patrol
    private int stateDelay = 60;
    
    private int rabidLength = 90;
    
    //So the owls can add a random angle offset
    //So that they are not always in phase with each other.
    private boolean firstUpdate;

    private static float panicPercentage;
    private static boolean panicked;
    private float angle;
    private double angleProg;
    private Random rand;

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
    public AIController(Animal animal, World world, GameMap map, Array<Actor> actors) {
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
        
        sound = null;
		sndcue = -1;
		WanderStopRate= MathUtils.random(175,225);
		
		angle = 0f;
		angleProg = 0f;
		firstUpdate = true;
		rand = new Random();
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
        if (animal instanceof Owl) {
        	animal.setTarget(ac);
        	this.target = ac;
        }
        else {
	        if (ac != null) {
		    	this.target = ac;
		        animal.setTarget(ac);
	        }
	        else {
	        	this.target = null;
	        }
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
    
    public void setRabidity(int rabidLength) {
    	this.rabidity = rabidLength;
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
	public boolean isTrapSetPressed() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTrapPickupHeldDown() {
		// TODO Auto-generated method stub
		return false;
	}

	public void preUpdate() {
		if (!(animal instanceof Owl)){
			animal.calculateSteering();
		}
		else {
		  if (!panicked) {
		    setTarget(null);
		  }
		}
		  rayCast();
	}
	
	/**
	 * Returns the difference in angle for this frame
	 * This function should simulate periodic movement that 
	 * accelerates and decelerates. This should only be used
	 * for calculating the owl's new angle.
	 * 
	 * The function it simulates is 
	 * (Pi/100) * cos(4x) + (Pi/100)
	 * 
	 * @param angle : The current angle of the owl 
	 * @return diff : A float representing the angle change for this frame
	 */
	private double getDiff(double angle){
		return (Math.PI / 100) * Math.cos(4*angle) + (Math.PI/100);
	}
	
	public void update(float delta) {
		// TODO Auto-generated method stub
		if (animal instanceof Owl) {
			angleProg = (angleProg > (Math.PI * 2)) ? 0f : angleProg+(Math.PI/120.0);
			angle += getDiff(angleProg);
			//If this is the first frame, add a random number to the owl's offset 
			//To push multiple owls on the same map out of phase
			if(firstUpdate){
				angle+= (rand.nextDouble() * 2 * Math.PI);
				firstUpdate = false;
			}
			animal.setAngle(angle);
			animal.updateLOS(angle);
			if (hasTarget()) {
			  panicked = true;
			}
			//changeStateIfApplicable();
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
			    	if (hasTarget()) {
			    	  if (animal instanceof Pig) {
			    	    animal.setProximityRadius(0.0001f);
			    	    animal.setState(State.FLEE);
			    	    setTurns(stateDelay);
			    	  }
			    	  else if (animal instanceof Wolf) {
			    	    animal.setState(State.CHASE);
			    	    setTurns(stateDelay);
			    	  }
			    	  else if (animal instanceof Owl){
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
			    	if (animal.getWayPointList().size != 0) {
			    		Vector2 goal = animal.getWayPointList().get(0);
			    		float pixX = GameMap.metersToPixels(goal.x);
			    		float pixY = GameMap.metersToPixels(goal.y);
			    		int x = map.screenXToMap(pixX);
			    		int y = map.screenYToMap(pixY);
			    		
			    		animal.calculatePath(map.getNode(map.calculateIndex(x,y)));
			    		// May change state
			    		animal.getPathToPatrol();
			    		if (animal.getState() == State.WANDER) {
			    			break;
			    		}
			    		else {
			    			animal.setState(State.FIND);
			    		}
			    	}
			    	else if(Wanderturns%250>WanderStopRate){
			    		animal.setState(State.WANDER);
			    		WanderStopRate= MathUtils.random(175,225);
			    		Wanderturns=0;
			    	}
			    	Wanderturns+=1;
			    	break;
			    case CHASE:
			        if (turns <= 0) {
			        	animal.setState(State.WANDER);
			        	setTarget(null);
			        }
			        if (isScared()) {
			          animal.setProximityRadius(0.0001f);
			        	animal.setState(State.FLEE);
			        }
			        if (hasTarget() && !target.getAlive()) {
			        	animal.setState(State.KILL);
			        	setTarget(null);
			        	setTurns(stateDelay);
			        }
			        //if chasing the hunter, then increase panic
			        if(getTarget() instanceof Hunter){
				        panicked = true;
			        }
			        turns--;
			        break;
			    case FLEE:
			    	//System.out.println(getAnimal() + " is fleeing");
			        if (canSettle()) {
			        	setRabidity(0);
			        	animal.setRabid(rabidity);
			            animal.setState(State.WANDER);
			            setAttacker(null);
			            setTarget(null);
			            break;
			        }
			        //if fleeing hunter, then increase panic
			        if(getTarget() instanceof Hunter){
				        panicked = true;
			        }
			        if(getPanicPercentage() >= .75f) {
			        	// For Evade
			        	setRabidity(rabidLength);
			        	animal.setRabid(rabidity);
			        }
			        else {
			        	rabidity--;
			        	animal.setRabid(rabidity);
			        }
			        turns--;
			    	break;
			    case KILL:
			    	if (turns <= 0) {
			    		animal.setState(State.WANDER);
			    	}
			    	turns--;
			    	break;
			    case PATROL:
			    	animal.setState(State.PATROL);
			    	if (hasTarget()) {
				    	  if (animal instanceof Pig) {
				    	    animal.setProximityRadius(0.0001f);
				    	    animal.setState(State.FLEE);
				    	    setTurns(stateDelay);
				    	  }
				    	  else if (animal instanceof Wolf) {
				    	    animal.setState(State.CHASE);
				    	    setTurns(stateDelay);
				    	  }
				      }
			    	break;
			    case FIND:
			    	if (hasTarget()) {
				    	  if (animal instanceof Pig) {
				    	    animal.setProximityRadius(0.0001f);
				    		  animal.setState(State.FLEE);
				    		  setTurns(stateDelay);
				    	  }
				    	  else if (animal instanceof Wolf) {
				    		  animal.setState(State.CHASE);
				    		  setTurns(stateDelay);
				    	  }
				    }
			    	else if (canPatrol()) {
			    	    animal.setState(State.PATROL);
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

	private boolean canPatrol() {
      Vector2 position = animal.getPosition();
      int tileX = map.screenXToMap(GameMap.metersToPixels(position.x));
      int tileY = map.screenYToMap(GameMap.metersToPixels(position.y));
      Vector2 firstWaypoint = animal.getWayPointList().get(0);
      int wayX = map.screenXToMap(GameMap.metersToPixels(firstWaypoint.x));
      int wayY = map.screenYToMap(GameMap.metersToPixels(firstWaypoint.y));
      if (tileX == wayX && tileY == wayY) {
        return true;
      }
      else {
        return false;
      }
    }

  public static void increasePanic() {
		if(panicPercentage<1f){
			panicPercentage+=.003f;
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

  @Override
  public boolean escPressed() {
    
    return false;
  }


//	@Override
//	public int levelPressed() {
//		// TODO Auto-generated method stub
//		return 0;
//	}
}