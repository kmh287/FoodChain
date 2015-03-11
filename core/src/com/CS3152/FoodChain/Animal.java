package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Animal extends Actor {

	//Whether the animal is caught in a trap
	private boolean trapped = false;

	//texture used in getCenter and setCenter
	private float texWidth;
	private float texHeight;
	//how far forward the hunter can move in a turn. 
    private static final float MOVE_SPEED = 150f;

	
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
	              actorType[] prey, direction facing){
		super(tr, type, x, y, tr.getRegionWidth(), tr.getRegionHeight(), prey);
		this.setPos(x,y);
		this.facing = facing;
		setTexWidth(tr.getRegionWidth());
		setTexHeight(tr.getRegionHeight());
	}
	
	public void setPos(float x, float y){
	    if (x - getPosition().x == 0 && y - getPosition().y > 0){
	        this.facing = direction.NORTH;
	    }
	    else if (x - getPosition().x > 0 && y - getPosition().y > 0){
	        this.facing = direction.NORTHEAST;
	    }
	    else if (x - getPosition().x > 0 && y - getPosition().y == 0){
	        this.facing = direction.EAST;
	    }
	    else if (x - getPosition().x > 0 && y - getPosition().y < 0){
	        this.facing = direction.SOUTHEAST;
	    }
	    else if (x - getPosition().x == 0 && y - getPosition().y < 0){
	        this.facing = direction.SOUTH;
	    }
	    else if (x - getPosition().x < 0 && y - getPosition().y < 0){
	        this.facing = direction.SOUTHWEST;
	    }
	    else if (x - getPosition().x < 0 && y - getPosition().y == 0){
	        this.facing = direction.WEST;
	    }
	    else if (x - getPosition().x < 0 && y - getPosition().y > 0){
	        this.facing = direction.NORTHWEST;
	    }
	    else{
	        //Standing still
	    		//do nothing
	    		//If this code doesn't change, remove this else
	    }
	    
        super.setPosition(x, y);
	}
	
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
	 * Standard toString methoH
	 * @return A string representation of this animal's type and position
	 */
	public String toString(){
		String type = getTypeNameString();
		
		return type + "at x: " + getX() + " y: " + getY();
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
    * Updates the hunter's position according to the controlCode. 
	* 
	* @param controlCode The movement controlCode (from InputController).
	*/
	public void update(int controlCode, float dt) {
    	super.update(dt);
    	// Determine how we are moving.
    	boolean movingEast  = (controlCode == InputController.EAST);
   		boolean movingWest = (controlCode == InputController.WEST);
    	boolean movingNorth = (controlCode == InputController.NORTH);
    	boolean movingSouth = (controlCode == InputController.SOUTH);
    	boolean movingNorthWest = (controlCode == InputController.NORTHWEST);
    	boolean movingSouthWest = (controlCode == InputController.SOUTHWEST);
    	boolean movingSouthEast = (controlCode == InputController.SOUTHEAST);
    	boolean movingNorthEast = (controlCode == InputController.NORTHEAST);
    	
    	//process moving command 
    	if (movingWest) {
			super.setAngle(0.0f);
			super.setVX(-MOVE_SPEED);
			super.setVY(0);
		} else if (movingEast) {
			super.setAngle(180.0f);
			super.setVX(MOVE_SPEED);
			super.setVY(0);
		}
		else if (movingNorth) {
			super.setAngle(90.0f);
			super.setVX(0);
			super.setVY(MOVE_SPEED);
		}
		else if (movingSouth) {
			super.setAngle(270.0f);
			super.setVX(0);
			super.setVY(-MOVE_SPEED);
		}
		else if (movingSouthWest) {
			super.setAngle(180.0f);
			super.setVX(-MOVE_SPEED);
			super.setVY(-MOVE_SPEED);
		}
		else if (movingSouthEast) {
			super.setAngle(180.0f);
			super.setVX(MOVE_SPEED);
			super.setVY(-MOVE_SPEED);
		}
		else if (movingNorthEast) {
			super.setAngle(180.0f);
			super.setVX(MOVE_SPEED);
			super.setVY(MOVE_SPEED);
		}
		else if (movingNorthWest) {
			super.setAngle(180.0f);
			super.setVX(-MOVE_SPEED);
			super.setVY(MOVE_SPEED);
		}
		else {
			// NOT MOVING, SO STOP MOVING
			super.setVX(0);
			super.setVY(0);
		}
    }
	    
	/**
	 * @return the bottom LeftxPos
	 */
	public float getXDiamter() {
	    return texWidth;
    }
}
