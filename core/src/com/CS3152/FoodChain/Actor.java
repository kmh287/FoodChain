package com.CS3152.FoodChain;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.World;

public class Actor extends BoxObject {
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
    		
    		//From now on, x and y position are handled by the 
    		//superclass of actor.
    		//Facing needs to be moved
    		
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
}