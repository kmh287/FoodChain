package com.CS3152.FoodChain;


import com.badlogic.gdx.ai.steer.Steerable;
import com.badlogic.gdx.ai.steer.SteeringAcceleration;
import com.badlogic.gdx.ai.steer.SteeringBehavior;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.ai.tests.steer.box2d.Box2dLocation;
import com.badlogic.gdx.ai.tests.steer.box2d.Box2dSteeringUtils;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;


public abstract class Actor extends CircleObject implements Steerable<Vector2>{
	
	float boundingRadius;
	
	AIController ai;
	
	boolean tagged;
	
	float maxLinearSpeed;
	float maxLinearAcceleration;
	float maxAngularSpeed;
	float maxAngularAcceleration;
	
	boolean independentFacing;
	
	protected SteeringBehavior<Vector2> steeringBehavior;
	
	protected final SteeringAcceleration<Vector2> steeringOutput = new SteeringAcceleration<Vector2>(new Vector2());
	
	// Whether the actor is alive
	private boolean alive = true;
	//The direction this actor is facing
    protected Vector2 facing;
    //TextureRegion for this actor's texture
    protected TextureRegion tr;
	//The type of this actor
	private final actorType type;
	//The actors this actor can kill
	protected final actorType[] victims;
    
    public static enum direction {
        NORTH, NORTHEAST, EAST, SOUTHEAST,
        SOUTH, SOUTHWEST, WEST, NORTHWEST
    }
    
    public enum actorType{
    	HUNTER,
		PIG, 
		WOLF,
		OWL
	}
    
	public boolean activatePhysics(World world) {
		// create the box from our superclass
		if (!super.activatePhysics(world)) {
			return false;
		}
		
		return true;
	}
    
    public Actor(TextureRegion tr, actorType type, float x, float y, float width, 
    		     float height, actorType[] victims) {
	    super(tr, GameMap.pixelsToMeters(x), GameMap.pixelsToMeters(y), GameMap.pixelsToMeters(height/2));
	    this.type = type;
	    this.tr = tr;
        this.facing = new Vector2();
        this.victims = victims;
        setFixedRotation(true);
    }
    

	/*
     * @return the direction this Actor's facing
     */
    public Vector2 getFacing() {
        return this.facing;
    }
    
    public void setFacing(Vector2 dir) {
    	if(getAlive()){
    		this.facing = dir;
    		if (dir.x < 0 && dir.y == 0) {
    			super.setAngle((float) (-Math.PI/2.0));
    		}
    		else if (dir.x > 0 && dir.y == 0) {
    			super.setAngle((float) (Math.PI/2.0));
    		}
    		else if (dir.x == 0 && dir.y > 0) {
    			super.setAngle((float) (Math.PI));
    		}
    		else if (dir.x == 0 && dir.y < 0) {
    			super.setAngle(0.0f);
    		}
    		else if (dir.x < 0 && dir.y < 0) {
    			super.setAngle((float) (-Math.PI/4.0));
    		}
    		else if (dir.x > 0 && dir.y < 0) {
    			super.setAngle((float) (Math.PI/4.0));
    		}
    		else if (dir.x > 0 && dir.y > 0) {
    			super.setAngle((float) (3.0*Math.PI/4.0));
    		}
    		else if (dir.x < 0  && dir.y > 0) {
    			super.setAngle((float) (-3.0*Math.PI/4.0));
    		}
    		//setAngle(dir.angleRad() + (float)Math.PI/2);
    	}

    }
    
    
    /**
     * @return the type
     */
    public actorType getType() {
        return type;
    }
    
    
	/**
	 * The name of this actor type, e.g. "Sheep"
	 * @return String representing the name of this type
	 */
	public abstract String getTypeNameString();
	
	public actorType[] getPrey() {
		return victims;
	}
	
	/**
	 * Return true if this actor can eat ac, false otherwise
	 * @param ac Another actor
	 * @return boolean, whether or not this actor can eat ac
	 */
	public boolean canKill(Actor ac){
	    for (actorType t : victims){
	        if (ac.getType() == t){
	            return true;
	        }
	    }
        return false;
	}
	
	/**
	 * Returns whether the actor is alive
	 * 
	 * @return Whether the actor is alive
	 */
	public boolean getAlive() {
		return alive;
	}
	
	/**
	 * Sets whether the actor is alive
	 * 
	 * @param val A boolean whether the actor is alive
	 */
	public void setAlive(boolean val) {
		alive = val;
	}
	
    public void draw(GameCanvas canvas){
    	super.draw(canvas);
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
    	float oldAngle=getAngle();
    	if (!steeringOutput.linear.isZero()) {
    		//Vector2 force = steeringOutput.linear.scl(deltaTime);
    		Vector2 force = steeringOutput.linear.scl(10);
    		body.applyForceToCenter(force, true);
    		//body.applyLinearImpulse(force, getPosition(), true);
    		anyAccelerations = true;
    	}

    	// Update orientation and angular velocity
 //   	System.out.println(getAngularVelocity());
		//body.applyTorque(100, true);
		
    		// If we haven't got any velocity, then we can do nothing.
    		Vector2 linVel = getLinearVelocity();
    		if (!linVel.isZero(getZeroLinearSpeedThreshold())) {
    			
    			float newOrientation = vectorToAngle(linVel);
    			//body.setAngularVelocity((newOrientation-getAngularVelocity()) * deltaTime); // this is superfluous if independentFacing is always true
    			body.setTransform(body.getPosition(), oldAngle+((newOrientation-oldAngle)*deltaTime*5));
    			//body.setTransform(body.getPosition(), newOrientation);
    		}
 
    	//body.applyTorque(10, true);
		//body.applyAngularImpulse(10, true);
    	if (anyAccelerations) {

    		// body.setActive(true);

    		// TODO:
    		// Looks like truncating speeds here after applying forces doesn't work as expected.
    		// We should likely cap speeds form inside an InternalTickCallback, see
    		// http://www.bulletphysics.org/mediawiki-1.5.8/index.php/Simulation_Tick_Callbacks

    		// Cap the linear speed
    		Vector2 velocity = body.getLinearVelocity();
    		float currentSpeedSquare = velocity.len2();
    		float maxLinearSpeed = getMaxLinearSpeed()+AIController.getPanicPercentage()*8;
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
    
    public abstract void calculateSteering();
    
    public abstract void applySteering(float delta);
}