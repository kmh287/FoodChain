package com.CS3152.FoodChain;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class Hunter extends Actor {
    
    //The player's inventory
    //We want amortized O(1) insert and O(1) lookup
    //Since we will not be shifting elements, 
    //an arraylist is ideal for this.
    private List<Trap> inventory;
    
    private static final String PLAYER_TEX = "assets/player.png";
    private static Texture tex = null;
    
    // private boolean isSettingTrap;
    
    private Trap selectedTrap;

    //how far forward the hunter can move in a turn. 
    private static final float MOVE_SPEED = 150.0f;
    /** How far the hunter can lay a trap from itself */
    private static final float TRAP_RADIUS = 50.0f;

	
	private Vector2 tmp;
    
    public Hunter(float xPos, float yPos, Trap t){
    	super(new TextureRegion(tex), actorType.HUNTER, xPos, yPos, tex.getWidth(),
    		  tex.getHeight(), new actorType[]{actorType.SHEEP});
        inventory = new ArrayList<Trap>();
        inventory.add(t);
        selectedTrap = t;
        tmp = new Vector2();
    }
    
    /**
     * Load the texture for the player
     * This must be called before any calls to Player.draw()
     * @param manager an AssetManager
     */
    public static void loadTexture(AssetManager manager) {
        if (tex == null){
            manager.load(PLAYER_TEX, Texture.class);
            manager.finishLoading();
            if (manager.isLoaded(PLAYER_TEX)){
                tex = manager.get(PLAYER_TEX);
            }
        }
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
    	//TODO
    }

    
    public boolean canSetTrap(Vector2 clickPos) {
		tmp.set(getPosition().add(20.0f, 20.0f));
		tmp.sub(clickPos);
		if (Math.abs(tmp.len()) <= TRAP_RADIUS) {
			return true;
		}
		return false;
    }
    
    public void setTrap(Vector2 clickPos) {
    		selectedTrap.setPosition(clickPos);
    }
    
    /**
     * @return the bottom LeftxPos
     */
    public float getXDiamter() {
        return tex.getWidth();
    }
    
    /** 
    * Updates the hunter's position according to the controlCode. 
    * 
    * @param controlCode The movement controlCode (from InputController).
    */
    public void update(int controlCode,float dt) {
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
	    	boolean settingTrap = (controlCode == InputController.CLICK);
	    	
	    	//process moving command 
	    	//need to set super commands and set diagonal movement to less
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
			else if (settingTrap) {
				super.setVX(0);
				super.setVY(0);
			}
			else {
				// NOT MOVING, SO STOP MOVING
				super.setVX(0);
				super.setVY(0);
			}
	    }
    
    public String getTypeNameString() {
    	return "Hunter";
    }
}
