package com.CS3152.FoodChain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

public class Hunter {
    
    //The player's inventory
    //We want amortized O(1) insert and O(1) lookup
    //Since we will not be shifting elements, 
    //an arraylist is ideal for this.
    private List<Trap> inventory;
    
    private static final String PLAYER_TEX = "assets/player.png";
    private static Texture tex = null;
    
    private float xPos;
    private float yPos;
    
    private boolean isSettingTrap;
    
    public Hunter(float xPos, float yPos, Trap t){
        this.setxPos(xPos);
        this.setyPos(yPos);
        inventory = new ArrayList<Trap>();
        inventory.add(t);
        
    }
    
    /**
     * Load the texture for the player
     * This must be called before any calls to Player.draw()
     * @param manager an AssetManager
     */
    public void loadTexture(AssetManager manager) {
        if (tex == null){
            manager.load(PLAYER_TEX, Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(PLAYER_TEX)){
                tex = manager.get(PLAYER_TEX);
            }
        }
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
     * 
     * @return returns if the player is setting a trap
     */
    public boolean getIsSettingTrap() {
    	return isSettingTrap;
    }
    
    /**
     * @param value whether the hunter is setting a trap
     */
    public void setIsSettingTrap(boolean value) {
    	isSettingTrap = value;
    }
    
    /**
     * 
     * @param trap the trap to add to the inventory
     */
    public void addToInventory(Trap trap) {
    	//inventory.add(trap);
    }
    
    /**
     * 
     * @param trap the trap to remove from the inventory
     */
    public void removeFromInventory(Trap trap) {
    	
    }
    
    public void draw(GameCanvas canvas){
        canvas.begin();
        canvas.draw(tex, this.xPos, this.yPos);
        canvas.end();
    }
    
}
