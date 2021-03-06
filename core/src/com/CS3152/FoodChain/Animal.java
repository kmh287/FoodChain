package com.CS3152.FoodChain;

import java.util.List;

import com.CS3152.FoodChain.Actor.actorType;
import com.badlogic.gdx.ai.pfa.DefaultGraphPath;
import com.badlogic.gdx.ai.pfa.indexed.IndexedAStarPathFinder;
import com.badlogic.gdx.ai.steer.GroupBehavior;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.steer.behaviors.Flee;
import com.badlogic.gdx.ai.steer.behaviors.FollowPath;
import com.badlogic.gdx.ai.steer.behaviors.Seek;
import com.badlogic.gdx.ai.steer.proximities.RadiusProximity;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath;
import com.badlogic.gdx.ai.steer.utils.paths.LinePath.LinePathParam;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Animal extends Actor{

	// Whether the animal is caught in a trap
	private boolean trapped = false;
	
	private AIController.State state;
	
	// Vector that runs from the center of the animal diagonally leftward some length
    // RELATIVE TO ANIMAL'S POSITION
    protected Vector2 leftSectorLine;
    // Vector that runs from the center of the animal diagonally rightward that length
    // RELATIVE TO ANIMAL'S POSITION
    protected Vector2 rightSectorLine;
    
    // A vector used for temporary calculations
    private Vector2 tmp;

	//texture used in getCenter and setCenter
	private float texWidth;
	private float texHeight;
	protected static final int AnimalWidth = 40;
	protected static final int AnimalHeight = 40;
	//how far forward an animal can move in a turn. 
    protected static final float MOVE_SPEED = 10.0f;
    // How wide the animal's line of sight is
    protected double SIGHT_ANGLE = 0.35;
    // How long the animal's line of sight is
    protected float SIGHT_LENGTH;
    
    protected float SIGHT_RADIUS;

    protected float MIN_STEERING = 0.001f;
    
    protected SteeringBehavior<Vector2> collisionAvoidanceSB;
    protected SteeringBehavior<Vector2> wanderSB;
    protected SteeringBehavior<Vector2> fleeSB;
    protected SteeringBehavior<Vector2> evadeSB;
    protected SteeringBehavior<Vector2> seekSB;
    protected RadiusProximity<Vector2> proximity;
    
    protected boolean isRabid;
    
    private boolean finishedDeatAnimation;
    
    protected Array<Vector2> wayPoints;
    protected LinePath<Vector2> linePath;
    protected LinePath<Vector2> pathToPatrol;
    protected Array<Vector2> ptp;
    protected FollowPath<Vector2, LinePathParam> followPathAnimal;

    protected FollowPath<Vector2, LinePathParam> followPathToPatrol;
    
    protected IndexedAStarPathFinder<MapNode> pathFinder;
	//protected SmoothableMapPath<MapNode> path = new SmoothableMapPath<MapNode>();
    protected DefaultGraphPath<MapNode> path = new DefaultGraphPath<MapNode>();
    protected GameMap map;
    protected TiledManhattanDistance<MapNode> heuristic;
    
	/** Protected constructor for the animal type. 
	 * 
	 * This constructor should never be called directly
	 * Animals should be instantiated as specific animals
	 * 
	 * @param type The type of animal. Check the animalType enum for values
	 * @param x	STARTING x-position on the map for the animal
	 * @param y STARTING y-position on the map for the animal
	 * @param patrol 
	 * @param foodchainVal The food chain value for this animal. 
	 *                     It is up to the CALLER to ensure this is correct.
	 */
	public Animal(TextureRegion tr, actorType type, float x, float y, 
	              actorType[] prey, Vector2 facing, List<Vector2> patrol,
	              IndexedAStarPathFinder<MapNode> pathFinder, GameMap map, TiledManhattanDistance<MapNode> heuristic){
		super(tr, type, x, y, AnimalWidth, AnimalHeight, prey);
		this.heuristic = heuristic;
		this.setPos(GameMap.pixelsToMeters(x), GameMap.pixelsToMeters(y));
		setFacing(facing);
		this.leftSectorLine = new Vector2();
		this.rightSectorLine = new Vector2();
		
		//need to determine default state of animal on initialize
		//also when  set to patrol it crashes on restart
		

		
		this.tmp = new Vector2();

		updateLOS(0);
		setTexWidth(GameMap.pixelsToMeters(tr.getRegionWidth()));
		setTexHeight(GameMap.pixelsToMeters(tr.getRegionHeight()));
		this.tagged = false;
		finishedDeatAnimation= false;
		
		this.pathFinder = pathFinder;
		this.map = map;

		wayPoints=new Array<Vector2>();
		for(int i=0;i<patrol.size();i++){
			//wayPoints.add(patrol.get(i));
			//adding .4 for offset to gamemap
			wayPoints.add(new Vector2((float) (GameMap.pixelsToMeters(patrol.get(i).x)+.4),(float) (GameMap.pixelsToMeters(patrol.get(i).y)+.4)));
		}
	
		linePath = new LinePath<Vector2>(wayPoints, false);
		ptp = new Array<Vector2>();
		ptp.add(this.getPosition());
		ptp.add(ptp.get(0));
		pathToPatrol = new LinePath<Vector2>(ptp, false);
		//.5 is the offset/farthest distance it can be away from the patrol path
		followPathAnimal = new FollowPath<Vector2, LinePathParam>(this, linePath, (float) .5);
		followPathToPatrol = new FollowPath<Vector2, LinePathParam>(this, pathToPatrol, (float) .5);
		
		//if not waypoints, set to wander
		if(this instanceof Owl){
			this.state = AIController.State.WANDER;
		}
		//else put in patrol state
		else{
			this.state = AIController.State.PATROL;
		}
		
		
	}
	
	public abstract void createSteeringBehaviors();
	
	public void setPos(float x, float y){
		
		float xPos = getX();
		float yPos = getY();
		
	    if (x - getPosition().x == 0 && y - getPosition().y > 0){
	        this.facing = InputController.NORTH;
	    }
	    else if (x - getPosition().x > 0 && y - getPosition().y > 0){
	        this.facing = InputController.NORTHEAST;
	    }
	    else if (x - getPosition().x > 0 && y - getPosition().y == 0){
	        this.facing = InputController.EAST;
	    }
	    else if (x - getPosition().x > 0 && y - getPosition().y < 0){
	        this.facing = InputController.SOUTHEAST;
	    }
	    else if (x - getPosition().x == 0 && y - getPosition().y < 0){
	        this.facing = InputController.SOUTH;
	    }
	    else if (x - getPosition().x < 0 && y - getPosition().y < 0){
	        this.facing = InputController.SOUTHWEST;
	    }
	    else if (x - getPosition().x < 0 && y - getPosition().y == 0){
	        this.facing = InputController.WEST;
	    }
	    else if (x - getPosition().x < 0 && y - getPosition().y > 0){
	        this.facing = InputController.NORTHWEST;
	    }
	    else{
	        //Standing still
	    		//do nothing
	    		//If this code doesn't change, remove this else
	    }
        super.setPosition(xPos, yPos);
	}
	
	public float getMoveSpeed() {
		return MOVE_SPEED;
	}
	
	public abstract void updateLOS(float angle);
    
	/**
	 * The name of this animal type, e.g. "Sheep"
	 * @return String representing the name of this type
	 */
	public abstract String getTypeNameString();

	/**
	 * returns width of texture
	 * @return Vector
	 */
	public float getTexWidth(){
		return this.texWidth;
	}
	/**
	 * returns height of texture
	 * @return String representing the name of this type
	 */
	public float getTexHeight(){
		return this.texHeight;
	}
	
	protected void setTexHeight(float texHeight){
		this.texHeight = texHeight;
	}
	
	protected void setTexWidth(float texWidth){
		this.texWidth = texWidth;
	}
	
	
	/**
	 * returns center of animal
	 * @return String representing the name of this type
	 */
	public Vector2 getCenter(){
		Vector2 pos = new Vector2(getX()+getTexWidth()/2, getY()+getTexHeight()/2);
        return pos;
	}
	
	public void setCenter(Vector2 pos){
		float xPos = pos.x-getTexWidth()/2;
		float yPos = pos.y-getTexHeight()/2;
        super.setPosition(xPos, yPos);
	}
	
	/**
	 * Standard toString methoH
	 * @return A string representation of this animal's type and position
	 */
	public String toString(){
		String type = getTypeNameString();
		return type + " at x: " + getX() + " y: " + getY();
	}
	
	/**
	 * Used to check to see if an animal is caught in a trap.
	 * @return A boolean value of whether the animal is trapped or not
	 */
	public boolean getTrapped() {
		return trapped;
	}
	
	/**
	 * Used to set that an animal is trapped or not.
	 * @param val A boolean value of whether the animal is trapped or not
	 */
	public void setTrapped(boolean val) {
		trapped = val;
	}

	    
	/**
	 * @return
	 */
	public boolean canEat(Actor ani){
	    for (actorType t : getPrey()){
	        if (ani.getType() == t){
	            return true;
	        }
	    }
        return false;
	}
	
	public float getXDiamter() {
	    return texWidth;
    }
	
	public float getSightLength() {
		return SIGHT_LENGTH;
	}
	
	public float getSightRadius() {
		return SIGHT_RADIUS;
	}
	
	public Vector2 getLeftSectorLine() {
		return this.leftSectorLine;
	}
	
	public Vector2 getRightSectorLine() {
		return this.rightSectorLine;
	}
	
	public void setRabid(int rabidity) {
		if (rabidity <= 0) {
			isRabid = false;
		}
		else {
			isRabid = true;
		}
		
	}
	
	public void drawDebugSight(GameCanvas canvas) {
		if (getAlive()) {
			//old code that draws vision lines to scale
//			Vector2 position = getPosition();
//			tmp.set(GameMap.metersToPixels(position.x), GameMap.metersToPixels(position.y));
//			tmp2.set(GameMap.metersToPixels(position.x), GameMap.metersToPixels(position.y));
//			Vector2 sectorLine = getLeftSectorLine();
//			tmp2.add(GameMap.metersToPixels(sectorLine.x), GameMap.metersToPixels(sectorLine.y));
//			canvas.drawLine(Color.YELLOW, tmp, tmp2);
//			tmp3.set(GameMap.metersToPixels(position.x), GameMap.metersToPixels(position.y));
//			sectorLine = getRightSectorLine();
//			tmp3.add(GameMap.metersToPixels(sectorLine.x), GameMap.metersToPixels(sectorLine.y));
//			canvas.drawLine(Color.YELLOW, tmp, tmp3);
			
			//draws patrol waypoint if they have waypoints
			if(wayPoints.size>0){
				canvas.DrawPatrolPaths(GameMap.metersToPixels(followPathAnimal.getInternalTargetPosition().x), GameMap.metersToPixels(followPathAnimal.getInternalTargetPosition().y), 5, wayPoints, linePath);
				for (int i = 0; i < wayPoints.size; i++) {
					int next = (i + 1) % wayPoints.size;				
					if (next != 0 || !linePath.isOpen()) {
						canvas.drawLine(Color.GREEN, new Vector2(GameMap.metersToPixels(wayPoints.get(i).x),GameMap.metersToPixels(wayPoints.get(i).y)), new Vector2(GameMap.metersToPixels(wayPoints.get(next).x),GameMap.metersToPixels(wayPoints.get(next).y)));
					}
					canvas.DrawPatrolPaths(GameMap.metersToPixels(wayPoints.get(i).x), GameMap.metersToPixels(wayPoints.get(i).y), 5, wayPoints, linePath);
				}
			}
			/*
			Vector2 position = getPosition();
      tmp.set(GameMap.metersToPixels(position.x), GameMap.metersToPixels(position.y));
      tmp2.set(GameMap.metersToPixels(position.x), GameMap.metersToPixels(position.y));
      Vector2 sectorLine = getLeftSectorLine();
      tmp2.add(GameMap.metersToPixels(sectorLine.x), GameMap.metersToPixels(sectorLine.y));
      canvas.drawLine(Color.YELLOW, tmp, tmp2);
      tmp2.set(GameMap.metersToPixels(position.x), GameMap.metersToPixels(position.y));
      sectorLine = getRightSectorLine();
      tmp2.add(GameMap.metersToPixels(sectorLine.x), GameMap.metersToPixels(sectorLine.y));
      canvas.drawLine(Color.YELLOW, tmp, tmp2);
      */
		}
	}
	
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
	public void drawCone(GameCanvas canvas){
		Vector2 position = getPosition();
		tmp.set(GameMap.metersToPixels(position.x), GameMap.metersToPixels(position.y));
		//draw cone
		//if patrolling, staying still, or wandering, then draw yellow cone
		
		if (this instanceof Owl) {
			if (getTarget() != null) {
				canvas.drawCone(true, tmp, body.getAngle(),this.getType());
			}
			else {
				canvas.drawCone(false, tmp, body.getAngle(),this.getType());
			}
		}
		else if(getState()==AIController.State.PATROL ||
				getState()==AIController.State.STAYSTILL|| getState()==AIController.State.WANDER || getState()==AIController.State.FIND){
			canvas.drawCone(false, tmp, body.getAngle(),this.getType());
		}
		else{
			canvas.drawCone(true, tmp, body.getAngle(),this.getType());
		}
	}
	
	@Override
	public void draw(GameCanvas canvas) {
		if (getFinishedDeatAnimation()==false) {
			super.draw(canvas);
		}
		if(isRabid && this.getType()==actorType.PIG){
			canvas.drawExclamation(body.getPosition());
		}
	}
	
	@Override
	public void update(float delta) {
		super.update(delta);
		updateLOS(getAngle() + (float)Math.PI/2.0f);
	}
	
	@Override
	public void calculateSteering() {		
		if (state != AIController.State.FIND) {
			collisionAvoidanceSB.calculateSteering(steeringOutput);
		}
		
		switch (state) {
		case WANDER:
			wanderSB.calculateSteering(steeringOutput);
			break;
		case CHASE:
			seekSB.calculateSteering(steeringOutput);
			break;
		case FLEE:
			if (isRabid) {
				evadeSB.calculateSteering(steeringOutput);
			}
			else {
				fleeSB.calculateSteering(steeringOutput);
			}
			break;
		case KILL:
			steeringOutput.setZero();
			break;
		case PATROL:
			followPathAnimal.calculateSteering(steeringOutput);
			break;
		case STAYSTILL:
			steeringOutput.setZero();
			this.setLinearVelocity(new Vector2(0,0));
			break;
		case FIND:
			try {
				followPathToPatrol.calculateSteering(steeringOutput);
			}
			catch (Exception e) {
				setState(AIController.State.WANDER);
			}
			break;
    case DEAD:
      break;
    default:
      break;
		}
		return;
	}

	
	public Array<Vector2> getWayPointList() {
		return wayPoints;
	}
	
	public void applySteering(float delta) {
		super.applySteering(steeringOutput, delta);
	}
	
	public void setState(AIController.State state) {
		this.state = state;
	}
	
	public AIController.State getState() {
		return state;
	}
	
	public void setFlee(Actor actor) {
		((Flee<Vector2>) fleeSB).setTarget(actor);
	}
	
	public void resetFlee() {
		((Flee<Vector2>) fleeSB).setTarget(null);
	}
	
	public void setTarget(Actor actor) {
		((Seek<Vector2>) seekSB).setTarget(actor);
	}
	
	public void resetTarget() {
		((Seek<Vector2>) seekSB).setTarget(null);
	}

	public boolean getFinishedDeatAnimation(){
		return finishedDeatAnimation;
	}
	
	public void setFinishedDeatAnimation(boolean b){
		this.finishedDeatAnimation=b;
	}
	
	public void calculatePath(MapNode goal) {
		Vector2 pos = getPosition();
		float pixX = GameMap.metersToPixels(pos.x);
		float pixY = GameMap.metersToPixels(pos.y);
		int x = map.screenXToMap(pixX);
		int y = map.screenYToMap(pixY);
		
		pathFinder.searchNodePath(map.getNode(map.calculateIndex(x,y)), 
								  goal, heuristic, path);
	}
	
	public void getPathToPatrol () {
		ptp.clear();
		for (MapNode mn : path) {
			int tileX = mn.getX();
			int tileY = mn.getY();
			float pixX = map.mapXToScreen(tileX);
			float pixY = map.mapYToScreen(tileY);
			float x = GameMap.pixelsToMeters(pixX + 20.0f);
			float y = GameMap.pixelsToMeters(pixY + 20.0f);
			ptp.add(new Vector2(x,y));
		}
		if (ptp.size <= 1) {
		  setState(AIController.State.WANDER);
		  return;
		}
		pathToPatrol.createPath(ptp);
		followPathToPatrol.setPath(pathToPatrol);
	}
	
	public void setProximityRadius(float radius) {
	  RadiusProximity<Vector2> proximity = (RadiusProximity<Vector2>) ((GroupBehavior<Vector2>) collisionAvoidanceSB).getProximity();
	  proximity.setRadius(radius);
	}
	
	public abstract Actor getTarget();
}
