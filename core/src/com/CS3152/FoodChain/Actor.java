package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Actor extends BoxObject {
    // The current xPosition of this actor
    protected float xPos;
    // The current yPosition of this actor
    protected float yPos;
    //The direction this animal is facing
    protected direction facing;
    //TextureRegion for this actor's texture
    protected TextureRegion tr;
    
    public static enum direction {
        NORTH, NORTHEAST, EAST, SOUTHEAST,
        SOUTH, SOUTHWEST, WEST, NORTHWEST
    }
    
	public boolean activatePhysics(World world) {
		// create the box from our superclass
		return super.activatePhysics(world);
	}
    
    public Actor(TextureRegion tr, float x, float y, float width, float height){
    		super(tr,x,y,width,height);
    		this.tr = tr;
        this.xPos = x;
        this.yPos = y;
        this.facing = direction.WEST;
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
     * @return the yPos
     */
    public float getyPos() {
        return yPos;
    }
    
    /**
     * @return the xPos
     */
    public float getxPos() {
        return xPos;
    }
    
    /** Sets yPos
     */
    public float setyPos() {
        return yPos;
    }
    
    /** Sets xPos
     */
    public float setxPos() {
        return xPos;
    }
    
    public void setPosition(Vector2 pos) {
	    	xPos = pos.x;
	    	yPos = pos.y;
    }
}