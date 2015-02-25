package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public abstract class Animal {
	protected enum animalType{
		SHEEP, 
		WOLF
	}
	
	//The type of this animal
	private final animalType type;
	//The current xPosition of this animal
	private float xPos;
	//The current yPosition of this animal
	private float yPos;
	//The animals this animal can eat
	private final animalType[] prey;
	
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
	public Animal(animalType type, float x, float y, animalType[] prey){
		this.type = type;
		this.setxPos(x);
		this.setyPos(y);
		this.prey = prey;
	}

	/**
	 * @return the yPos
	 */
	public float getyPos() {
		return yPos;
	}

	/**
	 * @param yPos the yPos to set
	 */
	public void setyPos(float yPos) {
		this.yPos = yPos;
	}

	/**
	 * @return the xPos
	 */
	public float getxPos() {
		return xPos;
	}

	/**
	 * @param xPos the xPos to set
	 */
	public void setxPos(float xPos) {
		this.xPos = xPos;
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
	 * Standard toString method
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
