package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Trap extends BoxObject {
	
	/**
	 * Enumeration to encode the trap type
	 */
	public enum TRAP_TYPE {
		REGULAR_TRAP,
		SHEEP_TRAP,
		WOLF_TRAP,
	}
	
	/** File for the regular trap */
	private static String REGULAR_TRAP = "assets/REGULAR_TRAP.png";
	/** File for the sheep trap */
	private static String SHEEP_TRAP = "assets/SHEEP_TRAP.png";
	/** File for the wolf trap */
	private static String WOLF_TRAP = "assets/WOLF_TRAP.png";
	/** The texture for the regular trap */
	protected static Texture regularTrapTexture = null;
	/** The texture for the sheep trap */
	protected static Texture sheepTrapTexture = null;
	/** The texture for the wolf trap */
	protected static Texture wolfTrapTexture = null;
	
	/** Whether the trap is in the hunters inventory */
	private boolean inInventory;
	/** Whether the trap is placed on the map */
	private boolean onMap;
	/** Whether the trap is the currently selected trap */
	private boolean isSelected;
	/** The type of the trap */
	private String type;
	
	/**
	 * Creates a new trap of a given type at point 0.0, 0.0 on the map.
	 * 
	 * @param texture Texture for the trap
	 * @param type Type of trap to create
	 */
	public Trap(Texture texture, String type) {
		super(new TextureRegion(texture), 0.0f, 0.0f, texture.getWidth(), texture.getHeight());
		inInventory = true;
		onMap = false;
		this.type=type;
	}
	
	/** 
	 * Preloads the assets for the Traps.
	 * 
	 * All trap objects use one of three textures, so this is a static method.  
	 * This keeps us from loading the same images multiple times for more than one 
	 * Trap object.
	 *
	 * The asset manager for LibGDX is asynchronous.  That means that you
	 * tell it what to load and then wait while it loads them.  This is 
	 * the first step: telling it what to load.
	 * 
	 * @param manager Reference to global asset manager.
	 */
	public static void PreLoadContent(AssetManager manager) {
		manager.load(REGULAR_TRAP,Texture.class);
		manager.load(SHEEP_TRAP,Texture.class);
		manager.load(WOLF_TRAP,Texture.class);
	}
	
	/**
     * Load the texture for the player
     * This must be called before any calls to Player.draw()
     * 
     * @param manager an AssetManager
     */
	public static void LoadContent(AssetManager manager) {
		if (manager.isLoaded(REGULAR_TRAP)) {
			regularTrapTexture = manager.get(REGULAR_TRAP,Texture.class);
			regularTrapTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		} else {
			regularTrapTexture = null;  // Failed to load
		}
		if (manager.isLoaded(SHEEP_TRAP)) {
			sheepTrapTexture = manager.get(SHEEP_TRAP,Texture.class);
			sheepTrapTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		} else {
			sheepTrapTexture = null;  // Failed to load
		}
		if (manager.isLoaded(WOLF_TRAP)) {
			wolfTrapTexture = manager.get(WOLF_TRAP,Texture.class);
			wolfTrapTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
		} else {
			wolfTrapTexture = null; // Failed to load
		}
	}
	
	/**
	 * Returns the type of the trap
	 * 
	 * @return Trap type
	 */
	public String getType(){
		return type;
	}
	
	/**
	 * Sets the type of the trap
	 * 
	 * @param t String representing the trap type
	 */
	public void setType(String t){
		type=t;
	}
    
    /**
     * Returns whether the trap is in the hunters inventory
     * 
     * @return inInventory whether the trap is in the player's inventory
     */
    public boolean getInInventory() {
    	return this.inInventory;
    }
    
    /**
     * Sets whether the trap is in the hunters inventory
     * 
     * @param value whether the trap is in the player's inventory.
     */
    public void setInInventory(boolean value) {
    	this.inInventory = value;
    }
    
    /**
     * Sets the position of the trap on the map at (x,y)
     * 
     * @param x the x component of the position at which to place the trap
     * @param y the y component of the position at which to place the trap
     */
    @Override
    public void setPosition(float x, float y) {
		super.setPosition(x, y);
		onMap = true;
    }
    
    /**
     * Sets the position of the trap on the map at the given point
     * represented by the vector
     * 
     * @param pos the position at which to place the trap on the map
     */
    @Override
    public void setPosition(Vector2 pos) {
    	super.setPosition(pos);
	    onMap = true;
    }
    
    /**
     * Sets whether the trap is on the map
     * 
     * @param val whether the trap is on the map
     */
    public void setOnMap(boolean val) {
    	onMap = val;
    }
    
    /**
     * Gets whether the trap is on the map
     * 
     * @return whether the trap is on the map
     */
    public boolean getOnMap() {
    	return onMap;
    }
    
    /**
     * Draws the trap on the GameCanvas if it is on the map
     * 
     * @param canvas the GameCanvas on which to draw the map
     */
    @Override
    public void draw(GameCanvas canvas){
        if (onMap) {
        	super.draw(canvas);
        }	
    }
}
