package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

public class Trap {
	
	public enum TRAP_TYPE {
		REGULAR_TRAP,
		SHEEP_TRAP,
		WOLF_TRAP,
	}
	
	private String TRAP_FILE;
	private Texture trapTexture = null;
	private boolean inInventory;
	private boolean onMap;
	private boolean isSelected;
	private Vector2 pos;
	private float xPos, yPos;
	private String type;
	
	public Trap(String type) {
		TRAP_FILE = "assets/" + type + ".png";
		inInventory = true;
		onMap = false;
		pos = new Vector2();
		xPos = 0.0f;
		yPos = 0.0f;
		this.type=type;
	}
	
	public String getType(){
		return type;
	}

	public void setType(String t){
		type=t;
	}
	/**
     * Load the texture for the player
     * This must be called before any calls to Player.draw()
     * 
     * @param manager an AssetManager
     */
    public void loadTexture(AssetManager manager) {
        if (trapTexture == null){
            manager.load(TRAP_FILE, Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(TRAP_FILE)){
                trapTexture = manager.get(TRAP_FILE);
            }
        }
    }
    
    /**
     * 
     * @return inInventory whether the trap is in the player's inventory
     */
    public boolean getInInventory() {
    	return this.inInventory;
    }
    
    /**
     * 
     * @param value whether the trap is in the player's inventory.
     */
    public void setInInventory(boolean value) {
    	this.inInventory = value;
    }
    
    public void setPosition(Vector2 pos) {
    		this.pos = pos;
    		//xPos = pos.x;
	    	//yPos = pos.y;
	    	onMap = true;
    }
    
    public Vector2 getPosition() {
    		return pos;
    }
    
    public void setOnMap() {
    		onMap = true;
    }
    
    public boolean getOnMap() {
    	return onMap;
    }
    
    public void draw(GameCanvas canvas){
        if (onMap) {
        	canvas.begin();
            canvas.draw(trapTexture, pos.x - 20.0f, pos.y - 20.0f);
            canvas.end();
        }	
    }
}
