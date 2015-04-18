package com.CS3152.FoodChain;

import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.tests.steer.box2d.Box2dLocation;
import com.badlogic.gdx.ai.tests.steer.box2d.Box2dSteeringUtils;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

public abstract class Animal extends Actor implements Steerable<Vector2>{
	
	float boundingRadius;
	
	boolean tagged;
	
	float maxLinearSpeed;
	float maxLinearAcceleration;
	float maxAngularSpeed;
	float maxAngularAcceleration;
	
	boolean independentFacing;
	
	protected SteeringBehavior<Vector2> steeringBehavior;
	
	private final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());

	// Whether the animal is caught in a trap
	private boolean trapped = false;
	
	// The way points for an animal's patrol path
	private Array<Vector2> patrolPath;
	
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
    protected static final float MOVE_SPEED = 75.0f;
    // How wide the animal's line of sight is
    protected double SIGHT_ANGLE = 0.35;
    // How long the animal's line of sight is
    protected float SIGHT_LENGTH;
	
	/** Protected constructor for the animal type. 
	 * 
	 * This constructor should never be called directly
	 * Animals should be instantiated as specific animals
	 * 
	 * @param type The type of animal. Check the animalType enum for values
	 * @param x	STARTING x-position on the map for the animal
	 * @param y STARTING y-position on the map for the animal
	 * @param foodchainVal The food chain value for this animal. 
	 *                     It is up to the CALLER to ensure this is correct.
	 */
	public Animal(TextureRegion tr, actorType type, float x, float y, 
	              actorType[] prey, Vector2 facing){
		super(tr, type, x, y, AnimalWidth, AnimalHeight, prey);
		this.setPos(x,y);
		setFacing(facing);
		this.leftSectorLine = new Vector2();
		this.rightSectorLine = new Vector2();
		
		patrolPath = new Array();

		this.tmp = new Vector2();

		updateLOS(0);
		setTexWidth(tr.getRegionWidth());
		setTexHeight(tr.getRegionHeight());
		this.tagged = false;
	}
	
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
	
//	public void draw(GameMap map, GameCanvas canvas){
//	    canvas.begin();
//	    canvas.draw(getTexture(), getX(), getY());
//	    canvas.end();
//	}
	
	public float getXDiamter() {
	    return texWidth;
    }
	
	public float getSightLength() {
		return SIGHT_LENGTH;
	}
	
	public Vector2 getLeftSectorLine() {
		return this.leftSectorLine;
	}
	
	public Vector2 getRightSectorLine() {
		return this.rightSectorLine;
	}
	
	public Array<Vector2> getPatrolPath() {
		return patrolPath;
	}
	
	public void setPatrolPath(Array<Vector2> path) {
		patrolPath = path;
	}
	
	public void drawSight(GameCanvas canvas) {
		if (getAlive()) {
			tmp.set(getPosition());
			canvas.drawLine(Color.YELLOW, getPosition(), tmp.add(getLeftSectorLine()));
			tmp.set(getPosition());
			canvas.drawLine(Color.YELLOW, getPosition(), tmp.add(getRightSectorLine()));
		}
	}
	
	@Override
	public void draw(GameCanvas canvas) {
		if (getAlive()) {
			super.draw(canvas);
		}
	}
	
    public boolean isTagged() {
    	return tagged;
    }

    public void setTagged(boolean tagged) {
    	this.tagged = tagged;
    }
    
    public boolean isIndependentFacing() {
    	return independentFacing;
    }

    public void setIndependentFacing(boolean independentFacing) {
    	this.independentFacing = independentFacing;
    }
    
	public float getOrientation() {
		return getBody().getAngle();
	}
    
    public float getBoundingRadius() {
    	return boundingRadius;
    }
    
	public Vector2 newVector() {
		return new Vector2();
	}
    public Location<Vector2> newLocation() {
    	return new Box2dLocation();
    }
    
    public float vectorToAngle(Vector2 vector) {
    	return Box2dSteeringUtils.vectorToAngle(vector);
    }

    public Vector2 angleToVector(Vector2 outVector, float angle) {
    	return Box2dSteeringUtils.angleToVector(outVector, angle);
    }

    public SteeringBehavior<Vector2> getSteeringBehavior() {
    	return steeringBehavior;
    }
    
    public void setSteeringBehavior(SteeringBehavior<Vector2> steeringBehavior) {
    	this.steeringBehavior = steeringBehavior;
    }
	
    public void update (float deltaTime) {
    	if (steeringBehavior != null) {
    		// Calculate steering acceleration
    		steeringBehavior.calculateSteering(steeringOutput);

    		/*
    		 * Here you might want to add a motor control layer filtering steering accelerations.
    		 * 
    		 * For instance, a car in a driving game has physical constraints on its movement: it cannot turn while stationary; the
    		 * faster it moves, the slower it can turn (without going into a skid); it can brake much more quickly than it can
    		 * accelerate; and it only moves in the direction it is facing (ignoring power slides).
    		 */

    		// Apply steering acceleration
    		applySteering(steeringOutput, deltaTime);
    	}
    }

    protected void applySteering (SteeringAcceleration<Vector2> steering, float deltaTime) {
    	boolean anyAccelerations = false;

    	// Update position and linear velocity.
    	if (!steeringOutput.linear.isZero()) {
    		Vector2 force = steeringOutput.linear.scl(deltaTime);
    		body.applyForceToCenter(force, true);
    		anyAccelerations = true;
    	}

    	// Update orientation and angular velocity
    	if (isIndependentFacing()) {
    		if (steeringOutput.angular != 0) {
    			body.applyTorque(steeringOutput.angular * deltaTime, true);
    			anyAccelerations = true;
    		}
    	}
    	else {
    		// If we haven't got any velocity, then we can do nothing.
    		Vector2 linVel = getLinearVelocity();
    		if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
    			float newOrientation = vectorToAngle(linVel);
    			body.setAngularVelocity((newOrientation - getAngularVelocity()) * deltaTime); // this is superfluous if independentFacing is always true
    			body.setTransform(body.getPosition(), newOrientation);
    		}
    	}

    	if (anyAccelerations) {
    		// body.activate();

    		// TODO:
    		// Looks like truncating speeds here after applying forces doesn't work as expected.
    		// We should likely cap speeds form inside an InternalTickCallback, see
    		// http://www.bulletphysics.org/mediawiki-1.5.8/index.php/Simulation_Tick_Callbacks

    		// Cap the linear speed
    		Vector2 velocity = body.getLinearVelocity();
    		float currentSpeedSquare = velocity.len2();
    		float maxLinearSpeed = getMaxLinearSpeed();
    		if (currentSpeedSquare > maxLinearSpeed * maxLinearSpeed) {
    			body.setLinearVelocity(velocity.scl(maxLinearSpeed / (float)Math.sqrt(currentSpeedSquare)));
    		}

    		// Cap the angular speed
    		float maxAngVelocity = getMaxAngularSpeed();
    		if (body.getAngularVelocity() > maxAngVelocity) {
    			body.setAngularVelocity(maxAngVelocity);
    		}
    	}
    }
    
    //
    // Limiter implementation
    //

    public float getMaxLinearSpeed () {
    	return maxLinearSpeed;
    }

    public void setMaxLinearSpeed (float maxLinearSpeed) {
    	this.maxLinearSpeed = maxLinearSpeed;
    }

    public float getMaxLinearAcceleration () {
    	return maxLinearAcceleration;
    }

    public void setMaxLinearAcceleration (float maxLinearAcceleration) {
    	this.maxLinearAcceleration = maxLinearAcceleration;
    }

    public float getMaxAngularSpeed () {
    	return maxAngularSpeed;
    }

    public void setMaxAngularSpeed (float maxAngularSpeed) {
    	this.maxAngularSpeed = maxAngularSpeed;
    }

    public float getMaxAngularAcceleration () {
    	return maxAngularAcceleration;
    }

    public void setMaxAngularAcceleration (float maxAngularAcceleration) {
    	this.maxAngularAcceleration = maxAngularAcceleration;
    }
    
    public float getZeroLinearSpeedThreshold () {
    	return 0.001f;
    }

    public void setZeroLinearSpeedThreshold (float value) {
    	throw new UnsupportedOperationException();
    }
}
