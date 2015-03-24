package com.CS3152.FoodChain;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Trap extends BoxObject {
    private static float scaleXDrawTrap = 1f;
    private static float scaleYDrawTrap = 1f;
    
	public enum TRAP_TYPE {
		REGULAR_TRAP,
		SHEEP_TRAP,
		WOLF_TRAP,
	}
	
	 static String REGULAR_TRAP = "assets/REGULAR_TRAP.png";
	private static String SHEEP_TRAP = "assets/SHEEP_TRAP.png";
	private static String WOLF_TRAP = "assets/WOLF_TRAP.png";
	protected static Texture regularTrapTexture = null;
	protected static Texture sheepTrapTexture = null;
	protected static Texture wolfTrapTexture = null;
	private boolean inInventory;
	private boolean onMap;
	private boolean isSelected;
	private String type;
	
	public Trap(Texture texture, String type) {
		super(new TextureRegion(texture), 0.0f, 0.0f, texture.getWidth(), texture.getHeight());
		inInventory = true;
		onMap = false;
		this.type=type;
		drawScale.x = scaleXDrawTrap;
		drawScale.y = scaleYDrawTrap;
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
    
    /*public void setTextureRegion() {
    	texture.setRegion(trapTexture);
    }*/
	
	public String getType(){
		return type;
	}

	public void setType(String t){
		type=t;
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
    
    public void setPosition(float x, float y) {
		super.setPosition(x, y);
		onMap = true;
    }
    
    public void setPosition(Vector2 pos) {
    	super.setPosition(pos);
	    onMap = true;
    }
    
    public void setOnMap(boolean val) {
    	onMap = val;
    }
    
    public boolean getOnMap() {
    	return onMap;
    }
    
    public void draw(GameCanvas canvas){
        if (onMap) {
        	super.draw(canvas);
        }	
    }
}
