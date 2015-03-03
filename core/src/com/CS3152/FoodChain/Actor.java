package com.CS3152.FoodChain;

import com.badlogic.gdx.math.Vector2;

public class Actor {
    // The current xPosition of this actor
    protected float xPos;
    // The current yPosition of this actor
    protected float yPos;
    //The direction this animal is facing
    protected direction facing;
    
    public static enum direction {
        NORTH, NORTHEAST, EAST, SOUTHEAST,
        SOUTH, SOUTHWEST, WEST, NORTHWEST
    }
    
    public Actor(float x, float y)
    {
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