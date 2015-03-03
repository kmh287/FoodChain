package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public abstract class Animal {
	public enum animalType{
		SHEEP, 
		WOLF
	}
	
	public enum direction{
	    NORTH, NORTHEAST, EAST, SOUTHEAST, 
	    SOUTH, SOUTHWEST, WEST, NORTHWEST
	}
	
	
	//The type of this animal
	private final animalType type;
	//The current xPosition of this animal
	private float xPos;
	//The current yPosition of this animal
	private float yPos;
	//The direction this animal is facing
	private direction facing;
	//The animals this animal can eat
	private final animalType[] prey;
	//texture used in getCenter adn setCenter
	private float texWidth;
	private float texHeight;
	
	
	/** Protected constructor for the animal type. 
	 * 
	 * This constructor should never be called directly
	 * Animals should be instantiated as specific animals
	 * 
	 * @param type The type of animal. Checl the animalType enum for values
	 * @param x	STARTING x-position on the map for the animal
	 * @param y STARTING y-position on the map for the animal
	 * @param foodchainVal The food chain value for this animal. 
	 *                     It is up to the CALLER to ensure this is correct.
	 */
	public Animal(animalType type, float x, float y, 
	              animalType[] prey, direction facing){
		this.type = type;
		this.setPos(x,y);
		this.prey = prey;
		this.facing = facing;
	}

	public direction getFacing(){
	    return this.facing;
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
	
	public void setPos(float x, float y){
	    
	    if (x - xPos == 0 && y - yPos > 0){
	        this.facing = direction.NORTH;
	    }
	    else if (x - xPos > 0 && y - yPos > 0){
	        this.facing = direction.NORTHEAST;
	    }
	    else if (x - xPos > 0 && y - yPos == 0){
	        this.facing = direction.EAST;
	    }
	    else if (x - xPos > 0 && y - yPos < 0){
	        this.facing = direction.SOUTHEAST;
	    }
	    else if (x - xPos == 0 && y - yPos < 0){
	        this.facing = direction.SOUTH;
	    }
	    else if (x - xPos < 0 && y - yPos < 0){
	        this.facing = direction.SOUTHWEST;
	    }
	    else if (x - xPos < 0 && y - yPos == 0){
	        this.facing = direction.WEST;
	    }
	    else if (x - xPos < 0 && y - yPos > 0){
	        this.facing = direction.NORTHWEST;
	    }
	    else{
	        System.out.println("Bug in setPos");
	    }
	    
	    this.xPos = x; this.yPos = y;
	    
	    
	}
	
    /**
     * @return the type
     */
    public animalType getType() {
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
		Vector2 pos = new Vector2(getxPos()+getTexWidth()/2, getyPos()+getTexHeight()/2);
        return pos;
	}
	
	/**
	 * Standard toString methoH
	 * @return A string representation of this animal's type and position
	 */
	public String toString(){
		String type = getTypeNameString();
		
		return type + "at x: " + getxPos() + " y: " + getyPos();
	}
	
	/**
	 * Return true if this animal can eat ani, false otherwise
	 * @param ani Another animal
	 * @return boolean, whether or not this animal can eat ani
	 */
	public boolean canEat(Animal ani){
	    for (animalType t : prey){
	        if (ani.getType() == t){
	            return true;
	        }
	    }
        return false;
	}
	
	/*
	 * Load the animal's texture.
	 * Do not load it again if it has already been loaded
	 */
	public abstract void loadTexture(AssetManager manager);
	
	public abstract Texture getTexture();
	
	public void draw(GameMap map, GameCanvas canvas){
	    Texture tex = getTexture();
	    canvas.begin();
	    canvas.draw(tex, this.xPos, this.yPos);
	    canvas.end();
    }


	
}
