package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Trap {
	
	public enum TRAP_TYPE {
		REGULAR_TRAP,
		SHEEP_TRAP,
		WOLF_TRAP,
	}
	
	private String TRAP_FILE;
	private Texture trapTexture = null;
	
	private boolean inInventory;
	
	public Trap(String type) {
		TRAP_FILE = "assets/" + type + ".png";
		inInventory = false;
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
}
