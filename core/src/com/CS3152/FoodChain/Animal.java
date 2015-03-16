package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public abstract class Animal extends Actor {

	//Whether the animal is caught in a trap
	private boolean trapped = false;

	private actorType type;
	
	// Vector that runs from the center of the animal diagonally leftward some length
    // RELATIVE TO ANIMAL'S POSITION
    protected Vector2 leftSectorLine;
    // Vector that runs from the center of the animal diagonally rightward that length
    // RELATIVE TO ANIMAL'S POSITION
    protected Vector2 rightSectorLine;
	
	//texture used in getCenter and setCenter
	private float texWidth;
	private float texHeight;
	//how far forward the hunter can move in a turn. 
    private static final float MOVE_SPEED = 150f;
    private static final double SIGHT_ANGLE = 0.35;
    private static final float SIGHT_LENGTH = 120;
	
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
		this.type = type;
		this.setPos(x,y);
		this.facing = facing;
		this.leftSectorLine = new Vector2(50, 30);
		this.rightSectorLine = new Vector2(50, -30);
		setTexWidth(tr.getRegionWidth());
		setTexHeight(tr.getRegionHeight());
	}
	
	public void setPos(float x, float y){
		
		float xPos = getX();
		float yPos = getY();
		
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
        super.setPosition(xPos, yPos);
	}
	
	public void updateLOS(float angle) {
		this.leftSectorLine.set((float)(SIGHT_LENGTH*Math.cos(angle + SIGHT_ANGLE)),
								(float)(SIGHT_LENGTH*Math.sin(angle + SIGHT_ANGLE)));

		this.rightSectorLine.set((float)(SIGHT_LENGTH*Math.cos(angle - SIGHT_ANGLE)),
								 (float)(SIGHT_LENGTH*Math.sin(angle - SIGHT_ANGLE)));
	}
	
    /**
     * @return the type
     */
    public actorType getType() {
        return type;
    }
    
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
	 * @return the bottom LeftxPos
	 */
	public boolean canEat(Animal ani){
	    for (actorType t : ani.getPrey()){
	        if (ani.getType() == t){
	            return true;
	        }
	    }
        return false;
	}
	
	public void draw(GameMap map, GameCanvas canvas){
	    canvas.begin();
	    canvas.draw(getTexture(), getX(), getY());
	    canvas.end();
	}
	
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
}
