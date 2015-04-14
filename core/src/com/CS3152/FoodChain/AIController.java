package com.CS3152.FoodChain;

import java.util.*; 	

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import com.badlogic.gdx.ai.pfa.PathSmoother;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
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
        // Animal is dead
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
    
    // The path finder used to calculate paths to the goal
    protected IndexedAStarPathFinder<MapNode> pathFinder;
    
    // Heuristic used to approximate distance to a node
    private static TiledManhattanDistance<MapNode> heuristic;
    
    // The path the AI uses to get from its current position to the goal
    private SmoothableMapPath<MapNode> path;
    
    // PathSmoother object used to smooth out path
    private PathSmoother<MapNode, Vector2> pathSmoother;
    
    // The random number generator used by the AI
    private Random random;

    private static float panicPercentage;
    private Hunter hunter; 
    
    private Vector2 vect;
    private float angle;
    
    private Sound sound;
	/** The associated sound cue (if ship is making a sound). */
	private long sndcue;
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
        goal.set (getAnimal().getX() + 1 , getAnimal().getY());

        this.turns = 10;//should be 0 in future
        
        this.patrolTurn = 0;
        
        this.pathFinder = new IndexedAStarPathFinder(map);
        
        this.heuristic = new TiledManhattanDistance<MapNode>();
        
        //this.pathSmoother = new PathSmoother<MapNode, Vector2>();
        
        this.path = new SmoothableMapPath();
        
        this.tmp = new Vector2();
        
        this.random = new Random(2);
        
        panicPercentage = 0f;
        angle = animal.getAngle();
        vect = new Vector2(animal.getPosition());
        
        sound = null;
		sndcue = -1;
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
      int testx = map.getMapWidth();
      int testy = map.getMapHeight();
      goal.set(400, 400);
        // Increment the number of ticks.
        //ticks++;
        
        //if (ticks % 10 == 0 && state != State.DEAD) {
        //comment out for fixing collisions
      // Process the State
      //changeStateIfApplicable();
      //checkCone();
        // RayCasting
        //Should be at beginning
      
      for (Actor a : actors) {
        if (a != getAnimal() && a.getAlive()) {
          world.rayCast(vcb, getAnimal().getPosition(), a.getPosition());
          Fixture fix = vcb.getFixture();
          if (fix != null) {
              Object objSeen = fix.getBody().getUserData();
              if (objSeen instanceof Actor) {
                Actor actor = (Actor) objSeen;
                if (panicPercentage<1 && actor.getType() != animal.getType()){
                  panicPercentage += 0.005;//.001
                }
                if (((Actor)objSeen).canKill(getAnimal())) {
                  setScared((Actor)objSeen);
                  setTurns();
                  setTarget(null);
                }
                if (getAnimal().canKill((Actor)objSeen)) {
                  setScared(null);
                  setTurns();
 
                  setTarget((Actor)objSeen);
                  if (animal.getTypeNameString() == "Wolf") {
                	  play(SoundController.WOLF_ANGRY_SOUND);
                  }
                }
              }
              else {
                if (target != null) {
                  if (a.getType() == target.getType()) {
                    turns--;
                  }
                }
              }
          }
          else {
            if (animal.getTypeNameString() == "Owl" &&
                a.getType() == Actor.actorType.HUNTER) {
              setTarget(null);
            }
            if (target != null) {
              if (a.getType() == target.getType()) {
                turns--;
              }
            }
            if (panicPercentage>0){
              panicPercentage -= 0.00005;
            }
          }
          if (canSettle()) { 
            setScared(null);
          setTarget(null);
          }
          //vcb.getFixture();
          }
      }
      if (getTarget() instanceof Animal) {
        Animal target = (Animal) getTarget();
        if (!target.getAlive()) {
          System.out.println("Wolf killed sheep");
          setTarget(null);
        }
      }
      if (getTarget() instanceof Hunter) {
        Hunter target = (Hunter) getTarget();
        if (!target.getAlive()) {
          setTarget(null);
        }
      }
          
        if (isScared()) {
          if (getAnimal().getTypeNameString() == "Owl") {
            //fleeOwl();
          }
          //System.out.println(animal.getType() + ": flee");
          else { 
            //System.out.println(getAnimal().getTypeNameString() + "isScared");
            flee();
          }
        }
        
        else if (hasTarget()) {
          if (getAnimal().getTypeNameString() == "Owl") {
            //System.out.println(getAnimal().getTypeNameString() + "owlhasTarget");
            //fleeOwl();
        	  
          }
          else {
            //System.out.println(getAnimal().getTypeNameString() + "hasTarget");
            chase();
          }
          //System.out.println(getAnimal().getTypeNameString() + ": chase"); 
        }
        else {
          if (getAnimal().getTypeNameString() == "Owl") {
            /*float angle = animal.getAngle();
            angle += Math.PI/8.0;
            System.out.println("Owl angle owwll: " + angle);
            animal.setAngle(angle); 
            animal.updateLOS(angle);*/
            //animal.deactivatePhysics(world);
            if (Math.random() > 0) {
              angle += Math.PI/80;
            }
            animal.updateLOS(angle);
            vect.set(vect.x + (float)Math.cos(angle), vect.y + (float)Math.sin(angle));
            goal.set(vect.x, vect.y);
            //System.out.println("Owl angle owwll: " + angle);
            play(SoundController.OWL_SOUND);
          }
          else {
          //System.out.println(animal.getType() + ": patrol");
            patrol();
            //System.out.println(getAnimal().getTypeNameString() + "patrol angle");
            //System.out.println(animal.getType() + "now patrolling");
          }
        }
        
        if (getAnimal().getTypeNameString() == "Owl") {
          //System.out.println("Owl angle: " + animal.getAngle());
        }
        move = getNextMoveToGoal();
        
        getPathToGoal();
        
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
    
    public void fleeOwl() {
      // Animal's position
      float anX = getAnimal().getX();
      float anY = getAnimal().getY();
      // Get random position on map. 
      //float targetX = goal.set(20, 5);
        //float targetY = ;
        // Animal's best option
        float goalX = 20;
        float goalY = 20;
        
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
      this.turns = 100;
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
      return tmp.sub(getAnimal().getPosition()).nor();
    }
    
    private void getPathToGoal() {
      int startX = map.screenXToMap(animal.getX());
      int startY = map.screenYToMap(animal.getY());
      int goalX = map.screenXToMap(goal.x);
      int goalY = map.screenYToMap(goal.y);
      path.clear();
      pathFinder.searchNodePath(map.getNode(map.calculateIndex(startX, startY)),
                    map.getNode(map.calculateIndex(goalX, goalY)),
                    heuristic,
                    path);
    }
    
    // Should not be here, but need to finish
    public Vector2 getClickPos() {return new Vector2();}
    
    public boolean isClicked() {return false;}
    
    public int getNum() {return 0;}
    

    public static float getPanicPercentage() {
      return panicPercentage;
    }
    public void drawPath(GameCanvas canvas) {
      int pathSize = path.getCount();
      
      for (int i = 0; i < pathSize - 1; i++) {
        canvas.drawLine(Color.YELLOW,
                tmp.set(map.mapXToScreen(path.get(i).getX()), map.mapYToScreen(path.get(i).getY())),
                tmp.set(map.mapXToScreen(path.get(i+1).getX()), map.mapYToScreen(path.get(i+1).getY())));
      }

    }
    
    public void play(String sound) {
		if (sndcue != -1) {
			this.sound.stop(sndcue);
		}
		this.sound = SoundController.get(sound);
		sndcue = this.sound.loop();
	}

}