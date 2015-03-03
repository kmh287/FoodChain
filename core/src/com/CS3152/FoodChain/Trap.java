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
	private float xPos, yPos;
	
	public Trap(String type) {
		TRAP_FILE = "assets/" + type + ".png";
		inInventory = false;
		onMap = false;
		xPos = 0.0f;
		yPos = 0.0f;
	}

	/**
     * Load the texture for the player
     * This must be called before any calls to Player.draw()
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
    	return inInventory;
    }
    
    /**
     * 
     * @param value whether the trap is in the player's inventory.
     */
    public void setInInventory(boolean value) {
    		inInventory = value;
    }
    
    public void setPosition(Vector2 pos) {
	    	xPos = pos.x;
	    	yPos = pos.y;
	    	onMap = true;
    }
    
    public void setOnMap() {
    		onMap = true;
    }
    
    public void draw(GameCanvas canvas){
        if (onMap) {
        	canvas.begin();
            canvas.draw(trapTexture, xPos - 20.0f, yPos - 20.0f);
            canvas.end();
        }	
    }
}
