package com.CS3152.FoodChain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;


public abstract class Actor extends CircleObject {
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
		SHEEP, 
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


	    	super(tr,x,y, height/2);

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
    		this.facing = dir;
    		/*if (dir.x < 0 && dir.y == 0) {
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
    		}*/
    		super.setAngle(dir.angleRad() + (float)Math.PI/2);
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
        System.out.println(getTypeNameString() + " draw angle: " + getAngle());
    	super.draw(canvas);
    }
	
}