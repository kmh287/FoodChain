package com.CS3152.FoodChain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;


public abstract class Actor extends BoxObject {
    //The direction this actor is facing
    protected direction facing;
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
		WOLF
	}
    
	public boolean activatePhysics(World world) {
		// create the box from our superclass
		return super.activatePhysics(world);
	}
    
    public Actor(TextureRegion tr, actorType type, float x, float y, float width, 
    		     float height, actorType[] victims) {
	    super(tr,x,y,width,height);
	    //setDimension(tr.getRegionWidth(),tr.getRegionHeight());
	    setFixedRotation(true);
	    this.type = type;
	    this.tr = tr;
        this.facing = direction.WEST;
        this.victims = victims;
    }
    
    /*
     * @return the direction this Actor's facing
     */
    public direction getFacing() {
        return this.facing;
    }
    
    public void setFacing(direction dir) {
    		this.facing = dir;
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
	
    public void draw(GameCanvas canvas){
        canvas.begin();
        canvas.draw(getTexture(), getBody().getPosition().x, getBody().getPosition().y);
        canvas.end();
    }
	
}